package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.util.AbImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PaySelectAdapter extends BaseAdapter{
    private LayoutInflater mInflater;

    private List<String> mData = new ArrayList<String>();
private int [] image = {R.drawable.moren,R.drawable.zfb,R.drawable.weixin,R.drawable.qq };
    private AbImageLoader mAbImageLoader = null;

    private String msgType = AbConstant.PUSHMSG_TYPE_SYS;
    Context context;
    public PaySelectAdapter(Context context   ) {
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        mData.add("钱包支付");
  //      mData.add("支付宝支付");
        mData.add("微信支付");
   //     mData.add("QQ钱包支付");
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final String model = mData.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.payselectsigledailog, null);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.iv_avatar_payselect);
            viewHolder.tv_apply_title = (TextView)convertView.findViewById(R.id.tv_name_payselect);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tv_apply_title.setText(model);
        viewHolder.icon.setBackgroundResource(image[position]);
        return convertView;

    }

    final static class ViewHolder{

        ImageView icon;


        TextView tv_apply_title;



    }

}