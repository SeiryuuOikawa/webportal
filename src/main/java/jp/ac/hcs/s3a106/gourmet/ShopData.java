package jp.ac.hcs.s3a106.gourmet;

import lombok.Data;

/**
 * 1件分の店舗検索情報
 */
@Data
public class ShopData {	
	
	/** お店ID */
	private String id ;
	
	/** 掲載店名 */
	private String name ;

	/** ロゴ画像 */
	private String logo_image;
	
	/** 掲載店名かな */
	private String name_kana;

	/** 住所 */
	private String address;

	/** 交通アクセス */
	private String access;
	
	/** 食べログ掲載URL */
	private String Url;
	
	/** 画像 */
	private String Image;
	
}
