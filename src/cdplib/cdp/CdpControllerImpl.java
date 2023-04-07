package cdplib.cdp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cdplib.cdpdata.EventResource;
import cdplib.cdpdata.eventresponse.page.JavascriptDialogOpening;
import cdplib.cdpservice.ScreenShotService;
import cdplib.cdpservice.ScreenShotServiceImpl;
import cdplib.cdpservice.WsSendService;
import cdplib.cdpservice.WsSendServiceFactory;
import cdplib.cdpservice.XpathService;
import cdplib.cdpservice.XpathServiceImpl;
import cdplib.core.Page;
import cdplib.core.PageImpl;
import cdplib.resource.CdpCommandStrings.PageEvents;
import cdplib.websocket.WebSocketSync;
import debug.CLogger;

public class CdpControllerImpl implements CdpController{
	WebSocketSync ws;
	WsSendService wss;
	XpathService xpathService;
	final int SLEEP_ONE_MILTIME = 100;
	String windowTargetId = "";

//	public CdpControllerImpl(String debuggerUrl) throws Exception {
//		ws = new WebSocketSync(debuggerUrl);
//
//		WsSendServiceFactory.setWebSocket(ws);
//		wss = WsSendServiceFactory.getSerivceSingleton();
//	}

	public CdpControllerImpl() throws Exception {
		this._currentConnect();
		this.xpathService = new XpathServiceImpl();
	}

	public void disConnect() {
		ws.disconnect();
	}

	private JsonNode jsonParse(String json) throws IOException {
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
	 * @throws Exception
	 */
	private void _currentConnect() throws Exception {
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

		WsSendServiceFactory.setWebSocket(ws);
		wss = WsSendServiceFactory.getSerivceSingleton();
	}

	@Deprecated
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
	@Deprecated
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
//		ObjectMapper mapper = new ObjectMapper();
//		ObjectNode root = mapper.createObjectNode();
//		root.put("id", 1);
//		root.put("method", "Runtime.evaluate");
//
//		ObjectNode params = mapper.createObjectNode();
//		params.put("expression", javascript);
//
//		root.set("params", params);
//
//		return this.sendJsonNode(mapper, root);

		return wss.sendJavascript(javascript);
	}

 	private boolean isVisibled(String selector) {
 		String ret = this.sendJavascript(""
 				+ "{"
 				+ 	"let selector = '" + selector + "';"
 				+ 	"function isVisible (selector) {"
 				+ 		"let element = document.querySelector(selector);"
 				+ 		"if (element.offsetWidth > 0 && element.offsetHeight > 0) {"
 				+			"return true;"
 				+ 		"}"
 				+ 		"return false;"
 				+ 	"}"
 				+ 	"isVisible(selector);"
 				+ "}");
 		try {
			return jsonParse(ret).get("result").get("result").get("value").asBoolean();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
 	}

 	@Override
 	public String input(String selector, String value) throws Exception {
 		if (!isVisibled(selector)) {
 			throw new Exception(String.format("selector(%s) cannot input", selector));
 		}
 		return this.input(selector, value);
 	}

	public String select(String selector, String value) throws Exception {
 		if (!isVisibled(selector)) {
 			throw new Exception(String.format("selector(%s) cannot select", selector));
 		}
		final String FORMAT  = "document.querySelector('%s').value = %s";
		return this.sendJavascript(String.format(FORMAT, selector, value));
	}

	public String click(String selector) throws Exception {
 		if (!isVisibled(selector)) {
 			throw new Exception(String.format("selector(%s) cannot click", selector));
 		}
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

	private int getRootNodeId() throws IOException {
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

	private int getNodeId(String selector) throws IOException {
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
		} catch (IOException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (cnt >= maxloopCnt) {
			throw new TimeoutException();
		}

		return;
	}

	@Override
	public String getUrl() {
		String retJson = this.sendJavascript("location.href");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readTree(retJson);
		} catch (IOException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		// wsの向き先を変える
		ws.disconnect();
		try {
			this._currentConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@Override
	public void scrollDown() {
		wss.sendJavascript("window.scrollTo(0, window.document.body.scrollHeight);");
	}

//	@Override
//	public void takeFullScreen() throws Exception {
//		ScreenShotService service = new ScreenShotServiceImpl();
//		String screenshotPath = "fullscreen-screenshot.png";
//		service.takeFullScreen(screenshotPath);
//	}

	@Override
	public void takeFullScreenDL() throws Exception {
		ScreenShotService service = new ScreenShotServiceImpl();
		service.takeFullScreenDL();
	}

	@Override
	public void pageEnable() {
		Page page = new PageImpl();
		wss.send(page.enable(1), false);
	}

	@Override
	public String getDialogMessage() {
		JsonNode json = EventResource.getInstance()
							.getData(PageEvents.javascriptDialogOpening);
		JavascriptDialogOpening response = new JavascriptDialogOpening();
		response.parse(json);

		return response.getMessage();
	}

	@Override
	public String clickDialogSelector() {
		//イベントデータを取得して、ダイアログが開いているかを取得する
		JsonNode json = EventResource.getInstance()
							.getData(PageEvents.javascriptDialogOpening);
		JavascriptDialogOpening response = new JavascriptDialogOpening();
		response.parse(json);

		//ダイアログが開いている場合、accept実行
		if(response.isHasBrowserHandler()) {
			Page page = new PageImpl();
			wss.send(page.handleJavaScriptDialog(1, true, ""));
		}
		return response.getMessage();
	}
}
