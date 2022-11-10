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

import debug.CLogger;

public class WebSocketSync {
	private WebSocket ws;
	private String responceBuf;
	private boolean execFlg = false;
	private static int WAIT_TIME = 500;
	private static int WAIT_COUNT = 10;

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
				webSocket.request(10);
			}
			@Override
			public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
				if(last) {
					parts.add(data);
					responceBuf = String.join("", parts);
					execFlg = false;
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
		CLogger.finer("sendSync send:" + sendStr);
		ws.sendText(sendStr, true);

		execFlg = true;
			//受信完了待ち
		int cnt = 0;
		for(; cnt < WAIT_COUNT; cnt++) {
			if ( execFlg ) {
				Thread.sleep(WAIT_TIME);
			} else {
				break;
			}
		}

		if (cnt == WAIT_COUNT) {
			throw new TimeoutException();
		}

		CLogger.finer("sendSync res:" + this.responceBuf);
		return this.responceBuf;
	}

}
