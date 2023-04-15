package cdplib.core;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.FieldName;
import cdplib.resource.CdpCommandStrings.PageMethods;

public class PageImpl implements Page{

	@Override
	public String enable(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.enable);
		return creator.getJson();
	}

	@Override
	public String disable(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.disable);
		return creator.getJson();
	}

	@Override
	public String captureScreenshot(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.captureScreenshot);
		return creator.getJson();
	}

	@Override
	public String getLayoutMetrics(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.getLayoutMetrics);
		return creator.getJson();
	}

	@Override
	public String handleJavaScriptDialog(int id, boolean accept, String promptText) {
		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.handleJavaScriptDialog);
		creator.addParam(FieldName.accept, accept);
		creator.addParam(FieldName.promptText, promptText);
		return creator.getJson();
	}

	@Override
	public String onJavascriptDialogOpening() {
		return "";
	}

	@Override
	public String setInterceptFileChooserDialog(int id, boolean enabled) {
		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.setInterceptFileChooserDialog);
		creator.addParam(FieldName.enabled, enabled);
		return creator.getJson();
	}

	@Override
	public String onFileChooserOpened() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
