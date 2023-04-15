package cdplib.core.types;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;

public class DomQuad implements Types  {
	public double x1;
	public double y1;
	public double x2;
	public double y2;
	public double x3;
	public double y3;
	public double x4;
	public double y4;

	public double centerX;
	public double centerY;

	/**
	 * @param json x1～y4までカンマ区切りの数値(左上から右回りの角座標)
	 * 				[[x1, y2, x2, y2, x3, y3, x4, y4]]
	 * @throws Exception
	 */
	@Override
	public void parse(JsonNode json) throws Exception {
		// カンマ区切りで分割する
 		List<String> lists =
				  StreamSupport.stream(json.get(0).spliterator(), false)
				    .map(
				      e -> {
				        return e.asText(); // Stringに変換
				    })
				    .collect(Collectors.toList());

		int pos = 0;
		this.x1 = Double.parseDouble(lists.get(pos++));
		this.y1 = Double.parseDouble(lists.get(pos++));
		this.x2 = Double.parseDouble(lists.get(pos++));
		this.y2 = Double.parseDouble(lists.get(pos++));
		this.x3 = Double.parseDouble(lists.get(pos++));
		this.y3 = Double.parseDouble(lists.get(pos++));
		this.x4 = Double.parseDouble(lists.get(pos++));
		this.y4 = Double.parseDouble(lists.get(pos++));

		// 中心座標を計算しておく
		this.centerX = (this.x2 - this.x1) / 2 + this.x1;
		this.centerY = (this.y3 - this.y1) / 2 + this.y1;
	}
}
