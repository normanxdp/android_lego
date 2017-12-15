package android.bigplan.lego.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import android.bigplan.lego.R;
import android.bigplan.lego.model.Referee;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbStrUtil;
import android.bigplan.lego.view.RoundImageView;

public class MyRefereeListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    // 图片下载类
    private AbImageLoader mAbImageLoader = null;
    private List<Referee> mData;

    public MyRefereeListAdapter(Context context, List<Referee> data) {
        this.mInflater = LayoutInflater.from(context);
        mData = data;
        // 图片的下载
        mAbImageLoader = AbImageLoader.getInstance(context);
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }
    
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<Referee> list) {
        this.mData = list;
        notifyDataSetChanged();
    }
    
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Referee item = mData.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.businessneed_list_item, null);
            viewHolder.icon = (RoundImageView)view.findViewById(R.id.grid_avatar);
            viewHolder.tvName = (TextView)view.findViewById(R.id.tv_name);
            viewHolder.tvDesc = (TextView)view.findViewById(R.id.tv_desc);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        
        viewHolder.tvName.setText(item.getShowName());
        viewHolder.tvDesc.setText(item.getLevel());

        mAbImageLoader.display(viewHolder.icon, R.drawable.faqihuodong, AbStrUtil.getSmallImage(this.mData.get(position).getAvatar()));

        return view;
        
    }
    
    final static class ViewHolder{
        
        RoundImageView icon;
        
        TextView tvName;
        
        TextView tvDesc;
    }
    
}
