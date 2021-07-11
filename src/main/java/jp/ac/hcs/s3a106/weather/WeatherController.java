package jp.ac.hcs.s3a106.weather;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WeatherController{

	@Autowired
	private WeatherService weatherService;
	/**
	 * 
	 * @param weather 選択した地域（初期は札幌、後に全地域対応可能）
	 * @param principal ログイン情報
	 * @param model
	 * @return 検索結果-天気予報
	 */
	
	//都市コード改ざんチェック（正規表現）
	public static final String TAMPERING = ".*[0-9]{6}.*";
	
	@PostMapping("/weather")
	public String getWeather(@RequestParam("weather") String weather,
			Principal principal,Model model) {
		String returns = null;
		System.out.println(weather + "番号");
		if (weather == "") {
			System.out.println("1");
			log.info("choise miss");
			returns = "index";
			//改ざんされてるかどうか確認
		}else if(!(weather.matches(TAMPERING))) {
			System.out.println("2");
			log.info("Tampering!!");
			returns = "index";
		}else {
			System.out.println("3");
			WeatherEntity weatherEntity = weatherService.getWeather(weather);
			model.addAttribute("weatherEntity",weatherEntity);
			
			log.info("「" + principal.getName() + "」choise area to weather : " + 
			weatherEntity.getTitle());
			returns = "weather/weather";
		}
		return returns;
	}
}
