package debug;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CLogger2 {
	private static CLogger2 clogger = new CLogger2();
	private Logger logger;
	private FileHandler fHandler;

//	private CLogger() {
//		try {
//			logger = Logger.getLogger("CLogger");
//			logger.setLevel(Level.FINEST);
////			logger.setLevel(Level.FINE);
////			fHandler = new FileHandler(SettingData.LOG_FILE_PATH, false);
////			fHandler.setFormatter(new SimpleFormatter());
////			logger.addHandler(fHandler);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private static Logger getInstance() {
		return clogger.logger;
	}

	public static void finest(String msg) {
		getInstance().finest(msg);
	}

	public static void finer(String msg) {
		System.out.println(msg);
		getInstance().finer(msg);
	}

	public static void fine(String msg) {
		getInstance().fine(msg);
	}

	public static void config(String msg) {
		getInstance().config(msg);
	}

	public static void info(String msg) {
		getInstance().info(msg);
	}

	public static void warning(String msg) {
		getInstance().warning(msg);
	}

	public static void severe(String msg) {
		getInstance().severe(msg);
	}
}

