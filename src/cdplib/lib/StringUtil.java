package cdplib.lib;

public class StringUtil {
	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		}

		return value.isEmpty();
	}
}
