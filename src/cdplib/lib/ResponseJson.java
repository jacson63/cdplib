package cdplib.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cdplib.resource.CdpCommandStrings.FieldName;

public class ResponseJson {
	static ObjectMapper mapper = new ObjectMapper();

	public static JsonNode parse(String json) throws JsonMappingException, JsonProcessingException {
		return mapper.readTree(json);
	}

	public static JsonNode getResult(String json) {
		try {
			return parse(json).get(FieldName.result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static JsonNode getResultData(String json) {
		return getResult(json).get(FieldName.data);
	}

	public static JsonNode getResultCssContentSize(String json) {
		return getResult(json).get(FieldName.cssContentSize);
	}

	public static int getResultCssContentSizeWidth(String json) {
		return Integer.parseInt(getResultCssContentSize(json).get(FieldName.width).asText());
	}

	public static int getResultCssContentSizeHeight(String json) {
		return Integer.parseInt(getResultCssContentSize(json).get(FieldName.height).asText());
	}
}
