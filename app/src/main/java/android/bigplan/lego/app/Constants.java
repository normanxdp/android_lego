package android.bigplan.lego.app;

public class Constants {

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static String BASEINFORMATION="baseinformation";//跳转页面时携带数据的一个键
	public static String LOGOUTSTRING="logout";//跳转页面时携带数据的一个键

	public static String LOGIN_KEY ="sh-token-sign";
	public static String LOGIN_Value="";
	public static String OrderSource  = "2";//IOS = 1, Android = 2, WeiXin = 3,

	//这两个参数方便微信支付成功后掉起的参数
	public static boolean PayOrRecharge = false;//false是充值  true是支付
	public static String orderCode = "";
	public static String mPrepayId = "";
	// 商户PID
	public static  String PARTNER = "";
	// 商户收款账号
	public static  String SELLER = "";
	// 商户私钥，pkcs8格式
	public static  String RSA_PRIVATE = "";
	// 支付宝公钥
	public static  String RSA_PUBLIC = "";
	public static String dv="";
	public static String CartId="";
	public static String Figureurl="";


	 public static boolean nocity=false;
	
    //微信支付
	// 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxc875571394dffb17"/>为新设置的appid
		public static final String WXAPP_ID = "wx168c5f88591e35dc";

//		// 商户号
		public static final String MCH_ID = "1376852002";
		// API密钥，在商户平台设置
		public static final String API_KEY =  "dc0114c8382d47f880d1fea822ab35dp";
	
	  	public  static final String APPSECRET ="4f6f7f5c61fead432bcbdadc4939a269";

	//QQ
	public static final String QQAppid = "1106345163";
	public static final String QQAppKey = "zDyNIrpIWp8ciuD1";
}
