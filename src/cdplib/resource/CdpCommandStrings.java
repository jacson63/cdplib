package cdplib.resource;

public class CdpCommandStrings {
	public class strCom {
		public static final String height = "height";
		public static final String width = "width";
		public static final String deviceScaleFactor = "deviceScaleFactor";
		public static final String mobile = "mobile";
		public static final String result = "result";
		public static final String data = "data";
		public static final String cssContentSize = "cssContentSize";

		public static final String method = "method";
		public static final String params = "params";
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
		public static final String getLayoutMetrics = Com + "getLayoutMetrics";
		public static final String handleJavaScriptDialog = Com + "handleJavaScriptDialog";
		public static final String handleJavaScriptDialog_accept ="accept";
		public static final String handleJavaScriptDialog_promptText = "promptText";
		public static final String javascriptDialogOpening = Com + "javascriptDialogOpening";
		public static final String javascriptDialogOpening_message = "message";
		public static final String javascriptDialogClosed = Com + "javascriptDialogClosed";
	}
}

