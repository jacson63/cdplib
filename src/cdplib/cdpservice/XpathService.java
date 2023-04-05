package cdplib.cdpservice;

public interface XpathService {
 	public String input(String selector, String value);
	public String select(String selector, String value);
	public String click(String selector);
    public boolean isXPath(String xpath);
}
