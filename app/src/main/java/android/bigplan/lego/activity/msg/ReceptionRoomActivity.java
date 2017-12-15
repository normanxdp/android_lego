package android.bigplan.lego.activity.msg;
import android.bigplan.lego.activity.BaseTActivity;
import android.bigplan.lego.adapter.ReceptionRoomAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.db.dao.PushMsgDao;
import android.bigplan.lego.db.storage.AbSqliteStorage;
import android.bigplan.lego.db.storage.AbSqliteStorageListener;
import android.bigplan.lego.db.storage.AbStorageQuery;
import android.bigplan.lego.fragment.AbAlertDialogFragment;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.model.PushMsg;
import android.bigplan.lego.util.AbDialogFragmentUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.bigplan.lego.R;
import android.bigplan.lego.util.AbJsonUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.util.AbLogUtil;
public class ReceptionRoomActivity extends BaseTActivity implements SwipeRefreshLayout.OnRefreshListener{
    private final static String TAG = ReceptionRoomActivity.class.getSimpleName();
    public final static String EXTRA_MESSAGE_TYPE = "extra_message_type";
    public final static String EXTRA_MESSAGE_TITLE = "extra_message_title";
    private ListView listView;
    
    private List<PushMsg> mListDatas;
    private ReceptionRoomAdapter mAdapter;
    
    // 定义数据库操作实现类
    private PushMsgDao pushMsgDao = null;
    
    // 数据库操作类
    private AbSqliteStorage mAbSqliteStorage = null;
    
    public static boolean isForeground = false;
    
