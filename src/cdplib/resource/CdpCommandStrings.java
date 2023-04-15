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
		public static final String enabled = "enabled";
		public static final String frameId = "frameId";
		public static final String mode = "mode";
		public static final String nodeId = "nodeId";
		public static final String backendNodeId = "backendNodeId";
		public static final String files = "files";
		public static final String objectId = "objectId";
		public static final String quads = "quads";
		public static final String x = "x";
		public static final String y = "y";
		public static final String modifiers = "modifiers";
		public static final String timestamp = "timestamp";
		public static final String button = "button";
		public static final String buttons = "buttons";
		public static final String clickCount = "clickCount";
		public static final String force = "force";
		public static final String tangentialPressure = "tangentialPressure";
		public static final String tiltX = "tiltX";
		public static final String tiltY = "tiltY";
		public static final String twist = "twist";
		public static final String deltaX = "deltaX";
		public static final String deltaY = "deltaY";
		public static final String pointerType = "pointerType";
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
		public static final String setInterceptFileChooserDialog = Com + "setInterceptFileChooserDialog";
	}

	public class PageEvents {
		static final String Com = PageMethods.Com;
		public static final String javascriptDialogOpening = Com + "javascriptDialogOpening";
		public static final String javascriptDialogClosed = Com + "javascriptDialogClosed";
		public static final String fileChooserOpened = Com + "fileChooserOpened";
	}

	public class DomMethods{
		static final String Com = "DOM.";
		public static final String getContentQuads = Com + "getContentQuads";
		public static final String setFileInputFiles = Com + "setFileInputFiles";
	}

	public class InputMethods{
		static final String Com = "Input.";
		public static final String dispatchMouseEvent = Com + "dispatchMouseEvent";

		public static final String dispatchMouseEvent_type_mouseMoved = "mouseMoved";
		public static final String dispatchMouseEvent_type_mousePressed = "mousePressed";
		public static final String dispatchMouseEvent_type_mouseReleased = "mouseReleased";

		public static final String dispatchMouseEvent_button_none = "none";
		public static final String dispatchMouseEvent_button_left = "left";
	}
}

