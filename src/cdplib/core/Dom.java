package cdplib.core;

/**
 * Pageのjson文字列を返すクラス
 * @author jacson32
 *
 */
public interface Dom {
	public String setFileInputFiles(int id, String filePath, Integer nodeId, Integer backendNodeId, String objectId);
	public String getContentQuads(int id, Integer nodeId, Integer backendNodeId, String objectId);
}
