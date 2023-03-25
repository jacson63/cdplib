package cdplib.cdpservice;

import cdplib.cdplogic.CdpCallbackLogic;
import cdplib.websocket.WebSoketCallback;

public interface CdpCallbackService extends WebSoketCallback{
	public void callback(String json);

	/**
	 * callback対象に登録する
	 * @param method
	 * @param logic
	 */
	public void addCallback(String method, CdpCallbackLogic logic);

	/**
	 * callback対象から削除する
	 * @param method
	 * @param logic
	 */
	public void deleteCallback(String method);
}
