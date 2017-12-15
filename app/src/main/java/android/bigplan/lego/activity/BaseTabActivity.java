package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.interf.ExitAppInterf;
import android.bigplan.lego.util.AbAppUtil;
import android.bigplan.lego.util.AbToastUtil;

/**
 * TabActivity基类
 * Created by FingerArt.me on 2016/6/6.
 */
public abstract class BaseTabActivity extends BaseTActivity implements ExitAppInterf {

    @Override
    public void onBackPressed() {
        //双击退出程序
        AbAppUtil.exitForDoubleBack(this, 3000);
    }

    @Override
    public void again() {
        AbToastUtil.showToast(this, getString(R.string.tips_exit_for_double, getString(R.string.app_name)));
    }

    @Override
    public void exit() {
        AppApplication.getInstance().exit();
    }
}
