package jp.ac.hcs.s3a106.gourmet;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 天気予報検索の天気情報
 * 各項目のデータ使用については、APIの使用を参照する
 * https://weather.tsukumijima.net/
 */
@Data
public class ShopEntity {
	
	/** 検索条件 */
	private String serchWord;
	
	/** グルメ情報のリスト */
	private List<ShopData> forecasts = new ArrayList<ShopData>();
	
}

