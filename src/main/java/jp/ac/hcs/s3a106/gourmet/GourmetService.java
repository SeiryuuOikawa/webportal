package jp.ac.hcs.s3a106.gourmet;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * グルメ情報を操作する 食べログAPIを活用する -http://webservice.recruit.co.jp/hotpepper/gourmet/v1
 */
@Transactional
@Service
public class GourmetService {

	@Autowired
	RestTemplate restTemplate;

	/** グルメサーチAPI リクエストURL */
	private static final String URL =
	"http://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key={API_KEY}&name={shopname}&large_service_area={large_service_area}&format=json";

	private static final String apiKey = "c16ff42075c5197d";
	
	/**
	 * グルメ検索取得業務ロジック
	 * @param shopname 検索ワード
	 * @param 地域コード
	 * @return ShopEntity
	 */
	public ShopEntity getShops(String shopname,String large_service_area) {
		// APIへアクセスして、結果を取得
		String json = restTemplate.getForObject(URL, String.class,apiKey,shopname,large_service_area);

		// エンティティクラスを生成
		ShopEntity shopEntity = new ShopEntity();
		shopEntity.setSerchWord(shopname);

		// jsonクラスへの変換失敗のため、例外処理
		try {
			// 変換クラスを生成し、文字列からjsonクラスへ変換する(例外の可能性あり)
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);

			// resultsパラメータの抽出(配列分取得する)
			for (JsonNode shop : node.get("results").get("shop")) {
				// データクラスの生成(result1件分)
				ShopData data = new ShopData();
				
				// dataLabelをDataクラスへ設定
				data.setId(shop.get("id").asText());
				data.setName(shop.get("name").asText());
				data.setLogo_image(shop.get("logo_image").asText());
				data.setName_kana(shop.get("name_kana").asText());
				data.setAddress(shop.get("address").asText());
				data.setAccess(shop.get("access").asText());
				data.setUrl(shop.get("urls").get("pc").asText());
				data.setImage(shop.get("photo").get("mobile").get("l").asText());
				
				// DataクラスをEnecityの配列に追加
				shopEntity.getForecasts().add(data);
			}
		} catch (IOException e) {
			// 例外発生時は、エラーメッセージの詳細を標準エラー出力
			e.printStackTrace();
		}
		return shopEntity;
	}
}
