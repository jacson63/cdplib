package cdplib.resource;

public class CdpCommandStrings {
	public class strCom {
		public static final String height = "height";
		public static final String width = "width";
		public static final String deviceScaleFactor = "deviceScaleFactor";
		public static final String mobile = "mobile";
		public static final String result = "result";
		public static final String data = "data";
	}

	public class strRuntime {
		static final String Com = "Runtime.";
		public static final String evaluate = Com + "evaluate";
		public static final String evaluate_expression = "expression";
	}
	public class strEnumlation {
		static final String Com = "Emulation.";
		public static final String setVisibleSize = Com + "setVisibleSize";
		public static final String setDeviceMetricsOverride = Com + "setDeviceMetricsOverride";
		public static final String clearDeviceMetricsOverride = Com + "clearDeviceMetricsOverride";
	}

	public class strPage{
		static final String Com = "Page.";
		public static final String enable = Com + "enable";
		public static final String disable = Com + "disable";
		public static final String captureScreenshot = Com + "captureScreenshot";

	}
}

