package cdplib.cdpservice;

import cdplib.websocket.WebSocketSync;

public class WsSendServiceFactory {
	private static WsSendService _instance;
	private static WebSocketSync _ws;

	public static void setWebSocket(WebSocketSync ws) {
		_ws = ws;
	}

	public static WsSendService getSerivceSingleton() throws Exception {
		if (_ws == null) {
			throw new Exception("web socket is null");
		}
		if(_instance == null) {
			_instance = new WsSendServiceImpl(_ws);
		}

		return _instance;
	}
}
