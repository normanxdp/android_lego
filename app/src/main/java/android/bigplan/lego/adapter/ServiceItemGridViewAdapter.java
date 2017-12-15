package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.model.HomeGridViewButton;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gg on 16-3-27.
 */
public class ServiceItemGridViewAdapter extends BaseAdapter {
    private final static String TAG = ServiceItemGridViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<HomeGridViewButton> mBtnList = null;

    public ServiceItemGridViewAdapter(Context ctx, List<HomeGridViewButton> btnList) {
        mContext = ctx;
        mBtnList = btnList;
    }

    public void updateView(List<HomeGridViewButton> btnList) {
        mBtnList = btnList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBtnList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBtnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_home_service_btn, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_btnName = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.iv_Avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        HomeGridViewButton btn = mBtnList.get(position);
        viewHolder.iv_Avatar.setImageResource(btn.getResDrawable());
        viewHolder.tv_btnName.setText(btn.getTitle());

        return view;
    }

    final static class ViewHolder {
        TextView tv_btnName;
        ImageView iv_Avatar;
    }
}
