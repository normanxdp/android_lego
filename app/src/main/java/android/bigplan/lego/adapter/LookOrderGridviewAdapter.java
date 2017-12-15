package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.util.AbImageLoader;
import android.bigplan.lego.view.RoundImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.util.ArrayList;

/**
 * Created by gg on 16-4-23.
 */
public class LookOrderGridviewAdapter extends BaseAdapter {
    private Context mContext;
    private    ArrayList<String> image;


    public LookOrderGridviewAdapter(Context context) {
        this.  mContext = context;
    }
    public LookOrderGridviewAdapter(Context ctx, ArrayList<String> image ) {
      this.  mContext = ctx;
        this. image = image;

    }



    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int position) {
        return image.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_choose_skill, null);
            viewHolder.head = (RoundImageView)view.findViewById(R.id.iv_skill);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        String imageurl = image.get(position);
//    public void display(ImageView imageView,final int defaultImage,String url){
        AbImageLoader.getInstance(mContext).display(viewHolder.head ,R.drawable.ic_launcher,imageurl);

        return view;
    }
    final static class ViewHolder{
        ImageView head;
    }
}
