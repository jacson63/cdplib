package cdplib.core;

/**
 * Emulationのjson文字列を返すクラス
 * @author jacson32
 *
 */
public interface Emulation {
	public String setVisibleSize(int id, int width, int height) ;
	public String setDeviceMetricsOveride(int id, int width, int height) ;
	public String clearDeviceMetricsOverride(int id);
}
