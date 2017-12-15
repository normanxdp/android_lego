package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.model.DropDownMenuItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gg on 16-4-24.
 */
public class DropDownMenuTypeListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<DropDownMenuItem> mItemList = null;

    public DropDownMenuTypeListViewAdapter(Context ctx, List<DropDownMenuItem> list) {
        mContext = ctx;
        mItemList = list;
    }

    public void updateView() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_dropdownmenu_type_item, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_Title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.iv_Icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_showNum = (TextView) view.findViewById(R.id.tv_show_num);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DropDownMenuItem item = mItemList.get(position);

        viewHolder.iv_Icon.setBackgroundResource(item.getResID());
        viewHolder.tv_Title.setText(item.getTitle());
        viewHolder.tv_showNum.setText(Integer.toString(item.getTotalSum()));
        return view;
    }


    final static public class ViewHolder {
        TextView tv_Title;
        ImageView iv_Icon;
        TextView tv_showNum;
    }
}
