package cdplib.cdpdata.eventresponse.page;

import com.fasterxml.jackson.databind.JsonNode;

import cdplib.cdpdata.eventresponse.EventResponse;
import cdplib.resource.CdpCommandStrings.FieldName;

public class JavascriptDialogOpening implements EventResponse {
	String url;
	String message;
	String type;
	boolean hasBrowserHandler = false;
	String defaultPrompt;

	@Override
	public void parse(JsonNode json) {
		if (json == null) {
			return;
		}

		JsonNode params = json.get(FieldName.params);
		this.url = params.get(FieldName.url).asText();
		this.message = params.get(FieldName.message).asText();
		this.type = params.get(FieldName.type).asText();
		this.hasBrowserHandler = params.get(FieldName.hasBrowserHandler).asBoolean();
		this.defaultPrompt = params.get(FieldName.defaultPrompt).asText();
	}

	public String getUrl() {
		return url;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public boolean isHasBrowserHandler() {
		return hasBrowserHandler;
	}

	public String getDefaultPrompt() {
		return defaultPrompt;
	}
}
