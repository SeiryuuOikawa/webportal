package jp.ac.hcs.s3a106.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	/**
	 * ユーザ一覧を表示する
	 * @param model
	 * @return 結果画面 - タスク
	 */
	@GetMapping("/user/list")
	public String getUserList(Principal principal, Model model){
		 UserEntity userEntity = userService.selectAll();
		
		//* タスクリスト表示 */
		model.addAttribute("userEntity", userEntity);
		
		/** ロガー */
		log.info("[" + principal.getName() + "]ユーザ:" + "表示");
		
		return "user/userList";
	}
	
	/**
	 * ユーザ登録画面(管理者用)を表示する.
	 * @param form 登録時の入力チェック用UserForm
	 * @param model
	 * @return ユーザ登録画面(管理者用)
	 */
	@GetMapping("/user/insert")
	public String getUserInsert(UserForm from,Model model) {
		return "user/insert";
	}
	
	/**
	 * 1件分のユーザ情報をデータベースに登録する.
	 * @param form 登録するユーザ情報(パスワードは平文)
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model
	 * @return ユーザー一覧画面
	 */
	@PostMapping("/user/insert")
	public String getUserInsert(@ModelAttribute @Validated UserForm form,
			BindingResult bindingResult,
			Principal principal,
			Model model) {
		
		// 入力チェックに引っかかった場合、前の画面に戻る
		if(bindingResult.hasErrors()) {
			return getUserInsert(form, model);
		}
		
		//追加処理を実装する
		UserData data = userService.refillToData(form);
		boolean result = userService.insertOne(data);
		
		if(result) {
			model.addAttribute("message","ユーザを登録しました");
		}else {
			model.addAttribute("errorMessage","ユーザ登録に失敗しました。操作をやり直してください");
		}
		
		return getUserList(principal, model);
	}
	
	/**
	 * ユーザ詳細画面を表示する
	 * @param user_id 検索するユーザID
	 * @param princcipal ログイン画面
	 * @param model
	 * @return ユーザ詳細情報画面
	 */
	@GetMapping("/user/detail/{id}")
	public String getUserDetail(@PathVariable("id") String user_id,Principal principal,Model model){
		
		log.info("[" + principal.getName() + "]ユーザ:" + user_id);
		//1.ユーザIDの必須チェック(null値は除く)
		if(user_id.equals(null)) {
			return getUserList(principal, model);
		}
		
		//2.ユーザIDの妥当性チェック(メールアドレス)
		if(!(user_id.contains("@"))) {
			return getUserList(principal, model);
		}
		
		//3.ユーザ情報の取得(Service)
		UserData data = userService.selectOne(user_id);
		
		//4.ユーザ情報の存在チェック(無効なユーザIDで遷移しない)
		UserData myData = userService.selectOne(principal.getName());
		if( !(myData.getRole().equals("ROLE_ADMIN")) ) {
			return getUserList(principal, model);
		}
		
		/** ロガー */
		log.info("[" + principal.getName() + "]ユーザ:" + "表示");
	
		model.addAttribute("userData",data);
		return "user/detail";
	}
	
	
	/**
	 * ユーザ登録情報を変更する
	 * @param form 変更するユーザ情報(パスワードは平文)
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model
	 * @return ユーザー一覧画面
	 */
	@PostMapping("/user/update")
	public String getUserUpdate(UserFormForUpdate form,
			BindingResult bindingResult,
			Principal principal,
			Model model) {
		
		//パスワードが未入力の場合もとのパスワード格納
		if(form.getPassword().equals(null)) {
			UserData dataBefore = userService.selectOne(form.getUser_id());
			String pass = dataBefore.getPassword();
			form.setPassword(pass);
		}
		
		// 入力チェックに引っかかった場合、画面を遷移しない
		if(bindingResult.hasErrors() ) {
			return getUserDetail(form.getUser_id(), principal, model);
		}
		
		//変更処理を実装する
		UserData data = userService.refillToData(form);
		boolean result = userService.updateOne(data);
		if(result) {
			model.addAttribute("message","ユーザを情報を変更しました");
		}else {
			model.addAttribute("errorMessage","ユーザ登録に失敗しました。操作をやり直してください");
		}
		
		/** ロガー */
		log.info("[" + principal.getName() + "]ユーザ:" + "変更");
		
		return getUserList(principal, model);
	}
	
	/**
	 * ユーザ登録情報を削除する
	 * @param form 削除するユーザ情報(パスワードは平文)
	 * @param principal ログイン情報
	 * @param model
	 * @return ユーザー一覧画面
	 */
	@PostMapping("/user/delete")
	public String getUserDelete(@RequestParam String user_id,Principal principal,Model model) {
		//削除処理を実装する
		boolean result = userService.deleteOne(user_id);
		
		if(result) {
			model.addAttribute("message","ユーザを情報を削除しました");
		}else {
			model.addAttribute("errorMessage","ユーザ削除に失敗しました。操作をやり直してください");
		}

		/** ロガー */
		log.info("[" + principal.getName() + "]ユーザ:" + "削除" + user_id);
		
		return getUserList(principal, model);
	}
	
}
