package jp.ac.hcs.s3a106.user;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * フロント-バックでユーザ情報をやり取りする
 * 各項目のデータ仕様はUserEntityを参照する
 *
 */
@Data
public class UserForm {
	/**ユーザＩＤ（メールアドレス）*/
	@NotBlank(message = "{require_check}")
	@Email(message = "{email_check}")
	private String user_id;
	
	/**パスワード*/
	@NotBlank(message = "{require_check}")
	@Length(min = 4, max = 100)
	private String password;
	
	/**ユーザ名*/
	@NotBlank(message = "{require_check}")
	private String user_name;
	
	/**ダークモードフラグ*/
	private boolean darkmode;
	
	/**権限*/
	@NotBlank(message = "{require_check}")
	private String role;
}