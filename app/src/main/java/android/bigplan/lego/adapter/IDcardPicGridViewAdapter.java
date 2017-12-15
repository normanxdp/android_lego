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
 * Created by gg on 16-5-14.
 */
public class IDcardPicGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mPicUrlList;
    private AbImageLoader mAbImageLoader = null;

    public IDcardPicGridViewAdapter (Context ctx, String[] list) {
        mContext = ctx;
        mPicUrlList = list;
        mAbImageLoader = AbImageLoader.getInstance(mContext);
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
        ImageView imageView = null;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_idcard_pic, null);

            imageView = (ImageView) view.findViewById(R.id.iv_pic);
            view.setTag(imageView);
        } else {
            imageView = (ImageView) view.getTag();
        }

        mAbImageLoader.display(imageView, R.drawable.default_head, mPicUrlList[position]);

        return view;
    }
}
