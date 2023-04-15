package cdplib.core;

import java.util.List;

/**
 * Pageのjson文字列を返すクラス
 * @author jacson32
 *
 */
public interface Input {
//	public String dispatchMouseEvent(int id, String type, Double x, Double y, Integer modifiers
//			, Date timestamp, String button, Integer buttons, Integer clickCount, Integer force
//			, Integer tangentialPressure, Integer tiltX, Integer tiltY, Integer twist, Integer deltaX
//			, Integer deltaY, String pointerType);
	public String dispatchMouseEvent(int id, String type, Double x, Double y, Integer modifiers, String button, Integer clickCount);
	public List<String> dispatchMouseEvent_LClick(int id, Double x, Double y);
}
