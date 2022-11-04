package cdplib.cdp;

import cdplib.websocket.WebSocketSync;

public class CdpController {
	WebSocketSync ws;

	public CdpController(String debuggerUrl) {
		ws = new WebSocketSync(debuggerUrl);
	}

	public void sendJavascript(String javascript) {
		final String FORMAT = "{\"id\": 1,\"method\": \"Runtime.evaluate\",\"params\": {\"expression\": \"%s\"}}";

		try {
			ws.sendSync(String.format(FORMAT, javascript));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void input(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').value='%s'";
		this.sendJavascript(String.format(FORMAT, selector, value));

	}

	public void select(String selector, String value) {
		final String FORMAT  = "document.querySelector('%s').click()";
		this.sendJavascript(String.format(FORMAT, selector));
	}

	public void click(String selector) {
		final String FORMAT  = "document.querySelector('%s').click()";
		this.sendJavascript(String.format(FORMAT, selector));
	}
}
