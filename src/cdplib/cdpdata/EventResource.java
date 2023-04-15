package cdplib.cdpdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cdplib.resource.CdpCommandStrings.FieldName;
import cdplib.resource.CdpCommandStrings.PageEvents;
import debug.CLogger;

/**
 * Eventのデータを保持するクラス
 */
public class EventResource {
	static EventResource instance;
	ObjectMapper mapper = new ObjectMapper();
	Map<String, JsonNode> dataMap = new HashMap<String, JsonNode>();
	List<Event> eventList = new ArrayList<Event>();

	enum DataMethod {
		Create,
		Delete
	}

	class Event {
		String name;
		DataMethod method;
		String dataKey;

		public Event(String name, DataMethod method, String dataKey) {
			this.name = name;
			this.method = method;
			this.dataKey = dataKey;
		}
	}

	class CallBackedData {
		String method;
		String json;
	}

	private EventResource() {
		//処理対象イベント登録
		eventList.add(new Event(PageEvents.javascriptDialogOpening, DataMethod.Create, PageEvents.javascriptDialogOpening));
		eventList.add(new Event(PageEvents.javascriptDialogClosed, DataMethod.Delete, PageEvents.javascriptDialogOpening));
		eventList.add(new Event(PageEvents.fileChooserOpened, DataMethod.Create, PageEvents.fileChooserOpened));

		//データ領域用意
		for(Event e : eventList) {
			dataMap.put(e.dataKey, null);
		}
	}

	public static EventResource getInstance() {
		if (instance == null) {
			instance = new EventResource();
		}
		return instance;
	}

	public void setData(String json) {
		// json内にidに"method"がなければ対象外
		if(!json.contains("\"" + FieldName.method +  "\"")) {
			return;
		}

		// methodを取得
		JsonNode node = null;
		String method = "";
		try {
			node = mapper.readTree(json);
			method = node.get(FieldName.method).asText();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// callback対象に含まれていれば該当クラス呼び出し
		Event e = getEvent(method);
		if ( e != null) {
			CLogger.finer("callbacked " + e.name);

			if (DataMethod.Create.equals(e.method)) {
				CLogger.finer("data(create) " + e.name + ":" + json);
				this.dataMap.put(e.name, node);
			}

			if (DataMethod.Delete.equals(e.method)) {
				CLogger.finer("data(delete) " + e.name);
				this.dataMap.put(e.name, null);
			}
		}
	}

	private Event getEvent(String eventName) {
		for(Event e: eventList) {
			if(e.name.equals(eventName)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * callbackで取得したデータを取得する
	 * @param eventName
	 * @return
	 */
	public JsonNode getData(String eventName) {
		if (!dataMap.containsKey(eventName)) {
			return null;
		}

		JsonNode ret = dataMap.get(eventName);
		dataMap.put(eventName, null);
		return ret;
	}
}
