package cdplib.cdp;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cdplib.websocket.WebSocketSync;
import debug.CLogger;

public class CdpControllerImpl implements CdpController{
	final String VERSION = "v20230324";
	WebSocketSync ws;
	final int SLEEP_ONE_MILTIME = 100;
	String windowTargetId = "";
	private enum ScreenShotParam {
		height(1920),
		width(1080);

		private int value;

		ScreenShotParam(int value) {
			this.value = value;
		}
	};

	public CdpControllerImpl(String debuggerUrl) {
		ws = new WebSocketSync(debuggerUrl);
	}

	public CdpControllerImpl() {
		this._currentConnect();
	}

	private JsonNode jsonParse(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.readTree(json);
	}

	private void clearWindowTargetId() {
		this.windowTargetId = "";
	}

	private boolean isEmpty(String val) {
		if(val == null) {
			return true;
		}
		if("".equals(val)) {
			return true;
		}
		return false;
	}
	/**
	 * 現在開いているタブのデバッグポートに接続する
	 */
	private void _currentConnect() {
		CdpInfo info;
		try {
			// currentの情報取得
			info = new CdpInfo();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}
		CLogger.finest("ws info-------------------------");
		CLogger.finest("window title: " + info.getTitle());
		CLogger.finest("debugger url: " + info.getWebSocketDebuggerUrl());
		ws = new WebSocketSync(info.getWebSocketDebuggerUrl());
	}

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

	public String input(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').value='%s'";
		return this.sendJavascript(String.format(FORMAT, selector, value));
	}

	public String select(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s') = %s";
		return this.sendJavascript(String.format(FORMAT, selector, value));
	}

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

