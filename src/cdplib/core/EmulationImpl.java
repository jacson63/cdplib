package cdplib.core;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.EnumlationMethods;
import cdplib.resource.CdpCommandStrings.FieldName;

public class EmulationImpl implements Emulation{
	@Override
	public String setVisibleSize(int id, int width, int height) {
		CdpJsonCreator creator = new CdpJsonCreator(id, EnumlationMethods.setVisibleSize);
		creator.addParam(FieldName.width, width);
		creator.addParam(FieldName.height, height);

		return creator.getJson();
	}

	@Override
	public String setDeviceMetricsOveride(int id, int width, int height) {
		CdpJsonCreator creator = new CdpJsonCreator(id, EnumlationMethods.setDeviceMetricsOverride);
		creator.addParam(FieldName.width, width);
		creator.addParam(FieldName.height, height);
		creator.addParam(FieldName.deviceScaleFactor, 1);
		creator.addParam(FieldName.mobile, false);

		return creator.getJson();
	}

	@Override
	public String clearDeviceMetricsOverride(int id) {
		CdpJsonCreator creator = new CdpJsonCreator(id, EnumlationMethods.clearDeviceMetricsOverride);

		return creator.getJson();
	}

}
