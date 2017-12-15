package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.util.AbSharedUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 宣传页
 *
 * @author FingerArt FingerArt.me
 * @date 2016年1月4日 下午4:28:29
 */
public class SplashActivity extends BaseTActivity implements OnPageChangeListener {

    private ViewPager mVpSplash;
    private ArrayList<View> views = new ArrayList<View>();
    private static int[] splash_imgs = {R.drawable.splash_one, R.drawable.splash_two, R.drawable.splash_three};
//    private static int[] splash_imgs = {R.drawable.guione, R.drawable.guitwo, R.drawable.guitree, R.drawable.guifour};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {
        mVpSplash = (ViewPager) findViewById(R.id.vp_splash);

        for (int i = 0; i < splash_imgs.length; i++) {
            View view = View.inflate(this, R.layout.item_splash_view_pager, null);
            ((ImageView) view.findViewById(R.id.iv_image)).setBackgroundResource(splash_imgs[i]);
            views.add(view);
        }

        mVpSplash.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                if (view != null)
                    container.removeView(view);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = getItem(position);
                container.addView(view);
                return view;
            }

            public View getItem(int position) {
                return views.get(position);
            }

            @Override
            public int getCount() {
                return views == null ? 0 : views.size();
            }
        });

        mVpSplash.setOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        //Empty
    }

    private int count = 0;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Logger.e("FingerArt", position + "--" + positionOffset + "--" + positionOffsetPixels);
        if (position == splash_imgs.length - 1) {
            count++;
            if (count == 4) {
                startApp();
            }
        } else {
            count = 0;
        }
    }

    private void startApp() {
        AbSharedUtil.putBoolean(this, "isFrist", false);
        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_exit_left);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Empty
    }

    @Override
    protected String getToolBarTitle() {
        return null;
    }
}











