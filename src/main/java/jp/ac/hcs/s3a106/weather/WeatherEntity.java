package jp.ac.hcs.s3a106.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 *天気予報検索結果の天気予報
 *各項目のデータ仕様については、APIの仕様を参照する
 *-https://weather.tsukumijima.net/ 
 *
 */

@Data
public class WeatherEntity {
	
	/** タイトル*/
	private String title;
	
	/** 説明文のリスト*/
	private List<WeatherData> description = new ArrayList<WeatherData>();
	
	/** 天気予報のリスト*/
	private List<WeatherData> forecasts = new ArrayList<WeatherData>();

}
