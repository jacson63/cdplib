package cdplib.core.types;

import com.fasterxml.jackson.databind.JsonNode;

public interface Types {
	public void parse(JsonNode json) throws Exception;
}
