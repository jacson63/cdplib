package cdplib.cdp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CdpInfo {
	private int port = 9222;
	private String INIT_URL_FORMAT = "http://localhost:%d/json";
	private URL url;

	private String title;
	private String webSocketDebuggerUrl;

	public CdpInfo() throws MalformedURLException {
		getData();
	}

	public CdpInfo(int port) {
		this.port = port;
		getData();
	}

	private void getData() {
		HttpURLConnection con;
		try {
			url = new URL(String.format(INIT_URL_FORMAT, this.port));
			con = (HttpURLConnection)url.openConnection();
			con.connect();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try(InputStreamReader in = new InputStreamReader(con.getInputStream())) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode json = objectMapper.readTree(in);

			Iterator<JsonNode> elem = json.elements();
			while(elem.hasNext()) {
				JsonNode tmp = elem.next();

				if ("page".equals(tmp.get("type").toString().replace("\"", ""))) {
					// 最初に取れたデータを保持
					this.title = tmp.get("title").toString().replace("\"", "");
					this.webSocketDebuggerUrl = tmp.get("webSocketDebuggerUrl").toString().replace("\"", "");

					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getWebSocketDebuggerUrl() {
		return webSocketDebuggerUrl;
	}

	public String getTitle() {
		return title;
	}
}
