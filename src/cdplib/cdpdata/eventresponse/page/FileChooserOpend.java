package cdplib.cdpdata.eventresponse.page;

import com.fasterxml.jackson.databind.JsonNode;

import cdplib.cdpdata.eventresponse.EventResponse;
import cdplib.resource.CdpCommandStrings.FieldName;

public class FileChooserOpend implements EventResponse {
	String frameId;
	String mode;
	Integer backendNodeId;

	@Override
	public void parse(JsonNode json) {
		if (json == null) {
			return;
		}

		JsonNode params = json.get(FieldName.params);
		this.frameId = params.get(FieldName.frameId).asText();
		this.mode = params.get(FieldName.mode).asText();
		this.backendNodeId = params.get(FieldName.backendNodeId).asInt();
	}

	public String getFrameId() {
		return frameId;
	}

	public String getMode() {
		return mode;
	}

	public Integer getBackendNodeId() {
		return backendNodeId;
	}
}
