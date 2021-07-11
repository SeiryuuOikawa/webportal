package jp.ac.hcs.s3a106.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * user情報を操作する
 */
@Transactional
@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	/**
	 * 指定したユーザIDのタスク情報を全件取得する
	 * @return UserEntity
	 */
	public UserEntity selectAll() {
		UserEntity userEntity;
		try {
			userEntity = userRepository.selectAll();
		} catch (DataAccessException e) {
			// 例外発生時は、エラーメッセージの詳細を標準エラー出力
			e.printStackTrace();
			userEntity = null;
		}
		
		return userEntity;
	}
	
	/**
	 * ユーザ情報を一件追加する
	 * @param userData追加するユーザ情報(パスワードは平文)
	 * @return 処理結果(成功:true,失敗:false)
	 */
	public boolean insertOne(UserData userData) {
		int rowNumber;
		try {
			rowNumber = userRepository.insertOne(userData);
		}catch(DataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}
	
	/**
	 * 入力項目をUserDataへ変換する
	 * （このメソッドは入力チェックを実装したうえで呼び出すこと）
	 * @param form 入力データ
	 * @return UserData
	 */
	UserData refillToData(UserForm form) {
		UserData data = new UserData();
		data.setUser_id(form.getUser_id());
		data.setPassword(form.getPassword());
		data.setUser_name(form.getUser_name());
		data.setDarkmode(form.isDarkmode());
		data.setRole(form.getRole());
		//初期値は有効とする
		data.setEnabled(true);
		return data;
	}
	
	/**
	 * 入力項目をUserFDataUpdateへ変換する
	 * （このメソッドは入力チェックを実装したうえで呼び出すこと）
	 * @param form 入力データ
	 * @return UserData
	 */
	UserData refillToData(UserFormForUpdate form) {
		UserData data = new UserData();
		data.setUser_id(form.getUser_id());
		data.setPassword(form.getPassword());
		data.setUser_name(form.getUser_name());
		data.setDarkmode(form.isDarkmode());
		data.setRole(form.getRole());
		//初期値は有効とする
		data.setEnabled(true);
		return data;
	}

	/**
	 * ユーザ情報を１件取得する
	 * @param user_id
	 * @return data
	 */
	public UserData selectOne(String user_id) {
		UserData data;
		try {
			data = userRepository.selectOne(user_id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			data = null;
		}
		
		return data;
		
	}

	/**
	 * ユーザ情報を１件変更する
	 * @param userData変更するユーザ情報(パスワードは平文)
	 * @return 処理結果(成功:true,失敗:false)
	 */
	public boolean updateOne(UserData data) {
		int rowNumber;
		try {
			rowNumber = userRepository.updateOne(data);
		}catch(DataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}

	/**
	 * ユーザ情報を１件削除する
	 * @param userData削除するユーザ情報(パスワードは平文)
	 * @return 処理結果(成功:true,失敗:false)
	 */
	public boolean deleteOne(String id) {
		int rowNumber;
		try {
			rowNumber = userRepository.deleteOne(id);
		}catch(DataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}
	
}
