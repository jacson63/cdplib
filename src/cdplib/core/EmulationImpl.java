package cdplib.core;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.strCom;
import cdplib.resource.CdpCommandStrings.strEnumlation;

public class EmulationImpl implements Emulation{
	@Override
	public String setVisibleSize(int id, int width, int height) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strEnumlation.setVisibleSize);
		creator.addParam(strCom.width, width);
		creator.addParam(strCom.height, height);

		return creator.getJson();
	}

	@Override
	public String setDeviceMetricsOveride(int id, int width, int height) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strEnumlation.setDeviceMetricsOverride);
		creator.addParam(strCom.width, width);
		creator.addParam(strCom.height, height);
		creator.addParam(strCom.deviceScaleFactor, 1);
		creator.addParam(strCom.mobile, false);

		return creator.getJson();
	}

	@Override
	public String clearDeviceMetricsOverride(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, strEnumlation.clearDeviceMetricsOverride);

		return creator.getJson();
	}

}
