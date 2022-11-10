package cdplib.cdp;

import cdplib.websocket.WebSocketSync;

public class CdpController {
	WebSocketSync ws;

	public CdpController(String debuggerUrl) {
		ws = new WebSocketSync(debuggerUrl);
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

	public String sendJavascript(String javascript) {
		final String FORMAT = "{\"id\": 1,\"method\": \"Runtime.evaluate\",\"params\": {\"expression\": \"%s\"}}";
		return this.send(String.format(FORMAT, javascript));
	}

	public String input(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').value='%s'";
		return this.sendJavascript(String.format(FORMAT, selector, value));
	}

	public String select(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').click()";
		return this.sendJavascript(String.format(FORMAT, selector));
	}

	public String click(String selector) {
		final String FORMAT  = "document.querySelector('%s').click()";
		return this.sendJavascript(String.format(FORMAT, selector));
	}
}
