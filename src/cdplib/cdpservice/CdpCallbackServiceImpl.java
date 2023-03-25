package cdplib.cdpservice;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cdplib.cdplogic.CdpCallbackLogic;
import debug.CLogger;

public class CdpCallbackServiceImpl implements CdpCallbackService {
	static ObjectMapper mapper = new ObjectMapper();
	static Map<String, CdpCallbackLogic> map = new HashMap<String, CdpCallbackLogic>();

	@Override
	public void callback(String json) {
//		CLogger.finer("callback " + json);

		// json内にidに"method"がなければ対象外
		if(!json.contains("\"method\"")) {
			return;
		}

		// methodを取得
		JsonNode node = null;
		String method = "";
		try {
			node = mapper.readTree(json);
			method = node.get("method").asText();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}

		// callback対象に含まれていれば該当クラス呼び出し
		if (map.containsKey(method)) {
			CLogger.finer("callbacked " + method);
			map.get(method).callback(method, node);
		}
	}

	@Override
	public void addCallback(String method, CdpCallbackLogic logic) {
		map.put(method, logic);
	}

	@Override
	public void deleteCallback(String method) {
		map.remove(method);
	}
}
