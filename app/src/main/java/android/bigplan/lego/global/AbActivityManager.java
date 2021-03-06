package android.bigplan.lego.global;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

/**
 * 
 * 名称：AbActivityManager.java 
 * 描述：用于处理退出程序时可以退出所有的activity，而编写的通用类
 */
public class AbActivityManager {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static AbActivityManager instance;

	private AbActivityManager() {
	}

	/**
	 * 单例模式中获取唯一的AbActivityManager实例.
	 * @return
	 */
	public static AbActivityManager getInstance() {
		if (null == instance) {
			instance = new AbActivityManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中.
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 遍历所有Activity并finish.
	 */
	public void clearAllActivity() {
		for (Activity activity : activityList) {
			if(activity!=null){
				activity.finish();
			}
		}
	}
}