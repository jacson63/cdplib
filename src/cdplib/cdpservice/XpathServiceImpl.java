package cdplib.cdpservice;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XpathServiceImpl implements XpathService{

	private WsSendService getWss() {
		try {
			return WsSendServiceFactory.getSerivceSingleton();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void init() {
		//getElementsByXpathを登録
		this.getWss().sendJavascript(
				"document.getElementsByXPath = function(expression, parentElement) {"
				+	"var r = [];"
				+	"var x = document.evaluate(expression, parentElement || document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);"
				+	"for (var i = 0, l = x.snapshotLength; i < l; i++) {"
				+		"r.push(x.snapshotItem(i));"
				+ 	"}"
				+	"return r;"
				+ "}"
			);
	}

	@Override
    public boolean isXPath(String str) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.compile(str);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

	@Override
	public String input(String selector, String value) {
		init();
		final String FORMAT  = "document.getElementsByXpath('%s')[0].value='%s'";
		return this.getWss().sendJavascript(String.format(FORMAT, selector, value));
	}

	@Override
	public String select(String selector, String value) {
		init();
		final String FORMAT  = "document.getElementsByXpath('%s')[0].value = %s";
		return this.getWss().sendJavascript(String.format(FORMAT, selector, value));
	}

	@Override
	public String click(String selector) {
		init();
		final String FORMAT  = "document.getElementsByXpat('%s')[0].click()";
		return this.getWss().sendJavascript(String.format(FORMAT, selector));
	}

}
