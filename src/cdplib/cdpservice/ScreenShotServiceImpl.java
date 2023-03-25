package cdplib.cdpservice;

import java.io.FileOutputStream;
import java.util.Base64;

import cdplib.core.Emulation;
import cdplib.core.EmulationImpl;
import cdplib.core.Page;
import cdplib.core.PageImpl;
import cdplib.lib.DateUtil;
import cdplib.lib.ResponseJson;

public class ScreenShotServiceImpl implements ScreenShotService{
	WsSendService wss;

	public ScreenShotServiceImpl() throws Exception {
		wss = WsSendServiceFactory.getSerivceSingleton();
	}

	private String getBase64() {
		int id = 0;
		Page page = new PageImpl();
		Emulation emulation = new EmulationImpl();

		// 画面サイズの設定
		String layout = wss.send(page.getLayoutMetrics(id++));
		wss.send(emulation.setDeviceMetricsOveride(id++
				, ResponseJson.getResultCssContentSizeWidth(layout) + 100
				, ResponseJson.getResultCssContentSizeHeight(layout) + 100));

		// スクショ取得(少し時間かかるので長めにTimeout待ちする)
		String json = wss.send(page.captureScreenshot(id++), 500, 100);
		String base64str = ResponseJson.getResultData(json).asText();

		wss.send(emulation.clearDeviceMetricsOverride(id++));

		return base64str;
	}

	@Override
	public void takeFullScreen(String screenShotPath) {
		try {
			String base64str = getBase64();

			byte[] screenshotData = Base64.getDecoder().decode(base64str);
			String screenshotPath = screenShotPath;
			try (FileOutputStream fos = new FileOutputStream(screenshotPath)) {
				fos.write(screenshotData);
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void takeFullScreenDL() {
		String base64str = getBase64();
		String js = createImageDownloadLink(base64str);

		wss.sendJavascript(js);
	}

	/**
	 * 画像のダウンロードリンク(javascript)を作成する
	 */
	private static String createImageDownloadLink(String data) {
		String fileName = "imgage_" + DateUtil.getYYYYMMDDHHMMSS();

		String toBlob = "function toBlob(base64) {\r\n"
				+ "    var bin = atob(base64.replace(/^.*,/, ''));\r\n"
				+ "    var buffer = new Uint8Array(bin.length);\r\n"
				+ "    for (var i = 0; i < bin.length; i++) {\r\n"
				+ "        buffer[i] = bin.charCodeAt(i);\r\n"
				+ "    }\r\n"
				+ "    // Blobを作成\r\n"
				+ "    try{\r\n"
				+ "        var blob = new Blob([buffer.buffer], {\r\n"
				+ "            type: 'image/png'\r\n"
				+ "        });\r\n"
				+ "    }catch (e){\r\n"
				+ "        return false;\r\n"
				+ "    }\r\n"
				+ "    return blob;\r\n"
				+ "}\r\n";
		String cmd = "var filename = 'image';\r\n"
				+ "var file = toBlob('" + data + "');\r\n"
				+ "if (window.navigator.msSaveOrOpenBlob) // IE10+\r\n"
				+ "  window.navigator.msSaveOrOpenBlob(file, filename);\r\n"
				+ "else { // Others\r\n"
				+ "  var a = document.createElement(\"a\"),\r\n"
				+ "  url = URL.createObjectURL(file);\r\n"
				+ "  a.href = url;\r\n"
				+ "  a.download = '" + fileName +"';\r\n"
				+ "  document.body.appendChild(a);\r\n"
				+ "  a.click();\r\n"
				+ "  setTimeout(function () {\r\n"
				+ "    document.body.removeChild(a);\r\n"
				+ "    window.URL.revokeObjectURL(url);\r\n"
				+ "  }, 0);\r\n"
				+ "};";
		return toBlob + cmd;
	}
}
