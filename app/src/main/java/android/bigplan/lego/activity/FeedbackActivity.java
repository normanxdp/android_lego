package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class FeedbackActivity extends BaseTActivity implements View.OnClickListener {
    private final static String TAG = FeedbackActivity.class.getSimpleName();
    private Button mSubmitBtn = null;
    private EditText mSubmitEdit = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    private void initView() {
        mSubmitBtn = (Button) this.findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(this);
        mSubmitEdit = (EditText) this.findViewById(R.id.edit_feedbak);
    }

    @Override
    protected String getToolBarTitle() {
        return getString(R.string.title_feedback);
    }
    protected void back() {
        onBackPressed();
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        AbToastUtil.showToast(mContext, R.string.feedback_success);
        onBackPressed();
    }

    private void submitFeedback() {
        String strText = mSubmitEdit.getText().toString();
        if (TextUtils.isEmpty(strText)) {
            AbToastUtil.showToast(mContext, R.string.feedback_empty);
            mSubmitEdit.setFocusable(true);
            mSubmitEdit.requestFocus();
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("explain", strText);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
        post("/Member/Feedback", object);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_submit:
                submitFeedback();
                break;
        }
    }
}
