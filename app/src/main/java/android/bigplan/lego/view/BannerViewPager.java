package android.bigplan.lego.view;

import android.annotation.SuppressLint;
import android.bigplan.lego.util.Logger;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Field;

public class BannerViewPager extends ViewPager {

    private static final int PLAY_SPEED = 5000;
    protected static final String TAG = BannerViewPager.class.getSimpleName();
    private Handler mHandler = new Handler();
    private boolean isStop;
    private boolean scrollEnable = true;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setViewPagerScrollSpeed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
            case MotionEvent.ACTION_UP:
                play();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置滚动速度
     */
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new DecelerateInterpolator(), 800);
            mScroller.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        Logger.d(TAG, "play");
        isStop = false;
        mHandler.postDelayed(r, PLAY_SPEED);
    }

    public void stop() {
        Logger.d(TAG, "stop");
        isStop = true;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(this);
            if (isStop || !scrollEnable) {
                return;
            }
            int nextIndex = getAdapter() == null || getCurrentItem() + 1 == getAdapter().getCount() ? 0 : getCurrentItem() + 1;
            Logger.d(TAG, nextIndex + "滚动");
            setCurrentItem(nextIndex);
            mHandler.postDelayed(this, PLAY_SPEED);
        }
    };
}
