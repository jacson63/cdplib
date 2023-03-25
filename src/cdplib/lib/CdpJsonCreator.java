package cdplib.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CdpJsonCreator {
	int id;
	String method;
	ObjectMapper mapper = new ObjectMapper();
	ObjectNode root;
	ObjectNode param;

	public CdpJsonCreator (String method){
		this(1, method);
	}

	public CdpJsonCreator (int id, String method){
		this.id = id;
		this.method = method;

		initJackson();
	}

	private void initJackson() {
		root = mapper.createObjectNode();
		root.put("id", this.id);
		root.put("method", this.method);

		param = mapper.createObjectNode();
	}

	public String getJson() {
		if( param.size() > 0 ) {
			root.set("params", param);
		}

		try {
			return mapper.writeValueAsString(root);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void addParam(String paramName, String value) {
		param.put(paramName, value);
	}

	public void addParam(String paramName, int value) {
		param.put(paramName, value);
	}
	public void addParam(String paramName, boolean value ) {
		param.put(paramName, value);
	}
}