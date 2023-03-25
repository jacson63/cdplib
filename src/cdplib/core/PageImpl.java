package cdplib.core;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.strPage;

public class PageImpl implements Page{

	@Override
	public String enable(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strPage.enable);
		return creator.getJson();
	}

	@Override
	public String disable(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strPage.disable);
		return creator.getJson();
	}

	@Override
	public String captureScreenshot(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strPage.captureScreenshot);
		return creator.getJson();
	}

	@Override
	public String getLayoutMetrics(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strPage.getLayoutMetrics);
		return creator.getJson();
	}

	@Override
	public String handleJavaScriptDialog(int id, boolean accept, String promptText) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strPage.handleJavaScriptDialog);
		creator.addParam(strPage.handleJavaScriptDialog_accept, accept);
		creator.addParam(strPage.handleJavaScriptDialog_promptText, promptText);
		return creator.getJson();
	}

	@Override
	public String onJavascriptDialogOpening() {
		return "";
	}
}
