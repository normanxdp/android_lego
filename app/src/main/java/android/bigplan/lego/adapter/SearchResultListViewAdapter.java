package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.model.SearchResult;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.util.AbLogUtil;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;

import java.util.List;


public class SearchResultListViewAdapter extends BaseAdapter {
    private final static String TAG = "SearchResultListViewAdapter";

    private Context mContext = null;
    private List<SearchResult> mResultList = null;
    private AbImageLoader mAbImageLoader;

    public SearchResultListViewAdapter(Context context, List<SearchResult> resultList) {
        mContext = context;
        mResultList = resultList;
        mAbImageLoader = AbImageLoader.getInstance(mContext);
    }

    public void updateView(List<SearchResult> resultList) {
        mResultList.clear();
        mResultList = resultList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mResultList != null){
            return mResultList.size();
        }
        return  0;
    }

    @Override
    public Object getItem(int position) {
        if (mResultList != null){
            return mResultList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
      //  if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_search_result, null);
            viewHolder.tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
            viewHolder.tv_introduction = (TextView) view.findViewById(R.id.tv_introduction);
            viewHolder.rb_star = (RatingBar) view.findViewById(R.id.rb_score);
            viewHolder.icon = (ImageView) view.findViewById(R.id.iv_avatar);
            viewHolder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            view.setTag(viewHolder);
     //   } else {
         //   viewHolder = (ViewHolder) view.getTag();
       // }
        SearchResult  result = mResultList.get(position);
        viewHolder.tv_store_name.setText(result.getStoreName());

        String introduction = result.getIntroduction();
        if (!TextUtils.isEmpty(introduction)){
            viewHolder.tv_introduction.setText(introduction);
        }else{
            viewHolder.tv_introduction.setText(result.getStoreName());
        }

        viewHolder.tv_distance.setText(result.getDistance());
        viewHolder.rb_star.setStar(Float.parseFloat(result.getStar()));
        AbLogUtil.d("position="+position+"getAvatar:" +  result.getAvatar() + ",id="+result.getMemberId());
        mAbImageLoader.display(viewHolder.icon, result.getAvatar());

        return view;
    }

    public final static class ViewHolder {
        public TextView tv_store_name;
        public TextView tv_introduction;
        public ImageView icon;
        public TextView tv_distance;
        public RatingBar rb_star;
    }
}
