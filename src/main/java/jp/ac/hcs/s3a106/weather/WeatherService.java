package jp.ac.hcs.s3a106.weather;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 郵便番号情報を操作する
 * zipcloud社の郵便番号検索APIを活用する
 *- https://weather.tsukumijima.net/api/forecast?city={cityCode}
 */
@Transactional
@Service

public class WeatherService {
	
	@Autowired
	RestTemplate restTemplate;
	
	/**郵便番号検索API リクエストURL*/
	private static final String URL=
			"https://weather.tsukumijima.net/api/forecast?city={weather}";
	
	/**
	 * 指定した郵便番号に紐づく郵便番号情報を取得する
	 * @param weather 郵便番号(7桁、ハイフン無し)
	 * @return weatherEntity
	 */
	public WeatherEntity getWeather(String weather) {
		
		
		//APIへアクセスして、結果を取得
		String json=restTemplate.getForObject(URL, String.class,weather);
		System.out.println(json);
		
		//エンティティクラスを生成
		WeatherEntity weatherEntity = new WeatherEntity();
		
		//jsonクラスへの変換失敗のため、例外処理
		try {
			//変換クラスを生成し、文字列からjspnクラスへ変換する(例外の可能性あり)
			ObjectMapper mapper=new ObjectMapper();
			JsonNode node=mapper.readTree(json);
			
			//statusパラメータの抽出
			String title =node.get("title").asText();
			weatherEntity.setTitle(title);
			
			
			//メッセージに何か入力されたらエラー処理を行う。未定
			if (node.get("forecasts") == null) {
				WeatherData wetherData=new WeatherData();
				
				wetherData.setDateLabel("条件に該当する住所は見つかりませんでした。");
				
				weatherEntity.getForecasts().add(wetherData);
			}else {
				for(JsonNode forecast:node.get("forecasts")) {
					//データクラスの生成(forecasts1件分)description
					
					//forecastsパラメータの抽出(配列分取得する)
					WeatherData weatherData=new WeatherData();
					
					weatherData.setDateLabel(forecast.get("dateLabel").asText());
					weatherData.setTelop(forecast.get("telop").asText());
					
					//可変長配列の末尾に追加
					weatherEntity.getForecasts().add(weatherData);
					

				}
				

				//descriptionパラメータの抽出
				WeatherData weatherData=new WeatherData();
				
				  weatherData.setHeadlineText(node.get("description").get("headlineText").asText());
				  weatherData.setBodyText(node.get("description").get("bodyText").asText());
				  weatherData.setText(node.get("description").get("text").asText());
				
				//可変長配列の末尾に追加
				weatherEntity.getDescription().add(weatherData);
				
			}

		}catch(IOException e) {
			//例外発生時は、エラーメッセージの詳細を標準エラー出力
			e.printStackTrace();
			
		}
		return weatherEntity;
	}
}