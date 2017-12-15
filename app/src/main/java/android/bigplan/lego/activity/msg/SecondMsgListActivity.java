package android.bigplan.lego.activity.msg;

import android.app.AlertDialog.Builder;
import android.bigplan.lego.activity.BaseTActivity;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbObjResult;
import android.bigplan.lego.model.SecondMsg;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.adapter.SecondMsgAdapter;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.db.dao.PushMsgDao;
import android.bigplan.lego.db.storage.AbSqliteStorage;
import android.bigplan.lego.fragment.AbAlertDialogFragment;
import android.bigplan.lego.http.AbResult;
import android.bigplan.lego.util.AbDialogFragmentUtil;
import android.bigplan.lego.util.AbLogUtil;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.bigplan.lego.R;
import android.bigplan.lego.util.AbJsonUtil;
import android.app.AlertDialog;

public class SecondMsgListActivity extends BaseTActivity implements SwipeRefreshLayout.OnRefreshListener{
    private final static String TAG = SecondMsgListActivity.class.getSimpleName();
    private ListView listView;

    private List<SecondMsg> mListDatas;
    private SecondMsgAdapter mAdapter;

    public static boolean isForeground = false;

    // 定义数据库操作实现类
    private PushMsgDao pushMsgDao = null;

    // 数据库操作类
    private AbSqliteStorage mAbSqliteStorage = null;
    private  SecondMsg mDelModel = null;

    private SwipeRefreshLayout mSwipeRefresh = null;
    private String msgType = AbConstant.PUSHMSG_TYPE_SYS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.businessneeds_activity);
        AppApplication.getInstance().addActivity(this);

        // 定义数据库操作实现类
        pushMsgDao = new PushMsgDao(this);
        // 数据库操作类
        mAbSqliteStorage = AbSqliteStorage.getInstance(this);

        msgType = getIntent().getStringExtra(ReceptionRoomActivity.EXTRA_MESSAGE_TYPE);

        initView();

        GetNoticeTask();
    }
    protected String getToolBarTitle(){
        return getIntent().getStringExtra(ReceptionRoomActivity.EXTRA_MESSAGE_TITLE);
    }

    private void initView() {
        mListDatas = new ArrayList<SecondMsg>();
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);
        listView = (ListView)findViewById(R.id.listview);
        mAdapter = new SecondMsgAdapter(SecondMsgListActivity.this, msgType, mListDatas);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                SecondMsg model = mListDatas.get(arg2);
                //如果消息是未读消息，把数据库消息置为已读
                String trim = model.getIsRead().trim();
                String isRead = trim.substring(0, 1);
                if (isRead.equals("0")) {
                    ReadNoticeTask(model);
                }

                //把消息置为已读
                mListDatas.get(arg2).setIsRead("1");
                mAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                AlertDialog.Builder builder = new Builder(SecondMsgListActivity.this);
                builder.setTitle("提示");
                builder.setMessage("置顶或删除");
                builder.setPositiveButton(" 置顶 ", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        JSONObject object = new JSONObject();
                        final SecondMsg model = mListDatas.get(arg2);
                        try {
                            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
                            object.put("type", "" + msgType);
                            object.put("id", model.getId());
                        } catch (Exception ex) {
                            AbLogUtil.e(ex.getMessage());
                        }
                        post("/Message/TopNoticeSub", object);
                    }
                });
                builder.setNegativeButton(" 删除 ", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        del(arg2);
                    }
                });
                builder.show();
                return true;
            }
        });

    }
    void  del(int arg2){
        final SecondMsg model = mListDatas.get(arg2);

        AbDialogFragmentUtil.showAlertDialog(SecondMsgListActivity.this,
                R.drawable.ic_alert, "提示", "您确认要删除吗!",
                new AbAlertDialogFragment.AbDialogOnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        DelNoticeTask(model);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                }, true);
    }


    protected void onSuccessCallback(String url, String content, JSONObject param) {
        super.onSuccessCallback(url, content, param);
        AbResult abResult = (AbResult)AbJsonUtil.fromJson(content, AbResult.class);
        try {
            if (url.equals("/Message/GetNoticeSubList")) {
                String jsonData = AbJsonUtil.toJson(abResult.getData());
                if(mListDatas!=null){
                    mListDatas.clear();
                }
                mListDatas = (List<SecondMsg>)AbJsonUtil.fromJson(jsonData, new TypeToken<List<SecondMsg>>(){});
                if(mAdapter==null) {
                    mAdapter = new SecondMsgAdapter(SecondMsgListActivity.this, msgType, mListDatas);
                    listView.setAdapter(mAdapter);
                }else{
                    mAdapter.updateListView(mListDatas);
                }
            }else if (url.equals("/Message/DelNoticeSub")){
                mListDatas.remove(mDelModel);
                mAdapter.notifyDataSetChanged();
            }else if (url.equals("/Message/ReadNoticeSub")){
                pushMsgDao.startWritableDatabase(false);
                pushMsgDao.updateMessageCount("" + msgType, AppApplication.getInstance().getUser().getMemberId());
                pushMsgDao.closeDatabase();
            }else if (url.equals("/Message/TopNoticeSub")){
                GetNoticeTask();
            }

        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage());
        }
    }
    public void onRefresh(){
        GetNoticeTask();
        mSwipeRefresh.setRefreshing(false);
    }
    public void GetNoticeTask() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("type", "" + msgType);
        }catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Message/GetNoticeSubList", object);
    }

    public void DelNoticeTask(final SecondMsg model) {
        mDelModel = model;
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("type", "" + msgType);
            object.put("id", model.getId());
        }
        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Message/DelNoticeSub", object);
    }

    public void ReadNoticeTask(final SecondMsg model) {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
            object.put("type", "" + msgType);
            object.put("id", model.getId());
        }
        catch (Exception ex) {
            AbLogUtil.e(ex.getMessage());
        }
        post("/Message/ReadNoticeSub", object);
    }
//    public void ReadNoticeTask(final String  id) {
//        AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(this);
//        AbRequest request = new AbRequest();
//        JSONObject object = new JSONObject();
//        //        {"memberid":"1","type":"2","id":"1"}
//        try {
//            object.put("memberid", AppApplication.getInstance().getUser().getMemberId());
//            object.put("id", id);
//            object.put("type", "" + msgType);
////            if (msgType == 1) {
////                object.put("objid", model.getId());
////            }
////            else if (msgType == 2) {
////                object.put("objid", model.getFocusId());
////            }
////            else {
////                object.put("objid", model.getId());
////            }
//        }
//        catch (Exception ex) {
//            AbLogUtil.e(ex.getMessage());
//        }
//
//
//         post("/Message/ReadNoticeSub", object );
//    }
}
