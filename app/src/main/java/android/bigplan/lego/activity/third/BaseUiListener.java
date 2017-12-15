package android.bigplan.lego.activity.third;

import android.bigplan.lego.util.AbFileUtil;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * 调用SDK封装好的借口，需要传入回调的实例 会返回服务器的消息
 */
public class BaseUiListener implements IUiListener {
	/**
	 * 成功
	 */
	@Override
	public void onComplete(Object response) {
		doComplete((JSONObject) response);
	}

	/**
	 * 处理返回的消息 比如把json转换为对象什么的
	 *
	 * @param values
	 */
	protected void doComplete(JSONObject values) {
		AbFileUtil. writeByteArrayToSD("","doComplete",true);
	}

	@Override
	public void onError(UiError e) {
		AbFileUtil. writeByteArrayToSD("","onError",true);
	}

	@Override
	public void onCancel() {
		AbFileUtil. writeByteArrayToSD("","onCancel",true);
	}
}