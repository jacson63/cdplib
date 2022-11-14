import java.net.MalformedURLException;
import java.util.Scanner;

import cdplib.cdp.CdpController;
import cdplib.cdp.CdpInfo;

public class Main {
	static Scanner scanner = new Scanner(System.in);
	static CdpController controller;

	public static void main(String[] args) {
		CdpInfo info;
		try {
			// currentの情報取得
			info = new CdpInfo();
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}

		System.out.println(info.getTitle());
		System.out.println(info.getWebSocketDebuggerUrl());
		controller = new CdpController(info.getWebSocketDebuggerUrl());

		scan();
	}

	public static void scan() {
		boolean endFlg = false;
		while(!endFlg) {
			System.out.print(">");
			String input_text = scanner.nextLine();
			endFlg = parseCmd(input_text);
		}

		scanner.close();
	}

	public static boolean parseCmd(String text) {
		String[] params = text.split(" ");
		if ("q".equals(text) ) {
			return true;
		}

		// wssend [json]
		if (text.startsWith("wssend ")) {
			controller.send(params[1]);
		}

		// js [javascript]
		if (text.startsWith("js ")) {
			controller.sendJavascript(params[1]);
		}

		// input [selector] [value]
		if (text.startsWith("input ")) {
			controller.input(params[1], params[2]);
		}

		// select [selector] [value]
		if (text.startsWith("select ")) {
			controller.select(params[1], params[2]);
		}

		// click [selector]
		if (text.startsWith("click ")) {
			controller.click(params[1]);
		}

		// upload [selector] [filePath]
		if (text.startsWith("upload ")) {
			controller.fileUpload(params[1], params[2]);
		}

		return false;
	}
}
