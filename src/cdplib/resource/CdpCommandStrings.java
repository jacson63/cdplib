package cdplib.resource;

public class CdpCommandStrings {
	public class FieldName {
		public static final String method = "method";
		public static final String params = "params";

		public static final String height = "height";
		public static final String width = "width";
		public static final String deviceScaleFactor = "deviceScaleFactor";
		public static final String mobile = "mobile";
		public static final String result = "result";
		public static final String data = "data";
		public static final String cssContentSize = "cssContentSize";

		public static final String url = "url";
		public static final String message = "message";
		public static final String type = "type";
		public static final String hasBrowserHandler = "hasBrowserHandler";
		public static final String defaultPrompt = "defaultPrompt";
		public static final String accept ="accept";
		public static final String promptText = "promptText";
	}

	public class RuntimeMethods {
		static final String Com = "Runtime.";
		public static final String evaluate = Com + "evaluate";
		public static final String evaluate_expression = "expression";
	}
	public class EnumlationMethods {
		static final String Com = "Emulation.";
		public static final String setVisibleSize = Com + "setVisibleSize";
		public static final String setDeviceMetricsOverride = Com + "setDeviceMetricsOverride";
		public static final String clearDeviceMetricsOverride = Com + "clearDeviceMetricsOverride";
	}

	public class PageMethods{
		static final String Com = "Page.";
		public static final String enable = Com + "enable";
		public static final String disable = Com + "disable";
		public static final String captureScreenshot = Com + "captureScreenshot";
		public static final String getLayoutMetrics = Com + "getLayoutMetrics";
		public static final String handleJavaScriptDialog = Com + "handleJavaScriptDialog";
	}

	public class PageEvents {
		static final String Com = PageMethods.Com;
		public static final String javascriptDialogOpening = Com + "javascriptDialogOpening";
		public static final String javascriptDialogClosed = Com + "javascriptDialogClosed";
	}
}

