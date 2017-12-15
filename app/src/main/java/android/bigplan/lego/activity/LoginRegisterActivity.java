package android.bigplan.lego.activity;

import android.app.Dialog;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.fragment.LoginFragment;
import android.bigplan.lego.fragment.RegisterFragment;
import android.bigplan.lego.util.DialogFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class LoginRegisterActivity extends BaseTActivity implements View.OnClickListener {

    private RadioButton mRbLogin, mRbRegister;

    private TextView tv_title;

    private FragmentType fragmentType;

    public static boolean isForeground = false;
    private static LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        AppApplication.getInstance().addActivity(this);

        registerMessageReceiver();

        initView();

        fragmentType = FragmentType.Login;

        startFragment();
    }

    private void initView() {
        mRbRegister = (RadioButton) findViewById(R.id.btn_register);
        mRbLogin = (RadioButton) findViewById(R.id.btn_login);
        mRbRegister.setOnClickListener(this);
        mRbLogin.setOnClickListener(this);
        loginFragment = new LoginFragment();
        FragmentType.Login.setFragment(loginFragment);

    }

    public static final String MESSAGE_RECEIVED_ACTION = "android.bigplan.lego.MESSAGE_ACTION";
    public static final String KEY_PWD_MODIFY = "passwod_modify";
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected String getToolBarTitle() {
        return getString(R.string.title_login);
    }

//    @Override
//    protected void back() {
//        onBackPressed();
//    }

    public class MessageReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_PWD_MODIFY);
                if (!TextUtils.isEmpty(messge)) {
                    LoginFragment fragment = (LoginFragment) FragmentType.Login.getFragment();
                    fragment.setUserInfo();
                }
            }
        }
    }

    public void onSwitchFragment(int id) {
        switch (id) {
            case R.id.btn_login:
                if (fragmentType != FragmentType.Login) {
                    Fragment from = fragmentType.getFragment();
                    LoginFragment to = (LoginFragment) FragmentType.Login.getFragment();
                    fragmentType = FragmentType.Login;
                    setToolBarTitle(getString(R.string.title_login));
                    switchFragment(from, to);
                    to.setUserInfo();
                }
                mRbLogin.setChecked(true);
                mRbRegister.setChecked(false);
                break;
            case R.id.btn_register:
                if (fragmentType != FragmentType.Register) {
                    Fragment from = fragmentType.getFragment();
                    Fragment to = FragmentType.Register.getFragment();
                    fragmentType = FragmentType.Register;
                    setToolBarTitle(getString(R.string.title_register));
                    switchFragment(from, to);
                }
                mRbLogin.setChecked(false);
                mRbRegister.setChecked(true);
                break;

            default:
                break;
        }
    }

    public void onClick(View v) {
        onSwitchFragment(v.getId());
    }

    private void startFragment() {
        Fragment fragment = fragmentType.getFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frame_layout, fragment).commit();
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            if (to.isAdded()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.hide(from);
                ft.show(to).commit();
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.hide(from);
                ft.add(R.id.frame_layout, to).commit();
            }
        }
    }

    public enum FragmentType {

        Login(0, "know", loginFragment),
        Register(1, "message", new RegisterFragment());

        private int index;
        private String tag;
        private Fragment fragment;

        private FragmentType(int index, String tag, Fragment fragment) {
            this.index = index;
            this.tag = tag;
            this.fragment = fragment;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

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
        mDialog = DialogFactory.creatRequestDialog(this,
                getString(R.string.loading));
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
        isForeground = false;
        unregisterReceiver(mMessageReceiver);
    }

    protected void onResume() {
        super.onResume();
        isForeground = true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
//        Fragment f = fragmentManager.findFragmentByTag(curFragmentTag);
        /*然后在碎片中调用重写的onActivityResult方法*/
        loginFragment.onActivityResult(requestCode, resultCode, data);
    }
}
