package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.model.SearchTag;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gg on 17-9-16.
 */

public class SearchHistoryListViewAdapter extends BaseAdapter {
    private final static String TAG = SearchHistoryListViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<SearchTag> mSearchTagList;

    public SearchHistoryListViewAdapter(Context context, List<SearchTag> tagList) {
        mContext = context;
        mSearchTagList = tagList;
        Log.d(TAG, "mSearchTagList size " + mSearchTagList.size());
    }

    public void updateView(List<SearchTag> tagList) {
        mSearchTagList = tagList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSearchTagList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchTagList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_search_history, null);
            viewHolder.tv_search_tag = (TextView) view.findViewById(R.id.tv_search_tag);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Log.d(TAG, "getView position: " + position + " keyword  " + mSearchTagList.get(position).getKeyWords());
        viewHolder.tv_search_tag.setText(mSearchTagList.get(position).getKeyWords());

        return view;
    }

    public final static class ViewHolder {
        public TextView tv_search_tag;
        public ImageView icon;
    }
}
