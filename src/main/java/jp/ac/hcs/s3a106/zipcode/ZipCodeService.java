package jp.ac.hcs.s3a106.zipcode;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 郵便番号情報を操作する
 * zipcloud社の郵便番号検索APIを利用する
 * -https://zipcloud.ibsent.co.jp/doc/api
 */
@Transactional
@Service
public class ZipCodeService {

	@Autowired
	RestTemplate restTemplate;
	
	/** 郵便番号検索API リクエストURL */
	private static final String URL="https://zipcloud.ibsent.co.jp/api/search?zipcode={zipcode}";
	
	/**
	 * 指定した郵便番号に紐づく郵便番号情報を取得する
	 * @param zipcode 郵便番号（7桁、ハイフンなし）
	 * @return ZipCodeEntity
	 */
	public ZipCodeEntity getZip(String zipcode) {
		
		//APIへアクセスして、結果を取得
		String json = restTemplate.getForObject(URL, String.class, zipcode);
		
		//エンティティクラス生成
		ZipCodeEntity zipCodeEntity = new ZipCodeEntity();
		
		//jsonクラスの変更失敗のため、例外処理
		try {
			//変更クラスを生成し、文字列からJsonクラスへ変換する
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);
			
			//statusパラメータの抽出
			String status = node.get("status").asText();
			zipCodeEntity.setStatus(status);
			//messageパラメータの抽出
			String message = node.get("message").asText();
			zipCodeEntity.setMessage(message);
			
			//resultsパラメータの抽出
			for(JsonNode result : node.get("results")){
				//データクラスの生成
				ZipCodeData zipCodeData = new ZipCodeData();
				
				zipCodeData.setZipcode(result.get("zipcode").asText());
				zipCodeData.setPrefcode(result.get("prefcode").asText());
				zipCodeData.setAddress1(result.get("address1").asText());
				zipCodeData.setAddress2(result.get("address2").asText());
				zipCodeData.setAddress3(result.get("address3").asText());
				zipCodeData.setKana1(result.get("Kana1").asText());
				zipCodeData.setKana2(result.get("Kana2").asText());
				zipCodeData.setKana3(result.get("Kana3").asText());
				
				//可変長文字列の末尾に追加
				zipCodeEntity.getResults().add(zipCodeData);
			}
		} catch(IOException e) {
			//詳細を標準エラー出力
			e.printStackTrace();
		}
		return zipCodeEntity;
	}
}
