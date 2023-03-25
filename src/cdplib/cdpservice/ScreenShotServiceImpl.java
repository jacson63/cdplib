package cdplib.cdpservice;

import java.io.FileOutputStream;
import java.util.Base64;

import cdplib.core.Emulation;
import cdplib.core.EmulationImpl;
import cdplib.core.Page;
import cdplib.core.PageImpl;

public class ScreenShotServiceImpl implements ScreenShotService{
	WsSendService wss;
	private enum ScreenShotParam {
		height(1920),
		width(1080);

		private int value;

		ScreenShotParam(int value) {
			this.value = value;
		}
	};

	public ScreenShotServiceImpl() throws Exception {
		wss = WsSendServiceFactory.getSerivceSingleton();
	}

	@Override
	public void takeFullScreen(String screenShotPath) {
		int id = 0;
		Page page = new PageImpl();
		Emulation emulation = new EmulationImpl();

		// 画面サイズの設定
		wss.send(page.enable(id++));
		wss.send(emulation.setVisibleSize(id++
				, ScreenShotParam.width.value
				, ScreenShotParam.height.value));
		wss.send(emulation.setDeviceMetricsOveride(id++
				, ScreenShotParam.width.value
				, ScreenShotParam.height.value));

		// スクショ取得
		String base64str = wss.send(page.captureScreenshot(id++));

        byte[] screenshotData = Base64.getDecoder().decode(base64str);
        String screenshotPath = screenShotPath;
        try (FileOutputStream fos = new FileOutputStream(screenshotPath)) {
            fos.write(screenshotData);
        } catch (Exception e) {
			e.printStackTrace();
		}

        wss.send(emulation.clearDeviceMetricsOverride(id++));
		wss.send(page.disable(id++));
	}

}
