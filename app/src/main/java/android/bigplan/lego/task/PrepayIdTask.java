/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.bigplan.lego.task;
/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.ProgressDialog;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.app.Constants;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.MD5;
import android.bigplan.lego.util.Util;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbAsyncTask.java 
 * 描述：下载数据的任务实现
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-9-2 下午12:52:13
 */
public  class PrepayIdTask extends AsyncTask<String, Void, Map<String, String>> {

	private ProgressDialog dialog = null;
	private Context mContext = null;
	Map<String, String> resultunifiedorder;

	StringBuffer sb ;
	PayReq req;
	public static  IWXAPI msgApi = null;

	public PrepayIdTask(Context context){
		mContext = context;
		msgApi =  WXAPIFactory.createWXAPI(mContext, null);
		req = new PayReq();
		sb = new StringBuffer();
		msgApi.registerApp(Constants.WXAPP_ID);
	}
	protected void onPreExecute() {
		dialog = ProgressDialog.show(mContext, "提示", "正在获取预支付订单");
	}

	@Override
	protected void onPostExecute(Map<String, String> result) {
		if (dialog != null) {
			dialog.dismiss();
		}
		sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

		resultunifiedorder = result;
		if (resultunifiedorder != null) {
			genPayReq();
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected Map<String, String> doInBackground(String... params) {

		String url = String
				.format("https://api.mch.weixin.qq.com/pay/unifiedorder");

		String entity = genProductArgs(params[0], params[1]);

		Log.e("orion", entity);

		byte[] buf = Util.httpPost(url, entity);
//            byte[] buf = {};

		String content = new String(buf);
		Log.e("orion", content);
		Map<String, String> xml = decodeXml(content);

		return xml;
	}


	public Map<String, String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if ("xml".equals(nodeName) == false) {
							// 实例化student对象
							xml.put(nodeName, parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	//
	private String genProductArgs(String mDemId, String staffId) {
		//得到long类型当前时间
		long nowtime = System.currentTimeMillis();
//new日期对象
		Date date = new Date(nowtime);
//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = dateFormat.format(date);


		StringBuffer xml = new StringBuffer();
		JSONObject object = new JSONObject();
		try {
			object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
			object.put("mobile", AppApplication.getInstance().getUser().getMobile());
			object.put("realName", AppApplication.getInstance().getUser().getRealName());
			object.put("demandid", mDemId);
			object.put("staffid", staffId);

		}catch (Exception e){

		}
		String nonceStr = genNonceStr();
		xml.append("</xml>");
		List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams .add(new BasicNameValuePair("appid", Constants.WXAPP_ID));
		packageParams.add(new BasicNameValuePair("attach",object.toString()));
		packageParams.add(new BasicNameValuePair("body", "懒人小二微信支付测试"));
//        packageParams.add(new BasicNameValuePair("body", productName));//产品的名字
		packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));//商户号
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));//随机数
		packageParams.add(new BasicNameValuePair("notify_url","http://120.76.100.208/pay/notify"));//回调后台网址
		packageParams.add(new BasicNameValuePair("out_trade_no",mDemId+format));//订单号
		packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));//固定值

//        int danjiaInt = 100;
//        float a =  price * danjiaInt;
//        String s=String.valueOf(a);
//        String[] split = s.split("\\.");
//        String money = split[0];
		packageParams.add(new BasicNameValuePair("total_fee", "1"));//支付的钱

		packageParams.add(new BasicNameValuePair("trade_type", "APP"));
		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));
		String xmlstring = toXml(packageParams);
		try {
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//这句加上就可以了吧xml转码下
		return null;

	}
	private void genPayReq() {

		req.appId = Constants.WXAPP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);
		sb.append("sign\n" + req.sign + "\n\n");
		AbToastUtil.showToast(mContext, "sign\n" + req.sign + "\n\n");
		sendPayReq();
	}

	private void sendPayReq() {

		msgApi.registerApp(Constants.WXAPP_ID);
		msgApi.sendReq(req);
	}
	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//			String packageSign = MD5Encode(sb.toString()).toUpperCase();
		Log.e("orion", packageSign);
		Log.i("123", "sb==" + sb.toString());
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");

		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//			String appSign = MD5Encode(sb.toString()).toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}
	/**
	 * MD5编码
	 * @param origin 原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			// resultString = byteArrayToHexString(md.digest(resultString.getBytes()));//原文件内容，可能原因是：win2003时系统缺省编码为GBK，win7为utf-8
			resultString = bytesToHexString(md.digest(resultString.getBytes("utf-8")));//正确的写法
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
}