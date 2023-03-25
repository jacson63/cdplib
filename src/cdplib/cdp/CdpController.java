package cdplib.cdp;

import java.util.concurrent.TimeoutException;

public interface CdpController {
	public void disConnect();
	public String send(String message) ;

	/**
	 * cdpのjavascript実行送信
	 * @param javascript
	 * @return
	 */
	public String sendJavascript(String javascript) ;


	/**
	 * htmlのinputに値を入れる
	 * @param selector
	 * @param value
	 * @return
	 */
	public String input(String selector, String value) ;

	/**
	 * htmlのselect選択
	 * @param selector
	 * @param value
	 * @return
	 */
	public String select(String selector, String value) ;

	/**
	 * htmlのbutton click
	 * @param selector
	 * @param value
	 * @return
	 */
	public String click(String selector) ;

	public String domEnable() ;

	/**
	 * 表示しているdomの情報を取得する
	 * @return
	 */
	public String domGetDocument() ;

	public String domQuerySelector(int nodeId, String selector) ;

	/**
	 * htmlのinput fileにファイルを転送
	 * @param selector
	 * @param value
	 * @return
	 */
	public String fileUpload(String selector, String filePath) ;

	/**
	 * selectorの要素が使えるようになるまで待つ
	 * @param selector
	 * @param waitingMilsec
	 * @throws TimeoutException
	 */
	public void waitForSelector(String selector, int waitingMilsec) throws TimeoutException ;

	/**
	 * urlで指定したページに移動するまで待つ(後方一致)
	 * @param endWithUrl
	 * @param waitingMilsec
	 * @throws TimeoutException
	 */
	public void waitForNavigationEndWith(String endWithUrl, int waitingMilsec) throws TimeoutException ;

	/**
	 * urlで指定したページに移動するまで待つ
	 * @param startWithUrl
	 * @param waitingMilsec
	 * @throws TimeoutException
	 */
	public void waitForNavigation(String startWithUrl, int waitingMilsec) throws TimeoutException ;

	/**
	 * urlで指定したページに移動するまで待つ
	 * @param startWithUrl
	 * @param endWithUrl
	 * @param waitingMilsec
	 * @throws TimeoutException
	 */
	public void waitForNavigation(String startWithUrl, String endWithUrl, int waitingMilsec) throws TimeoutException ;

	/**
	 * wait(規定秒待ち)
	 */
	public void waitForTimeout() ;

	/**
	 * wait
	 * @param milsec
	 */
	public void waitForTimeout(long milsec) ;

	/**
	 * 新しいウィンドウを開き、Controllerの向きを新規ウィンドウに切り替える
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public void windowOpen(String url) ;

	/**
	 * windowを閉じる
	 */
	public void windowClose();

	/**
	 * スクリーンショットを取得する
	 */
//	public void takeFullScreen() throws Exception;
	public void takeFullScreenDL() throws Exception;

	/**
	 * 表示しているalertのメッセージを取得する
	 * @return
	 */
	public String getDialogMessage();

	/**
	 * ダイアログの選択肢を選択する
	 * @return
	 */
	public String clickDialogSelector();

	/**
	 *
	 * @return
	 */
	public String getVersion();
}
