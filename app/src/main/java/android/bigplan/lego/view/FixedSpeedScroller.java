package android.bigplan.lego.view;

import android.content.Context;
import android.os.Build;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 固定滚动速度
 *
 * @author FingerArt FingerArt.me
 * @date 2015年11月26日 上午10:00:07
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 500;

    public FixedSpeedScroller(Context context, Interpolator interpolator, int mDuration) {
        this(context, interpolator);
        this.mDuration = mDuration;
    }

    public FixedSpeedScroller(Context context) {
        this(context, null);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}