	public void waitForSelector(String selector, int waitingMilsec) throws TimeoutException {
		int cnt = 0;
		int maxloopCnt = waitingMilsec / SLEEP_ONE_MILTIME;
		try {
			// nodeIdが取得できるまでループ
			for(; cnt < maxloopCnt; cnt++) {
				int nodeId = this.getNodeId(selector);
				if (nodeId > 0) {
					break;
				}
				waitForTimeout();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		if (cnt >= maxloopCnt) {
			throw new TimeoutException();
		}

		return;
	}

	private String getUrl() {
		String retJson = this.sendJavascript("location.href");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readTree(retJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
		return node.get("result").get("result").get("value").toString().replace("\"", "");
	}

	private interface IWaitForNavigationChk {
		boolean isStartWithUrl(String baseUrl, String startWithUrl);
		boolean isEndWithUrl(String baseUrl, String endWithUrl);
	}

	/**
	 * urlで指定したページに移動するまで待つ(共通処理)
	 * @param chk urlチェック用メソッドのインタフェース
	 * @param startWithUrl
	 * @param endWithUrl
	 * @param waitingMilsec
	 * @throws TimeoutException
	 */
	private void _waitForNavigation(IWaitForNavigationChk chk, String startWithUrl, String endWithUrl, int waitingMilsec) throws TimeoutException {
		//現在のurl取得
		int cnt = 0;
		int maxloopCnt = waitingMilsec / SLEEP_ONE_MILTIME;
		for (; cnt < maxloopCnt; cnt++) {
			String retUrl = getUrl();
			if (chk.isStartWithUrl(retUrl, startWithUrl)
					&& chk.isEndWithUrl(retUrl, endWithUrl)) {
				break;
			}
			waitForTimeout();
		}

		if (cnt >= maxloopCnt) {
			throw new TimeoutException();
		}

		return;
	}

	public void waitForNavigationEndWith(String endWithUrl, int waitingMilsec) throws TimeoutException {
		IWaitForNavigationChk chk = new IWaitForNavigationChk() {
			@Override
			public boolean isStartWithUrl(String baseUrl, String startWithUrl) {
				return true;
			}

			@Override
			public boolean isEndWithUrl(String baseUrl, String endWithUrl) {
				return baseUrl.endsWith(endWithUrl);
			}
		};
		this._waitForNavigation(chk, "", endWithUrl, waitingMilsec);
	}

	public void waitForNavigation(String startWithUrl, int waitingMilsec) throws TimeoutException {
		IWaitForNavigationChk chk = new IWaitForNavigationChk() {
			@Override
			public boolean isStartWithUrl(String baseUrl, String startWithUrl) {
				return baseUrl.startsWith(startWithUrl);
			}

			@Override
			public boolean isEndWithUrl(String baseUrl, String endWithUrl) {
				return true;
			}
		};
		this._waitForNavigation(chk, startWithUrl, "", waitingMilsec);
	}

	public void waitForNavigation(String startWithUrl, String endWithUrl, int waitingMilsec) throws TimeoutException {
		IWaitForNavigationChk chk = new IWaitForNavigationChk() {
			@Override
			public boolean isStartWithUrl(String baseUrl, String startWithUrl) {
				return baseUrl.startsWith(startWithUrl);
			}

			@Override
			public boolean isEndWithUrl(String baseUrl, String endWithUrl) {
				return baseUrl.endsWith(endWithUrl);
			}
		};
		this._waitForNavigation(chk, startWithUrl, endWithUrl, waitingMilsec);
	}

	public void waitForTimeout() {
		waitForTimeout(SLEEP_ONE_MILTIME);
	}

	public void waitForTimeout(long milsec) {
		try {
			Thread.sleep(milsec);
		} catch (InterruptedException e) {
			//none
		}
	}

	public void windowOpen(String url) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "Target.createTarget");

		ObjectNode params = mapper.createObjectNode();
		params.put("url", url);

		root.set("params", params);
		String ret = this.sendJsonNode(mapper, root);

		// retのjsonをパースしてtargetIdを取得
		this.clearWindowTargetId();
		try {
			JsonNode node = jsonParse(ret);
			this.windowTargetId = node.get("result").get("targetId").asText();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// wsの向き先を変える
		ws.disconnect();
		this._currentConnect();
	}

	@Override
	public void windowClose() {
		if (isEmpty(this.windowTargetId)) {
			//targetId無い場合は何もしない
			CLogger.finest("windowClose targetId=null");
			return;
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "Target.closeTarget");

		ObjectNode params = mapper.createObjectNode();
		params.put("targetId", this.windowTargetId);

		root.set("params", params);
		this.sendJsonNode(mapper, root);

		this.clearWindowTargetId();
		ws.disconnect();
	}

	private void pageEnable(int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", id);
		root.put("method", "Page.enable");
		this.sendJsonNode(mapper, root);
	}

	private void setVisibleSize(int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", id);
		root.put("method", "Emulation.setVisibleSize");

		ObjectNode params = mapper.createObjectNode();
		params.put(ScreenShotParam.width.name(), ScreenShotParam.width.value);
		params.put(ScreenShotParam.height.name(), ScreenShotParam.height.value);

		root.set("params", params);
		this.sendJsonNode(mapper, root);
	}

	private void setDeviceMetricsOveride(int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", id);
		root.put("method", "Emulation.setDeviceMetricsOverride");

		ObjectNode params = mapper.createObjectNode();
		params.put(ScreenShotParam.width.name(), ScreenShotParam.width.value);
		params.put(ScreenShotParam.height.name(), ScreenShotParam.height.value);
		params.put("deviceScaleFactor", 1);
		params.put("mobile", false);

		root.set("params", params);
		this.sendJsonNode(mapper, root);
	}

	private void resetViewPort(int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", 1);
		root.put("method", "Emulation.clearDeviceMetricsOverride");
		this.sendJsonNode(mapper, root);
	}

	private String captureScreenshot(int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", id);
		root.put("method", "Page.captureScreenshot");
		return this.sendJsonNode(mapper, root);
	}

	@Override
	public void takeFullPicture() {
		// 画像サイズの設定
		pageEnable(1);
		setVisibleSize(2);
		setDeviceMetricsOveride(3);

		// スクショ取得
		String ret = captureScreenshot(4);
		System.out.println(ret);

        byte[] screenshotData = Base64.getDecoder().decode(ret);
        String screenshotPath = "C:\\Users\\jacson32\\Desktop\\programing\\chrome_gomidata\\fullscreen-screenshot.png";
        try (FileOutputStream fos = new FileOutputStream(screenshotPath)) {
            fos.write(screenshotData);
        } catch (Exception e) {
			e.printStackTrace();
		}

		resetViewPort(5);
	}

	@Override
	public String getDialogMessage() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String clickDialogSelector() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getVersion() {
		return this.VERSION;
	}
}
