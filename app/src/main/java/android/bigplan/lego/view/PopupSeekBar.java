package android.bigplan.lego.view;

import android.bigplan.lego.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by gg on 16-5-10.
 */
public class PopupSeekBar extends AppCompatSeekBar {

    private static final String TAG = PopupSeekBar.class.getSimpleName();
    private final Context mContext;
    //定义变量
    private PopupWindow pupWindow;
    private LayoutInflater layoutInflater;
    private View mView;
    //用来表示该组件在整个屏幕内的绝对坐标，其中 mPosition[0] 代表X坐标,mPosition[1] 代表Y坐标。
    private int[] mPosition;
    //SeekBar上的Thumb的宽度，即那个托动的小黄点的宽度
    private final int mThumbWidth = 50;
    private TextView mTvProgress;

    //构造函数，初始化操作
    public PopupSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }
    public PopupWindow  getPop(){
    return pupWindow;
}
    private void initView() {
        //获得父视图对象
        layoutInflater = LayoutInflater.from(mContext);
        //获得插入父节点的view对象
        mView = layoutInflater.inflate(R.layout.seekbar_popup, null);
        mTvProgress = (TextView) mView.findViewById(R.id.tvPop);
        pupWindow = new PopupWindow(mView, mView.getWidth(), mView.getHeight(), true);
        mPosition = new int[2];
    }

    public void setSeekBarText(String str) {
        mTvProgress.setText(str);
    }

    //重写触发按下监听事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                this.getLocationOnScreen(mPosition);
                Rect bounds = getThumb().getBounds();
                //控件显示位置
                pupWindow.showAsDropDown(this, bounds.left - (getViewWidth(mView) - bounds.right + bounds.left) / 2, -getViewHeight(mView) - bounds.bottom + bounds.top);
                break;
//            case MotionEvent.ACTION_MOVE:
//
//                this.getLocationOnScreen(mPosition);
//                Rect boundsMOVE = getThumb().getBounds();
//                //控件显示位置
//                pupWindow.showAsDropDown(this, boundsMOVE.left - (getViewWidth(mView) - boundsMOVE.right + boundsMOVE.left) / 2, -getViewHeight(mView) - boundsMOVE.bottom + boundsMOVE.top);
//
//                break;
            case MotionEvent.ACTION_UP:
//                pupWindow.dismiss();
                pupWindow.setFocusable(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    //获得控件的宽度
    private int getViewWidth(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    //获得控件的高度
    private int getViewHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    //重写draw方法
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");

        int thumb_x = this.getProgress() * (this.getWidth() - mThumbWidth) / this.getMax();
        //表示popupwindow在进度条所在y坐标减去popupwindow的高度，再减去他们直接的距离，我设置为5个dip；
        int middle = mPosition[1] - getViewHeight(mView);
        super.onDraw(canvas);
        if (pupWindow != null) {
            try {
                this.getLocationOnScreen(mPosition);
                pupWindow.update(thumb_x + mPosition[0] - getViewWidth(mView) / 2 + mThumbWidth / 2,
                        middle, getViewWidth(mView), getViewHeight(mView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void showPop(){
        Rect bounds = getThumb().getBounds();
        pupWindow.showAsDropDown(this, bounds.left - (getViewWidth(mView) - bounds.right + bounds.left) / 2, -getViewHeight(mView) - bounds.bottom + bounds.top);

    }
}
