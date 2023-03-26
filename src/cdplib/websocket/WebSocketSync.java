package cdplib.websocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import cdplib.cdpdata.EventResource;
import debug.CLogger;

public class WebSocketSync {
	private WebSocket ws;
	private String responseBuf;
	private EventResource eventResource = EventResource.getInstance();
	private boolean execFlg = false;
	private static int WAIT_TIME = 500;
	private static int WAIT_COUNT = 10;
	private static int RECEIVABLE_QUANTITY = 100; //１リクエストのレスポンス受信可能回数

	public WebSocketSync(String url) {
		try {
			ws = connect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private WebSocket connect(String url) throws InterruptedException, ExecutionException {
		HttpClient client = HttpClient.newHttpClient();
		WebSocket.Builder wsb = client.newWebSocketBuilder();

		//WebSocketイベント
		WebSocket.Listener listener = new WebSocket.Listener() {
			private List<CharSequence> parts = new ArrayList<>();

			@Override
			public void onOpen(WebSocket webSocket){
				webSocket.request(RECEIVABLE_QUANTITY);
			}
			@Override
			public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
				CLogger.finest("ws recv(" + last + "):" + data.toString());
				if(last) {
					parts.add(data);
					responseBuf = String.join("", parts);

					parts = new ArrayList<CharSequence>();
					execFlg = false;
					webSocket.request(RECEIVABLE_QUANTITY);

					//callbackデータ登録処理
					eventResource.setData(responseBuf);
				} else {
					parts.add(data);
				}

	            return null;
			}
		};

		//接続開始
		CompletableFuture<WebSocket> comp = wsb.buildAsync(URI.create(url), listener);

		//接続完了
		return comp.get();
	}

	/**
	 * 送信
	 * @param sendStr
	 * @return レスポンス文字列
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public String sendSync(String sendStr) throws TimeoutException, InterruptedException {
		return this.sendSync(sendStr, WAIT_TIME, WAIT_COUNT, true);
	}

	/**
	 * 送信
	 * @param sendStr
	 * @param waitCount
	 * @param waitTime
	 * @param responseFlg
	 * @return レスポンス文字列
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public String sendSync(String sendStr, int waitCount, int waitTime, boolean responseFlg) throws TimeoutException, InterruptedException {
		CLogger.finer("sendSync send:" + sendStr);
		ws.sendText(sendStr, true);

		if(!responseFlg) {
			//レスポンス待ちしない場合はここで終了
			return "";
		}

		execFlg = true;
			//受信完了待ち
		int cnt = 0;
		for(; cnt < waitCount; cnt++) {
			if ( execFlg ) {
				Thread.sleep(waitTime);
			} else {
				break;
			}
		}

		if (cnt == waitCount) {
			throw new TimeoutException();
		}

		CLogger.finer("sendSync res:" + this.responseBuf);
		return this.responseBuf;
	}

	public void disconnect() {
		CLogger.finer("disconnect:" + ws.toString());
		ws.sendClose(WebSocket.NORMAL_CLOSURE, "normal disconnect");
	}
}
