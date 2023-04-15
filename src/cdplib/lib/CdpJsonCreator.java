package cdplib.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

	public ArrayNode createArrayNode() {
		return mapper.createArrayNode();
	}

	public void addParam(String paramName, String value) {
		param.put(paramName, value);
	}
	public void addParam(String paramName, Integer value) {
		param.put(paramName, value);
	}
	public void addParam(String paramName, Double value) {
		param.put(paramName, value);
	}
	public void addParam(String paramName, Boolean value ) {
		param.put(paramName, value);
	}
	public void addParam(String paramName, ArrayNode value ) {
		param.set(paramName, value);
	}

	public void addOptionParam(String paramName, String value) {
		if (StringUtil.isEmpty(value)) {
			return;
		}
		this.addParam(paramName, value);
	}
	public void addOptionParam(String paramName, Integer value) {
		if (value == null) {
			return;
		}
		this.addParam(paramName, value);
	}
	public void addOptionParam(String paramName, Double value) {
		if (value == null) {
			return;
		}
		this.addParam(paramName, value);
	}
	public void addOptionParam(String paramName, Boolean value ) {
		if (value == null) {
			return;
		}
		this.addParam(paramName, value);
	}
}