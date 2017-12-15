package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.model.UploadPicture;
import android.bigplan.lego.util.AbImageUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/**
 * Created by gg on 16-5-10.
 */
public class PackagePicGridviewAdapter extends BaseAdapter {

    private Context mContext;
    private List<UploadPicture> mPicList;

    public PackagePicGridviewAdapter(Context ctx, List<UploadPicture> list) {
        mContext = ctx;
        mPicList = list;
    }

    @Override
    public int getCount() {
        return mPicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_package_pic, null);
            viewHolder.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            viewHolder.btn_delete = (ImageView) view.findViewById(R.id.btn_delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        UploadPicture uploadPicture = mPicList.get(position);

        if (uploadPicture.getPicFilePath() == null) {
            viewHolder.btn_delete.setVisibility(View.GONE);
            viewHolder.iv_pic.setImageResource(R.drawable.icon_add_photo);
            return view;
        }

        String filePath = uploadPicture.getPicFilePath();
        if (filePath != null && !filePath.equalsIgnoreCase("")) {
            Bitmap thumbBitmap = AbImageUtil.getScaleBitmap(new File(filePath), 200, 200);
            viewHolder.iv_pic.setImageBitmap(thumbBitmap);
        }

        viewHolder.btn_delete.setVisibility(View.VISIBLE);
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPicList.remove(position);
                for (int i = 0; i < mPicList.size(); i++) {
                    mPicList.get(i).setShowDelete(false);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public final static class ViewHolder {
        int position;
        public ImageView iv_pic;
        public ImageView btn_delete;
    }


}
