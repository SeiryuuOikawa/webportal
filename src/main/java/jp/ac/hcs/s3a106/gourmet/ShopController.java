package jp.ac.hcs.s3a106.gourmet;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ShopController {
	@Autowired
	private GourmetService gourmetService;
	
	/**
	 * 北海道のグルメ検索情報を表示する
	 * @param shopname 検索ワード
	 * @param model
	 * @return 結果画面 - グルメ情報
	 */
	@PostMapping("/gourmet")
	public String getGourmetList(@RequestParam("shopname") String shopname,
			Principal principal, Model model){
		
		String large_service_area = "SS40";
		ShopEntity shopEntity = gourmetService.getShops(shopname,large_service_area);
		model.addAttribute("shopEntity", shopEntity);
		
		log.info("[" + principal.getName() + "]グルメ検索:" + "表示");
		return "gourmet/gourmet";
		
	}
}
