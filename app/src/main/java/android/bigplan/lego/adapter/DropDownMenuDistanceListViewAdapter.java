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
public class DropDownMenuDistanceListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<DropDownMenuItem> mItemList = null;
    private int mIndexSelected;

    public DropDownMenuDistanceListViewAdapter(Context ctx, List<DropDownMenuItem> list, int indexSelected) {
        mContext = ctx;
        mItemList = list;
        mIndexSelected = indexSelected;
    }

    public void updateView(int indexSelected) {
        mIndexSelected = indexSelected;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_dropdownmenu_distance_item, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_Title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.iv_Icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.iv_Selected = (ImageView) view.findViewById(R.id.iv_selected);
            viewHolder.iv_Selected.setVisibility(View.INVISIBLE);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.iv_Selected.setVisibility(View.INVISIBLE);
        }

        DropDownMenuItem item = mItemList.get(position);
        if (mIndexSelected == position)
            viewHolder.iv_Selected.setVisibility(View.VISIBLE);

        viewHolder.iv_Icon.setBackgroundResource(mItemList.get(position).getResID());
        viewHolder.tv_Title.setText(mItemList.get(position).getTitle());

        return view;
    }


    final static public class ViewHolder {
        TextView tv_Title;
        ImageView iv_Icon;
        public ImageView iv_Selected;
    }
}
