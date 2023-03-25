package cdplib.cdpservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.strRuntime;
import cdplib.websocket.WebSocketSync;

public class WsSendServiceImpl implements WsSendService {
	WebSocketSync ws;

	public WsSendServiceImpl(WebSocketSync ws) {
		this.ws = ws;
	}

	@Override
	public String send(String message) {
		String res = "";
		try {
			res = ws.sendSync(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public String send(String message, int waitCount, int waitTime) {
		String res = "";
		try {
			res = ws.sendSync(message, waitCount, waitTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public String sendJsonNode(ObjectMapper mapper, ObjectNode root) {
		String json = "";
		try {
			json  = mapper.writeValueAsString(root);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return this.send(json);
	}

	@Override
	public String sendJsonNode(CdpJsonCreator creator) {
		return this.send(creator.getJson());
	}

	@Override
	public String sendJavascript(String javascript) {
		CdpJsonCreator creator = new CdpJsonCreator(1, strRuntime.evaluate);
		creator.addParam(strRuntime.evaluate_expression, javascript);
		return send(creator.getJson());
	}

}
