package android.bigplan.lego.activity;

import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;


public class EditNiChengActivity extends BaseTActivity implements OnClickListener {


	public static int BASEINFORMATION = 1100;
	public static int EDITNICHENG = 1110;
	public static int EDITSEX = 2110;
	public static int EDITDATE = 3110;
	public static int EDITQIANMING = 4110;

	public static String MOREN = "moren";

	private EditText et_name_bianjishouhuodizhi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {



		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycernter_name);
		initView();
	}

	@Override
	protected String getToolBarTitle() {
		return "修改昵称";
	}


	protected void initView() {


		ImageView iv_name_bianjishouhuodizhi = (ImageView) findViewById(R.id.iv_name_bianjishouhuodizhi);
		iv_name_bianjishouhuodizhi.setOnClickListener(this);


		tv_changdu_bianjishouhuodizhi = (TextView) findViewById(R.id.tv_changdu_bianjishouhuodizhi);
		Button btn_submi_lookorder = (Button) findViewById(R.id.btn_submi_lookorder);
		btn_submi_lookorder.setOnClickListener(this);


		et_name_bianjishouhuodizhi = (EditText) findViewById(R.id.et_name_bianjishouhuodizhi);
		et_name_bianjishouhuodizhi.addTextChangedListener(textWatcher);

			et_name_bianjishouhuodizhi.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26)});
			tv_changdu_bianjishouhuodizhi.setVisibility(View.INVISIBLE);
		}




	/**
	 * 实时监听用户输入的手机号，输入至最后一位后，计算折扣后的金额
	 */
	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			tv_changdu_bianjishouhuodizhi.setText((60 - s.length()) + "");
		}
	};

	private TextView tv_changdu_bianjishouhuodizhi;


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submi_lookorder:
			String name = et_name_bianjishouhuodizhi.getText().toString();
			if (!TextUtils.isEmpty(name)){
				setUserName(name);
			}else{
				AbToastUtil.showToast(mContext, R.string.error_input_name_empty);
			}

			break;
		case R.id.iv_name_bianjishouhuodizhi:
			et_name_bianjishouhuodizhi.setText("");
			break;
		default:
			break;
		}

	}


	public void setUserName(String   name) {
		JSONObject object = new JSONObject();
		try {
			object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
			object.put("name", name);
		} catch (Exception ex) {

		}

		String isLazyStaff = AppApplication.getInstance().getUser().getIsLazyStaff();
		if (isLazyStaff.equals("0")){
			post("/Member/ModifyNickname", object);
		}else{
			post("/Member/ModifyRealName", object);
		}
	}

	protected void onSuccessCallback(String url, String content, JSONObject param) {
		try {
			if (url.equals("/Member/ModifyNickname") || url.equals("/Member/ModifyRealName")) {
				AbObjResult abResult = (AbObjResult) AbJsonUtil.fromJson(content, AbObjResult.class);
				if (abResult.getCode() == AbResult.RESULT_OK) {
					AbToastUtil.showToast(mContext, "修改用户名成功");
					String isLazyStaff = AppApplication.getInstance().getUser().getIsLazyStaff();
					if (url.equals("/Member/ModifyNickname")){
						AppApplication.getInstance().getUser().setNickname(param.getString("name"));
						AppApplication.getInstance().getUser().setRealName(param.getString("name"));
					}else if (isLazyStaff.equals("0")){
						AppApplication.getInstance().getUser().setRealName(param.getString("name"));
						AppApplication.getInstance().getUser().setNickname(param.getString("name"));
					}else{
						AppApplication.getInstance().getUser().setStoreName(param.getString("name"));
						AppApplication.getInstance().getUser().setRealName(param.getString("name"));
					}
					Intent intent = new Intent(mContext, MyDetailActivity.class);
					intent.putExtra(MyDetailActivity.USER_NAME, param.getString("name"));
					startActivity(intent);
					finish();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
