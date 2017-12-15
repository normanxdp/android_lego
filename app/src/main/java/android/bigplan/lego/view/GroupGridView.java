package android.bigplan.lego.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 无滑动属性的GridView
 * 可与ScrollView/ListView嵌套使用
 *
 * @author FingerArt FingerArt.me
 * @date 2015年12月16日 下午5:00:24
 */
public class GroupGridView extends GridView {

    private static final int DELTA = 10;

    public GroupGridView(Context context) {
        this(context, null);
    }

    public GroupGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    float downX;
    float downY;

    /*
     * 禁止GridView的滑动
     * @param ev
     * @return
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                if (Math.abs(ev.getX() - downX) > DELTA || Math.abs(ev.getY() - downY) > DELTA)
                    return true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
