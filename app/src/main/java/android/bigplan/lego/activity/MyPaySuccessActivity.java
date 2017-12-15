package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;


public class MyPaySuccessActivity extends BaseTActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initView();

    }

    protected String getToolBarTitle(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        return  title;
    }

    private void initView() {
        String text = getIntent().getStringExtra("pay_success");
        TextView paySuccess = (TextView) findViewById(R.id.pay_result);
        paySuccess.setText(text);

        String money = getIntent().getStringExtra("lego_pay_money");
        String name = getIntent().getStringExtra("lego_pay_member_name");

        TextView payInfomation = (TextView)findViewById(R.id.pay_infomation);
        if (!TextUtils.isEmpty(name)){
            payInfomation.setText(getString(R.string.lable_pay_infomation) + name +  "支付￥" + money);
        }else{
            payInfomation.setText("您支付" + money);
        }
    }
}
