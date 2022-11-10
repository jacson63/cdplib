package debug;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CLogger {
	static CLogger logger;
	Level level = Level.ALL;
	static Map<Level, Integer> levels = new HashMap<>() {
		{
			put(Level.SEVERE, 1);
			put(Level.WARNING, 2);
			put(Level.INFO, 3);
			put(Level.CONFIG, 4);
			put(Level.FINE,  5);
			put(Level.FINER, 6);
			put(Level.FINEST, 7);
			put(Level.ALL, 100);
		}
	};

	private CLogger() {
		setLevel(Level.FINEST);
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	private static CLogger getInstance() {
		if (logger == null) logger = new CLogger();
		return logger;
	}

	public static void finest(String msg) {
		print(msg, Level.FINEST);
	}

	public static void finer(String msg) {
		print(msg, Level.FINER);
	}

	public static void fine(String msg) {
		print(msg, Level.FINE);
	}

	public static void config(String msg) {
		print(msg, Level.CONFIG);
	}

	public static void info(String msg) {
		print(msg, Level.INFO);
	}

	public static void warning(String msg) {
		print(msg, Level.WARNING);
	}

	public static void severe(String msg) {
		print(msg, Level.SEVERE);
	}

	private static void print(String msg, Level level)  {
		int currentVal = levels.get(getInstance().getLevel());
		int paramVal = levels.get(level);

		if (currentVal >= paramVal) {
			System.out.println(String.format("%s: %s", level, msg));
		}
	}
}
