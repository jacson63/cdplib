package cdplib.cdpservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cdplib.lib.CdpJsonCreator;

public interface WsSendService {
	public String send(String message);
	public String send(String message, int waitCount, int waitTime);
	public String sendJsonNode(ObjectMapper mapper, ObjectNode root) ;
	public String sendJsonNode(CdpJsonCreator creator) ;
	public String sendJavascript(String javascript) ;
}
