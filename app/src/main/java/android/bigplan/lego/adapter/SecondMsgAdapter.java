package android.bigplan.lego.adapter;

import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.model.PushMsg;
import android.bigplan.lego.util.AbImageLoader;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.bigplan.lego.R;
import android.bigplan.lego.model.SecondMsg;
import android.bigplan.lego.view.RoundImageView;
public class SecondMsgAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    
    private List<SecondMsg> mData;
    
    private AbImageLoader mAbImageLoader = null;
    
    private String msgType = AbConstant.PUSHMSG_TYPE_SYS;
    
    public SecondMsgAdapter(Context context, String msgType, List<SecondMsg> data) {
        this.mInflater = LayoutInflater.from(context);
        mAbImageLoader = AbImageLoader.getInstance(context);
        mData = data;
        this.msgType = msgType;
    }
    
    @Override
    public int getCount() {
        return mData.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<SecondMsg> list) {

        this.mData = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final SecondMsg pushMsg = mData.get(position);
        String trim = pushMsg.getIsRead().trim();
        String isRead=trim.substring(0,1);
        if (view == null) {
            viewHolder = new SecondMsgAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.reception_room_item, null);
            viewHolder.icon = (RoundImageView)view.findViewById(R.id.grid_avatar);
            viewHolder.tvName = (TextView)view.findViewById(R.id.tv_name);
            viewHolder.tvtime = (TextView)view.findViewById(R.id.tv_date_roomitem);
            viewHolder.tvDesc = (TextView)view.findViewById(R.id.tv_desc);
            viewHolder.read_count = (TextView)view.findViewById(R.id.read_count);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (SecondMsgAdapter.ViewHolder)view.getTag();
        }

        viewHolder.tvName.setText(pushMsg.getTitle());
        viewHolder.tvDesc.setText(pushMsg.getSubTitle());
        String time = pushMsg.getTime();
//        long aLong = Long.parseLong(time);
//        aLong = aLong*1000;
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//        Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//        calendar.setTimeInMillis(aLong);
//        viewHolder.tvtime.setText(formatter.format(calendar.getTime()));
        viewHolder.tvtime.setText(time);
        if(isRead.equals("1")){
            viewHolder.read_count.setVisibility(View.GONE);
        }else{
            viewHolder.read_count.setVisibility(View.VISIBLE);
        }

        if (AbConstant.PUSHMSG_TYPE_SYS.equals(msgType)) {
            // 系统消息
            viewHolder.icon.setImageResource(R.drawable.i_tongzhi);
        }
        else if (AbConstant.PUSHMSG_TYPE_WALLET.equals(msgType)) {
            String pic = pushMsg.getPic();
            if (!TextUtils.isEmpty(pic)){
                mAbImageLoader.display(viewHolder.icon, R.drawable.default_tou, pic);
            }else{
                viewHolder.icon.setImageResource(R.drawable.i_shangye);
            }

        }
        else if (AbConstant.PUSHMSG_TYPE_STATE.equals(msgType)) {
            String pic = pushMsg.getPic();
            if (!TextUtils.isEmpty(pic)){
                mAbImageLoader.display(viewHolder.icon, R.drawable.default_tou, pic);
            }else{
                viewHolder.icon.setImageResource(R.drawable.i_shangye);
            }
        }
        return view;

        
    }
    
    final static class ViewHolder{
        RoundImageView icon;
        TextView tvName;
        TextView tvDesc;
        TextView read_count;
        TextView tvtime;
    }
    
}