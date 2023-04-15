package cdplib.core;

import java.util.ArrayList;
import java.util.List;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.FieldName;
import cdplib.resource.CdpCommandStrings.InputMethods;

public class InputImpl implements Input{

//	@Override
//	public String dispatchMouseEvent(int id, String type, Double x, Double y, Integer modifiers, Date timestamp,
//			String button, Integer buttons, Integer clickCount, Integer force, Integer tangentialPressure,
//			Integer tiltX, Integer tiltY, Integer twist, Integer deltaX, Integer deltaY, String pointerType) {
//
//		CdpJsonCreator creator = new CdpJsonCreator(id, PageMethods.handleJavaScriptDialog);
//
//		creator.addParam(FieldName.type, type);
//		creator.addParam(FieldName.x, x);
//		creator.addParam(FieldName.y, y);
//
//		creator.addOptionParam(FieldName.modifiers, modifiers);
////			creator.addOptionParam(FieldName.timestamp, timestamp);
//		creator.addOptionParam(FieldName.button, button);
//		creator.addOptionParam(FieldName.buttons, buttons);
//		creator.addOptionParam(FieldName.clickCount, clickCount);
//		creator.addOptionParam(FieldName.force, force);
//		creator.addOptionParam(FieldName.tangentialPressure, tangentialPressure);
//		creator.addOptionParam(FieldName.tiltX, tiltX);
//		creator.addOptionParam(FieldName.tiltY, tiltY);
//		creator.addOptionParam(FieldName.twist, twist);
//		creator.addOptionParam(FieldName.deltaX, deltaX);
//		creator.addOptionParam(FieldName.deltaY, deltaY);
//		creator.addOptionParam(FieldName.pointerType, pointerType);
//
//		return creator.getJson();
//	}

	@Override
	public String dispatchMouseEvent(int id, String type, Double x, Double y, Integer modifiers, String button, Integer clickCount) {
		CdpJsonCreator creator = new CdpJsonCreator(id, InputMethods.dispatchMouseEvent);

		creator.addParam(FieldName.type, type);
		creator.addParam(FieldName.x, x);
		creator.addParam(FieldName.y, y);

		creator.addOptionParam(FieldName.modifiers, modifiers);
		creator.addOptionParam(FieldName.button, button);
		creator.addOptionParam(FieldName.clickCount, clickCount);
		return creator.getJson();
	}

	@Override
	public List<String> dispatchMouseEvent_LClick(int id, Double x, Double y) {
		List<String> ret = new ArrayList<String>();

		ret.add(this.dispatchMouseEvent(id, InputMethods.dispatchMouseEvent_type_mouseMoved, x, y, 0
				, InputMethods.dispatchMouseEvent_button_none, null));

		ret.add(this.dispatchMouseEvent(id, InputMethods.dispatchMouseEvent_type_mousePressed, x, y, 0
				, InputMethods.dispatchMouseEvent_button_left, 1));

		ret.add(this.dispatchMouseEvent(id, InputMethods.dispatchMouseEvent_type_mouseReleased, x, y, 0
				, InputMethods.dispatchMouseEvent_button_left, 1));

		return ret;
	}


}