    private boolean isFisrt = true;
    private PushMsg mPushMsg = null;
    private SwipeRefreshLayout mSwipeRefresh = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reception_room_list);
        AppApplication.getInstance().addActivity(this);

        // 定义数据库操作实现类
        pushMsgDao = new PushMsgDao(this);
        // 数据库操作类
        mAbSqliteStorage = AbSqliteStorage.getInstance(this);

        initView();
        initData();
    }

    private void initView() {
        mListDatas = new ArrayList<PushMsg>();
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);

        listView = (ListView)findViewById(R.id.listview);
        mAdapter = new ReceptionRoomAdapter(ReceptionRoomActivity.this, mListDatas);
        listView.setAdapter(mAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                PushMsg pushMsg = mListDatas.get(arg2);
                String type = pushMsg.getType();
                switch (type){//)//1系统2钱包3状态
                    case AbConstant.PUSHMSG_TYPE_SYS://1
                    case AbConstant.PUSHMSG_TYPE_WALLET://2
                    case AbConstant.PUSHMSG_TYPE_STATE://3
                        Intent intent = new Intent(ReceptionRoomActivity.this, SecondMsgListActivity.class);
                        intent.putExtra(EXTRA_MESSAGE_TYPE, type);
                        intent.putExtra(EXTRA_MESSAGE_TITLE, pushMsg.getTitle());
                        startActivity(intent);
                        break;
                }
            }
        });
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final PushMsg pushMsg = mListDatas.get(arg2);
                AbDialogFragmentUtil.showAlertDialog(ReceptionRoomActivity.this,
                        R.drawable.ic_alert, "提示", "您确认要删除吗!",
                        new AbAlertDialogFragment.AbDialogOnClickListener() {
                            @Override
                            public void onPositiveClick() {
                                DelNoticeTask(pushMsg);
                            }

                            @Override
                            public void onNegativeClick() {

                            }
                        });
                return true;
            }
        });
        
    }

    protected  String getToolBarTitle(){
        return getString(R.string.label_message_title);
    }
    protected  String getToolBarRightTitle(){
        return getString(R.string.btn_message_all_read);
    }
    protected  void onRightTitleClick(){
        ReadNoticeAllTask();
    }
    public void onRefresh(){
        GetNoticeListTask();
        mSwipeRefresh.setRefreshing(false);
    }
    
    private void initData() {
        showRequestDialog();
        List<PushMsg> paramList = pushMsgDao.getPushMsgListByMsgType(AppApplication.getInstance().getUser().getMemberId());
        if (paramList != null && paramList.size() > 0) {
            mListDatas.addAll((Collection<? extends PushMsg>)paramList);
            mAdapter.notifyDataSetChanged();
            closeRequestDialog();
        }
        GetNoticeListTask();
        isFisrt = false;
    }


    protected void onSuccessCallback(String url, String content, JSONObject param) {
        super.onSuccessCallback(url, content, param);
        isFisrt = false;
        AbResult abResult = (AbResult)AbJsonUtil.fromJson(content, AbResult.class);
        try {
            if (url.equals("/Message/GetNoticeList")) {
                String jsonData = AbJsonUtil.toJson(abResult.getData());
                Logger.v("syso", "receptionroom===  " + jsonData);
                mListDatas.clear();
                mListDatas = (List<PushMsg>)AbJsonUtil.fromJson(jsonData, new TypeToken<List<PushMsg>>(){});
                mAdapter = new ReceptionRoomAdapter(ReceptionRoomActivity.this, mListDatas);
                listView.setAdapter(mAdapter);

                pushMsgDao.startWritableDatabase(false);
                pushMsgDao.deleteAll();
                pushMsgDao.closeDatabase();

                for (PushMsg item : mListDatas) {
                    item.setMemberId(AppApplication.getInstance().getUser().getMemberId());
                }

                mAbSqliteStorage.insertData(mListDatas, pushMsgDao, new AbSqliteStorageListener.AbDataInsertListListener(){
                    public void onSuccess(long[] rowIds) {
                        AbLogUtil.d("onSuccess:"+mListDatas.size());
                    }
                    public void onFailure(int errorCode, String errorMessage) {
                        AbToastUtil.showToast(ReceptionRoomActivity.this, errorMessage);
                    }
                });
            }else if (url.equals("/Message/DelNotice")){
                delPushMsgData(mPushMsg);
            }else if (url.equals("/Message/ReadNoticeAll")){
                List<PushMsg> parmaList = pushMsgDao.getPushMsgListByMsgType(AppApplication.getInstance().getUser().getMemberId());
                if(parmaList!=null&& parmaList.size()>0){
                    for (PushMsg pushMsg : parmaList) {
                        pushMsg.setAmount(0);
                        pushMsgDao.update(pushMsg);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }

    public void GetNoticeListTask() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        }
        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Message/GetNoticeList", object);
    }
    
    public void DelNoticeTask(final PushMsg pushMsg) {
        mPushMsg = pushMsg;
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("type", pushMsg.getType());
        } catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Message/DelNotice",object);
    }

    public void ReadNoticeAllTask() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
        }catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Message/ReadNoticeAll", object);
    }
    
    public void delPushMsgData(final PushMsg pushMsg) {
        AbStorageQuery mAbStorageQuery = new AbStorageQuery();
        mAbStorageQuery.equals("Id", String.valueOf(pushMsg.getId()));
        mAbStorageQuery.equals("Type", String.valueOf(pushMsg.getType()));
        
        // 无sql存储的删除
        mAbSqliteStorage.deleteData(mAbStorageQuery, pushMsgDao, new AbSqliteStorageListener.AbDataDeleteListener(){
            @Override
            public void onSuccess(int rows) {
                mListDatas.remove(pushMsg);
                mAdapter.notifyDataSetChanged();
            }
            
            @Override
            public void onFailure(int errorCode, String errorMessage) {
                AbToastUtil.showToast(ReceptionRoomActivity.this, errorMessage);
            }
        });
        
    }
    
    @Override
    public void finish() {
        // 必须要释放
        mAbSqliteStorage.release();
        super.finish();
    }
    
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        AbLogUtil.d("isFisrt:"+isFisrt);
        if (!isFisrt) {
            List<PushMsg> paramList = pushMsgDao.getPushMsgListByMsgType(AppApplication.getInstance().getUser().getMemberId());
            AbLogUtil.d("paramList size=:"+paramList.size());
            if (paramList != null && paramList.size() > 0) {
                mListDatas.clear();
                mListDatas.addAll((Collection<? extends PushMsg>)paramList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
    
    @Override
    protected void onStart() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(AbConstant.RECEPTIONROOM_NEW_MESSAGE);
        registerReceiver(mDataReceiver, mIntentFilter);
        
        super.onStart();
    }
    
    @Override
    protected void onStop() {
        unregisterReceiver(mDataReceiver);
        super.onStop();
    }
    
    // 创建BroadcastReceiver
    private BroadcastReceiver mDataReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AbConstant.RECEPTIONROOM_NEW_MESSAGE)) {
                
            }
        }
        
    };
}
