package android.bigplan.lego.db.dao;

import android.content.Context;
import android.bigplan.lego.db.DBInsideHelper;
import android.bigplan.lego.db.orm.dao.AbDBDaoImpl;
import android.bigplan.lego.model.User;
import android.bigplan.lego.util.AbLogUtil;

/**
 * 
 * 名称：UserDao.java
 * 
 * @version v1.0
 */
public class UserDao extends AbDBDaoImpl<User>{
    
    public UserDao(Context context) {
        super(new DBInsideHelper(context), User.class);
        
    }
    
    
    public void addUser(User user) {
		try {
			startReadableDatabase();
			insert(user);
		} catch (Exception e) {
			AbLogUtil.e(AbDBDaoImpl.class, "[addChatUser] from DB Exception.");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
	}
    
}
