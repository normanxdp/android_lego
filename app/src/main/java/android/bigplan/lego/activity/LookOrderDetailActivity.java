package android.bigplan.lego.activity;



import android.bigplan.lego.R;
import android.bigplan.lego.activity.msg.ChatActivity;
import android.bigplan.lego.activity.voice.MusicInterface;
import android.bigplan.lego.activity.voice.MusicService;
import android.bigplan.lego.adapter.LookOrderGridviewAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.model.OrderDetail;
import android.bigplan.lego.task.thread.AbThreadFactory;
import android.bigplan.lego.util.AbFileUtil;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.view.GroupGridView;
import android.bigplan.lego.view.GroupListView;
import android.bigplan.lego.view.PopupSeekBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;


public class LookOrderDetailActivity extends BaseTActivity implements View.OnClickListener , MusicService.CallBackVoiceListener{
    private final static String TAG = LookOrderDetailActivity.class.getSimpleName();
    private LinearLayout ll_baomufuwu_look;
    private TextView tv_timebaomu_lookorder;
    private TextView tv_type_name;
//    private String receivepath;
    private LinearLayout ll_vocie_look;
    private ImageView iv_vocie_look;
    private ProgressBar pb_lookoder;
    private String downLoadFile;
    private MyServiceConnection myServiceConnection;
    private Intent intentService;
    private PopupSeekBar pb_voice_lookt;
    private int progressbar;
    private int HANDLERVOICE = 100;
    private int STARTSERVER = 200;
    private TextView tv_timevicece_look;


    public enum CourseType {
        小学语文("0"), 小学数学("1"),  小学英语("2"),初中语文("10"),
        初中数学("11"), 初中英语("12"),  初中政治("13"),初中历史("14"),
        初中物理("15"), 初中化学("16"),  高中语文("20"),高中数学("21"),
        高中英语("22"), 高中政治("23"),  高中历史("24"),高中物理("25"),
        高中化学("26") ;
        private String uri;
        CourseType(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }
        public static CourseType getExamType(String value) {
            for (CourseType examType : CourseType.values()) {
                if (value .equals( examType.getUri())) {
                    return examType;
                }
            }
            return null;
        }
    }
    public enum CookType {
        大杂烩("0"), 湘菜("1"),  川菜("2"),粤菜("3"),
        浙菜("4"), 鲁菜("5"),  闽菜("6"),苏菜("7"),
        徽菜("8") ;
        private String uri;
        CookType(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }
        public static CookType getExamType(String value) {
            for (CookType examType : CookType.values()) {
                if (value .equals( examType.getUri())) {
                    return examType;
                }
            }
            return null;
        }
    }
      Handler handlervoice = new Handler() {
          public void handleMessage(android.os.Message msg) {

//              switch (msg.what){
//                  case HANDLERVOICE:
//
//                      break;
//                  case STARTSERVER:
//                      iv_vocie_look.setVisibility(View.VISIBLE);
//                      pb_lookoder.setVisibility(View.GONE);
//                      startServer();
//                      break;
//              }
              if(msg.what==STARTSERVER)
              {
                  iv_vocie_look.setVisibility(View.VISIBLE);
                  pb_lookoder.setVisibility(View.GONE);
                  startServer();
              }else if(msg.what==HANDLERVOICE){
                  tv_timevicece_look.setText(progressbar+"");
              }

          }
      };
    public final static String EXTRA_ORDER_ID = "extra.order_id";
    public final static String EXTRA_IS_STAFF = "extra.is_staff";
    public final static String CONTACTS = "contacts_lazy";

