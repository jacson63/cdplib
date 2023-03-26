package cdplib.cdpdata.eventresponse;

import com.fasterxml.jackson.databind.JsonNode;

public interface EventResponse {
	public void parse(JsonNode json);
}
