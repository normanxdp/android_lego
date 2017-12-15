package android.bigplan.lego.activity;

import android.app.Application;
import android.app.Dialog;
import android.bigplan.lego.util.DialogFactory;
import android.bigplan.lego.view.AbBottomBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.bigplan.lego.R;
import android.bigplan.lego.global.AbActivityManager;
import android.bigplan.lego.view.AbTitleBar;

import cn.jpush.android.api.JPushInterface;

/**
 * 文 件 名: BaseActivity.java 描 述: 继承这个Activity使你的Activity拥有一个程序框架.
 * @author: chenzy
 * @date: 2015-5-28
 */
@Deprecated
public class BaseActivity extends FragmentActivity{
    //在Activity初始化的时候刷新获取网络数据
    protected static final int REFRESH_TYPE_INIT = 0;
    //在pullview的时候刷新获取网络数据
    protected static final int REFRESH_TYPE_PULL = 1;
    
    /** 全局的LayoutInflater对象，已经完成初始化. */
    public LayoutInflater mInflater;
    
    /** 全局的Application对象，已经完成初始化. */
    public Application abApplication = null;
    
    /** 总布局. */
    public RelativeLayout ab_base = null;
    
    /** 标题栏布局. */
    private AbTitleBar mAbTitleBar = null;
    
    /** 副标题栏布局. */
    private AbBottomBar mAbBottomBar = null;
    
    /** 主内容布局. */
    protected RelativeLayout contentLayout = null;
    
    protected static final int SOCKET_FAIL = -1;
    
    protected static final int SOCKET_SUCCESS = 0;

    /**当前登录手机信息*/
    protected Context mContext;

    /**
     * 描述：创建.
     * 
     * @param savedInstanceState the saved instance state
     * @see FragmentActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInflater = LayoutInflater.from(this);
        
        // 主标题栏
        mAbTitleBar = new AbTitleBar(this);
        
        // 最外层布局
        ab_base = new RelativeLayout(this);
        ab_base.setBackgroundColor(Color.rgb(255, 255, 255));
        
        // 内容布局
        contentLayout = new RelativeLayout(this);
        contentLayout.setPadding(0, 0, 0, 0);
        
        // 副标题栏
        mAbBottomBar = new AbBottomBar(this);
        
        // 填入View
        ab_base.addView(mAbTitleBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        mAbTitleBar.setVisibility(View.GONE);
        
        RelativeLayout.LayoutParams layoutParamsBottomBar = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsBottomBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        ab_base.addView(mAbBottomBar, layoutParamsBottomBar);
        
        RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsContent.addRule(RelativeLayout.BELOW, mAbTitleBar.getId());
        layoutParamsContent.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
        ab_base.addView(contentLayout, layoutParamsContent);
        
        // Application初始化
        abApplication = getApplication();
        mContext = this;
        
        // 设置ContentView
        setContentView(ab_base, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        AbActivityManager.getInstance().addActivity(this);

        
        //currentUser = AppApplication.getInstance().getUser();
        
        
    }
    
    /**
     * 描述：用指定的View填充主界面.
     * 
     * @param contentView 指定的View
     */
    public void setAbContentView(View contentView) {
        contentLayout.removeAllViews();
        contentLayout.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    
    /**
     * 描述：用指定资源ID表示的View填充主界面.
     * 
     * @param resId 指定的View的资源ID
     */
    public void setAbContentView(int resId) {
        setAbContentView(mInflater.inflate(resId, null));
    }
    
    /**
     * 获取主标题栏布局.
     * 
     * @return the title layout
     */
    public AbTitleBar getTitleBar() {
        mAbTitleBar.setVisibility(View.VISIBLE);
        return mAbTitleBar;
    }
    
    /**
     * 获取副标题栏布局.
     * 
     * @return the bottom layout
     */
    public AbBottomBar getBottomBar() {
        return mAbBottomBar;
    }
    
    /**
     * 设置主标题栏高度.
     * 
     * @param height LayoutParams属性 和具体的大小px
     */
    public void setTitleBarHeight(int height) {
        ViewGroup.LayoutParams params = mAbTitleBar.getLayoutParams();
        params.height = height;
        mAbTitleBar.setLayoutParams(params);
    }
    
    /**
     * 描述：设置绝对定位的主标题栏覆盖到内容的上边.
     * 
     * @param overlay the new title bar overlay
     */
    public void setTitleBarOverlay(boolean overlay) {
        ab_base.removeAllViews();
        if (overlay) {
            RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
            ab_base.addView(contentLayout, layoutParamsFW1);
            RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            ab_base.addView(mAbTitleBar, layoutParamsFW2);
            
            RelativeLayout.LayoutParams layoutParamsFW3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFW3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            ab_base.addView(mAbBottomBar, layoutParamsFW3);
            
        }
        else {
            ab_base.addView(mAbTitleBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            
            RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            ab_base.addView(mAbBottomBar, layoutParamsFW2);
            
            RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFW1.addRule(RelativeLayout.BELOW, mAbTitleBar.getId());
            layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
            ab_base.addView(contentLayout, layoutParamsFW1);
        }
    }
    
    /**
     * 描述：设置界面显示（忽略标题栏）.
     * @param layoutResID the new content view
     * @see android.app.Activity#setContentView(int)
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
    
    /**
     * 描述：设置界面显示（忽略标题栏）.
     * 
     * @param view the view
     * @param params the params
     * @see android.app.Activity#setContentView(View, ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View view,
        ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }
    
    /**
     * 描述：设置界面显示（忽略标题栏）.
     * @param view the new content view
     * @see android.app.Activity#setContentView(View)
     */
    public void setContentView(View view) {
        super.setContentView(view);
        
    }
    
    public Dialog mDialog = null;
    
    public void showRequestDialog(String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this, msg);
        mDialog.show();
    }
    
    public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(this, getString(R.string.loading));
        mDialog.show();
    }
    
    public void closeRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
