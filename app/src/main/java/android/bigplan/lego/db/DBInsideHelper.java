package android.bigplan.lego.db;

import android.bigplan.lego.db.dao.SearchTagDao;
import android.bigplan.lego.model.ProvinceCity;
import android.bigplan.lego.model.PushMsg;
import android.bigplan.lego.model.SearchTag;
import android.bigplan.lego.model.Skill;
import android.bigplan.lego.model.User;
import android.content.Context;
import android.bigplan.lego.db.orm.AbDBHelper;

/**
 * 
 * © 2012 amsoft.cn 名称：DBInsideHelper.java 描述：手机data/data下面的数据库
 * 
 * @author 还如一梦中
 * @date：2013-7-31 下午3:50:18
 * @version v1.0
 */
public class DBInsideHelper extends AbDBHelper {
	// 数据库名
	private static final String DBNAME = "/mnt/sdcard/lego.db";
	// 当前数据库的版本
	private static final int DBVERSION = 4;
	// 要初始化的表
	private static final Class<?>[] clazz = {
			User.class,
			ProvinceCity.class,
			Skill.class,
			PushMsg.class,
			SearchTag.class
	};

	public DBInsideHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}
}