    private int mOrderID;
    private int mStaff = 0;
    private OrderDetail mOrderDetail;
    private TextView tv_describe_lookorder;
    private TextView tv_jiajiao_lookorder;
    private TextView tv_kemu_lookorder;
    private TextView tv_time_lookorder;
    private TextView tv_qian_lookorder;
    private TextView tv_name_lookorder;
    private TextView tv_phone_lookorder;
    private TextView tv_server_lookorder;
    private TextView tv_shouhuo_lookorder;
    private TextView tv_haoma_lookorder;
    private TextView tv_shouhuo_address_lookorder;
    private TextView btn_submi;
    private GroupGridView mGroupGridView;
    private GroupListView mGroupListView;
    private LinearLayout layout_jiajiao;
    private LinearLayout layout_help_song;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookorder_detail);

        initData();
        initView();
        MusicService.setCallBackVoiceListener(this);
    }

    protected String getToolBarTitle(){
        return getString(R.string.label_order_look);
    }

    private void initData() {
        Intent intent = getIntent();
        mOrderID = intent.getIntExtra(EXTRA_ORDER_ID, -1);
        mStaff = intent.getIntExtra(EXTRA_IS_STAFF, 0);
        if (mStaff != 1){
            mStaff = 0;
        }
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {

                File files = new File(AppApplication.receivepath);
                if (!files.exists()) {
                    boolean mkdir = files.mkdirs();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        ll_vocie_look = (LinearLayout) findViewById(R.id.ll_vocie_look);
        ll_baomufuwu_look = (LinearLayout) findViewById(R.id.ll_baomufuwu_look);
        layout_jiajiao = (LinearLayout) findViewById(R.id.layout_jiajiao);
        layout_help_song = (LinearLayout) findViewById(R.id.layout_help_song);
        tv_type_name = (TextView) findViewById(R.id.tv_type_name);//看看是不是保姆服务或者是菜的品种
        tv_timebaomu_lookorder = (TextView) findViewById(R.id.tv_timebaomu_lookorder);
        tv_describe_lookorder = (TextView) findViewById(R.id.tv_describe_lookorder);
        tv_jiajiao_lookorder = (TextView) findViewById(R.id.tv_jiajiao_lookorder);
        tv_kemu_lookorder = (TextView) findViewById(R.id.tv_kemu_lookorder);
        tv_time_lookorder = (TextView) findViewById(R.id.tv_time_lookorder);
        tv_qian_lookorder = (TextView) findViewById(R.id.tv_qian_lookorder);
        tv_name_lookorder = (TextView) findViewById(R.id.tv_name_lookorder);
        tv_phone_lookorder = (TextView) findViewById(R.id.tv_phone_lookorder);
        tv_server_lookorder = (TextView) findViewById(R.id.tv_server_lookorder);
        tv_shouhuo_lookorder = (TextView) findViewById(R.id.tv_shouhuo_lookorder);
        tv_haoma_lookorder = (TextView) findViewById(R.id.tv_haoma_lookorder);
        tv_shouhuo_address_lookorder = (TextView) findViewById(R.id.tv_shouhuo_address_lookorder);
        btn_submi = (TextView) findViewById(R.id.btn_submi_lookorder);
        mGroupGridView = (GroupGridView) findViewById(R.id.gv_look);
        mGroupListView = (GroupListView) findViewById(R.id.lv_lookder);
        iv_vocie_look = (ImageView) findViewById(R.id.iv_vocie_look);//听声音按钮
        pb_lookoder = (ProgressBar) findViewById(R.id.pb_lookoder);//听声音ProgressBar
        pb_voice_lookt = (PopupSeekBar) findViewById(R.id.pb_voice_lookt);//听声音的进度条
        tv_timevicece_look = (TextView) findViewById(R.id.tv_timevicece_look);//听声音的秒数


        btn_submi.setOnClickListener(this);
        iv_vocie_look.setOnClickListener(this);

        getGrabOrderDetail();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submi_lookorder:
                grabOrder(mOrderDetail.getDemId() );
                break;
            case R.id.iv_vocie_look:
                mi.play();
                break;

        }
    }

    protected void onShowErrorMsg(String url, String msg) {
        if (url.equals("/Demand/GetDetailById")){


        }
    }

    protected void onSuccessCallback(String url, String content, JSONObject param){
        try {
            if (url.equals("/Demand/GetDetailById")){
                JSONObject orderDetailObj = new JSONObject(content).getJSONObject("data");
                mOrderDetail = (OrderDetail)AbJsonUtil.fromJson(orderDetailObj.toString(), OrderDetail.class);

                showData();
            }else if (url.equals("/Demand/GrabDemand")){

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showData() {


        tv_describe_lookorder.setText(mOrderDetail.getExplain());//描述
        tv_qian_lookorder.setText(mOrderDetail.getValuation());//懒人提供费用
        tv_name_lookorder.setText(mOrderDetail.getMyName());//懒人名称
        tv_phone_lookorder.setText(mOrderDetail.getMyMobile());//懒人手机号码
        tv_server_lookorder.setText(mOrderDetail.getMyAddr());//服务地址
        //有图片
        String graphic = mOrderDetail.getGraphic();
        if(null!=graphic&&graphic.toString().trim().length()>3){
            String[] split = graphic.split(",");
            ArrayList<String> strings = new ArrayList<>();
            for (String dd:split  ) {
                strings.add(dd);
            }

            LookOrderGridviewAdapter lookOrderGridviewAdapter = new LookOrderGridviewAdapter(mContext, strings);
            mGroupGridView.setAdapter(lookOrderGridviewAdapter);
            mGroupGridView.setVisibility(View.VISIBLE);
        }

        if(mOrderDetail.getType().equals("1")&&  mOrderDetail.getStyle().equals("1")){//帮我买

        }else  if(mOrderDetail.getType().equals("1")&&  mOrderDetail.getStyle().equals("2")){//帮我送
            layout_help_song.setVisibility(View.VISIBLE);
            tv_shouhuo_lookorder.setText(mOrderDetail.getName());//收货信息
            tv_haoma_lookorder.setText(mOrderDetail.getMobile());//收货手机号码
            tv_shouhuo_address_lookorder.setText(mOrderDetail.getEndAddr());//收货地址
        }else   if(mOrderDetail.getType().equals("2") ){//钟点工
            ll_baomufuwu_look.setVisibility(View.VISIBLE);
            tv_timebaomu_lookorder.setText(mOrderDetail.getHour()+"小时");
        }else   if(mOrderDetail.getType().equals("3") ){//超市


        }else   if(mOrderDetail.getType().equals("4") ){//洗衣店


        }else   if(mOrderDetail.getType().equals("5") ){//家政  style：必填，1保姆2月嫂3老人陪伴

            ll_baomufuwu_look.setVisibility(View.VISIBLE);
            tv_timebaomu_lookorder.setText(mOrderDetail.getHour()+"小时");

        }else   if(mOrderDetail.getType().equals("6") ){//厨师style：必填1:湘菜;2:川菜;3:粤菜;4:浙菜;5:鲁菜;6:闽菜;7:苏菜;8:徽菜
            tv_type_name.setText("厨师技能");
            CookType examType = CookType.getExamType(mOrderDetail.getStyle());
            String name = examType.name();
            tv_timebaomu_lookorder.setText(name);
            ll_baomufuwu_look.setVisibility(View.VISIBLE);
        }else   if(mOrderDetail.getType().equals("7") ){//当type=7时-家教
            CourseType examType = CourseType.getExamType(mOrderDetail.getStyle());
            String name = examType.name();

            layout_jiajiao.setVisibility(View.VISIBLE);
            tv_jiajiao_lookorder.setText("家教");//家教
            tv_kemu_lookorder.setText(name);//科目
            tv_time_lookorder.setText(mOrderDetail.getHour()+"小时");//家教时间
        }else   if(mOrderDetail.getType().equals("8") ){//求助
        //可以的


        }else   if(mOrderDetail.getType().equals("9") ){//自定义

            ll_vocie_look.setVisibility(View.VISIBLE);
            mExecutorService = AbThreadFactory.getExecutorService();
            mExecutorService.execute(new Runnable() {
                public void run() {
                    downLoadFile = AbFileUtil.downLoadFile(mOrderDetail.getExplain(),AppApplication. receivepath);
                    if(downLoadFile!=null&&!downLoadFile.toString().trim().equals("")){

                        handlervoice.sendEmptyMessage(STARTSERVER);
                    }
                }});
        }

    }

    private void startServer() {
        if(downLoadFile!=null) {
            intentService = new Intent(this, MusicService.class);
            intentService.putExtra("mediapath", downLoadFile);
            startService(intentService);
            myServiceConnection = new MyServiceConnection();
            bindService(intentService, myServiceConnection, BIND_AUTO_CREATE);
        }
    }
    private  MusicInterface mi;
    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mi = (MusicInterface) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

    }
    public static Executor mExecutorService = null;


    private void getGrabOrderDetail() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("demid", mOrderID);
            object.put("isstaff", mStaff);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/Demand/GetDetailById", object);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myServiceConnection!=null||intentService!=null){

            unbindService(myServiceConnection);
            stopService(intentService);
        }
    }
    @Override
    public void callBack(int duration, int currentPosition) {
        //把进度设置给进度条
        pb_voice_lookt.setMax(duration);
        pb_voice_lookt.setProgress(currentPosition);
        progressbar = (duration-currentPosition )/ 1000;
        //如果要显示时长，把currentPosition除以1000，得到秒数，再除以60，商为分，余数为秒，设置进TextView就行了

        handlervoice.sendEmptyMessage(HANDLERVOICE);
    }
    private void grabOrder(String  mOrderID) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            if (AppApplication.getInstance().isInitLocaltion()){
                object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
                object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
                object.put("citycode", "0755");
            }else{
                object.put("citycode", "4403");
                object.put("longitude", Double.toString(AppApplication.getInstance().getLongitude()));
                object.put("latitude", Double.toString(AppApplication.getInstance().getLatitude()));
            }
            object.put("demid", mOrderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/Demand/GrabDemand", object);
    }
}
