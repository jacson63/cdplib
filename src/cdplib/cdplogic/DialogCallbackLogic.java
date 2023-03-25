package cdplib.cdplogic;

import com.fasterxml.jackson.databind.JsonNode;

import cdplib.resource.CdpCommandStrings.strCom;
import cdplib.resource.CdpCommandStrings.strPage;

public class DialogCallbackLogic implements CdpCallbackLogic{
	JsonNode node = null;
	boolean dialogOpenFlg = false;
	static DialogCallbackLogic instance;

	private DialogCallbackLogic() {
	}

	public static DialogCallbackLogic getInstance() {
		if (instance == null) {
			instance = new DialogCallbackLogic();
		}
		return instance;
	}

	@Override
	public String callback(String method, JsonNode node) {
		if (strPage.javascriptDialogOpening.equals(method)) {
			this.node = node;
			this.dialogOpenFlg = true;
		}

		if (strPage.javascriptDialogClosed.equals(method)) {
			this.node = null;
			this.dialogOpenFlg = true;
		}

		return "";
	}

	public String getMessage() {
		if (node == null) {
			return "";
		}

		return node.get(strCom.params).get(strPage.javascriptDialogOpening_message).asText();
	}
}
