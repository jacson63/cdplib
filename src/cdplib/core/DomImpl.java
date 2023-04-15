package cdplib.core;

import cdplib.lib.CdpJsonCreator;
import cdplib.resource.CdpCommandStrings.DomMethods;
import cdplib.resource.CdpCommandStrings.FieldName;

public class DomImpl implements Dom{

	public String setFileInputFiles(int id, String filePath, Integer nodeId, Integer backendNodeId, String objectId) {
		CdpJsonCreator creator = new CdpJsonCreator(id, DomMethods.setFileInputFiles);

		creator.addParam(FieldName.files, creator.createArrayNode().add(filePath));

		creator.addOptionParam(FieldName.nodeId, nodeId);
		creator.addOptionParam(FieldName.backendNodeId, backendNodeId);
		creator.addOptionParam(FieldName.objectId, objectId);
		return creator.getJson();
	}

	@Override
	public String getContentQuads(int id, Integer nodeId, Integer backendNodeId, String objectId) {
		CdpJsonCreator creator = new CdpJsonCreator(id, DomMethods.getContentQuads);

		creator.addOptionParam(FieldName.nodeId, nodeId);
		creator.addOptionParam(FieldName.backendNodeId, backendNodeId);
		creator.addOptionParam(FieldName.objectId, objectId);
		return creator.getJson();
	}

}
