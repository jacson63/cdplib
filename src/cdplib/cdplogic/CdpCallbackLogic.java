package cdplib.cdplogic;

import com.fasterxml.jackson.databind.JsonNode;

public interface CdpCallbackLogic {
	public String callback(String method, JsonNode node);
}
