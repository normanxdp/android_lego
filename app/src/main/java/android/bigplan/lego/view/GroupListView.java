package android.bigplan.lego.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 能与ScrollView嵌套的ListView
 * @author FingerArt FingerArt.me
 * @date 2015年12月1日 下午12:13:01
 *
 */
public class GroupListView extends ListView {

	public GroupListView(Context context) {
		this(context, null);
	}

	public GroupListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GroupListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
