package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.util.AbImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by gg on 16-5-12.
 */
public class OrderDetailPackagePicGridViewAdapter extends BaseAdapter {

    private AbImageLoader mAbImageLoader = null;
    private Context mContext;
    private String[] mPicUrlList;

    public OrderDetailPackagePicGridViewAdapter(Context ctx, String[] list) {
        mContext = ctx;
        mPicUrlList = list;
        mAbImageLoader = AbImageLoader.getInstance(mContext);
    }

    public void updateView(String[] list) {
        mPicUrlList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPicUrlList.length;
    }

    @Override
    public Object getItem(int position) {
        return mPicUrlList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ImageView ivPic = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_order_detail_package_pic, null);
            ivPic = (ImageView) view.findViewById(R.id.iv_pic);

            view.setTag(ivPic);
        } else {
            ivPic = (ImageView) view.getTag();
        }

        mAbImageLoader.display(ivPic, R.drawable.icon_default, mPicUrlList[position]);

        return view;
    }
}
