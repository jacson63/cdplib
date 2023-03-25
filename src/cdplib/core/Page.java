package cdplib.core;

/**
 * Pageのjson文字列を返すクラス
 * @author jacson32
 *
 */
public interface Page {
	public String enable(int id);
	public String disable(int id);
	public String captureScreenshot(int id);
}
