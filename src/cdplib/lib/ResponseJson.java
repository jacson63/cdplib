package cdplib.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cdplib.resource.CdpCommandStrings.strCom;

public class ResponseJson {
	static ObjectMapper mapper = new ObjectMapper();

	public static JsonNode parse(String json) throws JsonMappingException, JsonProcessingException {
		return mapper.readTree(json);
	}

	public static JsonNode getResult(String json) {
		try {
			return parse(json).get(strCom.result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static JsonNode getResultData(String json) {
		return getResult(json).get(strCom.data);
	}
}
