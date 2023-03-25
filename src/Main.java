import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import cdplib.cdp.CdpController;
import cdplib.cdp.CdpControllerImpl;

/**
 * コマンドライン実行用のMainソース
 *
 */
public class Main {
	static Scanner scanner = new Scanner(System.in);
	static CdpController controller;

	public static void main(String[] args) throws Exception {
//		CdpInfo info;
//		try {
//			// currentの情報取得
//			info = new CdpInfo();
//		} catch (MalformedURLException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//			return;
//		}
//
//		System.out.println(info.getTitle());
//		System.out.println(info.getWebSocketDebuggerUrl());
//		controller = new CdpController(info.getWebSocketDebuggerUrl());

		controller = new CdpControllerImpl();

		scan();
	}

	public static void scan() {
		boolean endFlg = false;
		while (!endFlg) {
			System.out.print(">");
			String input_text = scanner.nextLine();
			endFlg = parseCmd(input_text);
		}

		scanner.close();
	}

	/**
	 * CDPコマンド呼び出し
	 * 　適宜cdplibのコマンドを追加
	 * @param text
	 * @return
	 */
	public static boolean parseCmd(String text) {
		String[] params = text.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		for(int cnt = 1; cnt < params.length; cnt++) {
			params[cnt] = trimDoubleQuot(params[cnt]);
		}

		if ("q".equals(text) ) {
			System.out.println("end");
			controller.disConnect();
			return true;
		}

		/**
		 * 基本系
		 */
		// wssend [json]
		if (text.startsWith("wssend ")) {
			controller.send(params[1]);
			return false;
		}

		// js [javascript]
		if (text.startsWith("js ")) {
			controller.sendJavascript(params[1]);
			return false;
		}

		/**
		 * html操作系
		 */
		// input [selector] [value]
		if (text.startsWith("input ")) {
			controller.input(params[1], params[2]);
			return false;
		}

		// select [selector] [value]
		if (text.startsWith("select ")) {
			controller.select(params[1], params[2]);
			return false;
		}

		// click [selector]
		if (text.startsWith("click ")) {
			controller.click(params[1]);
			return false;
		}

		// upload [selector] [filePath]
		if (text.startsWith("upload ")) {
			controller.fileUpload(params[1], params[2]);
			return false;
		}

		/**
		 * wait系
		 */
		// waitForSelector [selector] [waitmilsec]
		if (text.startsWith("waitForSelector ")) {
			try {
				controller.waitForSelector(params[1], Integer.parseInt(params[2]));
			} catch (NumberFormatException | TimeoutException e) {
				e.printStackTrace();
			}
			return false;
		}

		// waitForNavigation [urlStartWith] [urlEndWith] [waitmilsec]
		if (text.startsWith("waitForNavigation ")) {
			try {
				controller.waitForNavigation(params[1], params[2], Integer.parseInt(params[3]));
			} catch (NumberFormatException | TimeoutException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return true;
			}
			return false;
		}

		// waitForNavigationEndwith [urlEndWith] [waitmilsec]
		if (text.startsWith("waitForNavigationEndwith ")) {
			try {
				controller.waitForNavigationEndWith(params[1], Integer.parseInt(params[2]));
			} catch (NumberFormatException | TimeoutException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return true;
			}
			return false;
		}

		// waitForTimeout [waitmilsec]
		if (text.startsWith("waitForTimeout ")) {
			controller.waitForTimeout(Integer.parseInt(params[1]));
			return false;
		}

		/**
		 *	その他
		 */
		// windowOpen [url]
		if (text.startsWith("windowOpen ")) {
			controller.windowOpen(params[1]);
			return false;
		}

		// windowClose
		if (text.startsWith("windowClose")) {
			controller.windowClose();
			return false;
		}

		// takeFullScreen
		if (text.startsWith("takeFullScreen")) {
			try {
				controller.takeFullScreenDL();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			return false;
		}

		// getDialogMessage
		if (text.startsWith("getDialogMessage")) {
			System.out.println(controller.getDialogMessage());
			return false;
		}

		// clickDialogSelector
		if (text.startsWith("clickDialogSelector")) {
			controller.clickDialogSelector();
			return false;
		}

		// getVersion
		if (text.startsWith("getVersion")) {
			System.out.println(controller.getVersion());
			return false;
		}

		return false;
	}

	public static String trimDoubleQuot(String str) {
		char c = '"';
		if (str.charAt(0) == c && str.charAt(str.length() - 1) == c) {
			return str.substring(1, str.length() - 1);
		} else {
			return str;
		}
	}
}
