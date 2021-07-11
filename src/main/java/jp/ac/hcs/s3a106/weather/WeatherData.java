package jp.ac.hcs.s3a106.weather;

import lombok.Data;

/**
 * 
 * 一件分の天気情報
 * 各項目のデータ仕様について、APIの仕様を参照する。
 * -https://weather.tsukumijima.net/
 */

@Data
public class WeatherData {
	
	/** 日付*/
	private String dateLabel;
	
	/** 日付*/
	private String description;
	
	
	/** 予報*/
	private String telop;
	
	
	//説明
	/**冒頭の詳細内容*/
	private String headlineText;
	
	/** メインの詳細内容*/
	private String bodyText;
	
	
	/**詳細内容*/
	private String text;

}
