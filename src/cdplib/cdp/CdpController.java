package cdplib.cdp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cdplib.websocket.WebSocketSync;

public class CdpController {
	WebSocketSync ws;

	public CdpController(String debuggerUrl) {
		ws = new WebSocketSync(debuggerUrl);
	}

	/**
	 * websocket送信
	 * @param message
	 * @return
	 */
	public String send(String message) {
		String res = "";
		try {
			res = ws.sendSync(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * websocket送信(jackson node版)
	 * @param message
	 * @return
	 */
	private String sendJsonNode(ObjectMapper mapper, ObjectNode root) {
		String json = "";
		try {
			json  = mapper.writeValueAsString(root);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return this.send(json);
	}

	/**
	 * cdpのjavascript実行送信
	 * @param javascript
	 * @return
	 */
	public String sendJavascript(String javascript) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "Runtime.evaluate");

		ObjectNode params = mapper.createObjectNode();
		params.put("expression", javascript);

		root.set("params", params);

		return this.sendJsonNode(mapper, root);
	}

	/**
	 * htmlのinputに値を入れる
	 * @param selector
	 * @param value
	 * @return
	 */
	public String input(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').value='%s'";
		return this.sendJavascript(String.format(FORMAT, selector, value));
	}

	/**
	 * htmlのselect選択
	 * @param selector
	 * @param value
	 * @return
	 */
	public String select(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').click()";
		return this.sendJavascript(String.format(FORMAT, selector));
	}

	/**
	 * htmlのbutton click
	 * @param selector
	 * @param value
	 * @return
	 */
	public String click(String selector) {
		final String FORMAT  = "document.querySelector('%s').click()";
		return this.sendJavascript(String.format(FORMAT, selector));
	}

	//作成中
	public String domEnable() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "DOM.enable");

		return this.sendJsonNode(mapper, root);
	}

	public String domGetDocument() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "DOM.getDocument");

		return this.sendJsonNode(mapper, root);
	}

	private int getRootNodeId() throws JsonMappingException, JsonProcessingException {
		String retJson = domGetDocument();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		node = mapper.readTree(retJson);

		return Integer.parseInt(node.get("result").get("root").get("nodeId").toString());
	}

	public String domQuerySelector(int nodeId, String selector) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "DOM.querySelector");

		ObjectNode params = mapper.createObjectNode();
		params.put("nodeId", nodeId);
		params.put("selector", selector);

		root.set("params", params);

		return this.sendJsonNode(mapper, root);
	}

	private int getNodeId(String selector) throws JsonMappingException, JsonProcessingException {
		int	rootNodeId = getRootNodeId();
		String retJson  = domQuerySelector(rootNodeId, selector);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		node = mapper.readTree(retJson);

		return Integer.parseInt(node.get("result").get("nodeId").toString());
	}

	/**
	 * htmlのinput fileにファイルを転送
	 * @param selector
	 * @param value
	 * @return
	 */
	public String fileUpload(String selector, String filePath) {
		int nodeId;
		try {
			nodeId = getNodeId(selector);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "DOM.setFileInputFiles");

		ObjectNode params = mapper.createObjectNode();
		ArrayNode files = mapper.createArrayNode();
		files.add(filePath);
		params.set("files", files);
		params.put("nodeId", nodeId);

		root.set("params", params);
		return this.sendJsonNode(mapper, root);
	}
}
