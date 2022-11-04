import java.net.MalformedURLException;

import cdplib.websocket.CdpInfo;

public class Main {

	public static void main(String[] args) {
		CdpInfo info;
		try {
			info = new CdpInfo();
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}

		System.out.println(info.getTitle());
		System.out.println(info.getWebSocketDebuggerUrl());
	}

}
