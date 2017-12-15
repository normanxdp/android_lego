package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.model.ProvinceCity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gg on 16-4-14.
 */
public class SelectCityListViewAdapter extends BaseAdapter implements SectionIndexer {
    private final static String TAG = SelectCityListViewAdapter.class.getSimpleName();
    private List<ProvinceCity> list = null;

    private Context mContext;

    public SelectCityListViewAdapter (Context mContext, List<ProvinceCity> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ProvinceCity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return list==null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final ProvinceCity mCity = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_city_item, null);
            viewHolder.tvLetter = (TextView)view.findViewById(R.id.catalog);
            viewHolder.tvName = (TextView)view.findViewById(R.id.tv_name);
            viewHolder.tvDesc = (TextView)view.findViewById(R.id.tv_desc);
            viewHolder.tvPosition = (TextView)view.findViewById(R.id.tvPosition);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mCity.getFirstLetter());
        }
        else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvPosition.setText(this.list.get(position).getName());
        viewHolder.tvName.setText(this.list.get(position).getName());
        viewHolder.tvDesc.setText(this.list.get(position).getName());

        viewHolder.tvDesc.setText(this.list.get(position).getName() + "  " + this.list.get(position).getName());

        return view;

    }

    public static class ViewHolder{
        TextView tvLetter;
        TextView tvName;
        TextView tvPosition;
        TextView tvDesc;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        char[] charArray = list.get(position).getFirstLetter().toCharArray();
        return charArray[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        }
        else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
