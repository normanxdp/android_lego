package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * Created by gg on 16-4-14.
 */
public class SelectCityGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mCityList;

    public SelectCityGridViewAdapter(Context ctx, String[] cityList) {
        mContext = ctx;
        mCityList = cityList;
    }

    @Override
    public int getCount() {
        return mCityList.length;
    }

    @Override
    public Object getItem(int position) {
        return mCityList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Button btn = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_city_loaction_btn, null);
            btn = (Button) view.findViewById(R.id.btn_city);
            view.setTag(btn);
        } else {
            btn = (Button) view.getTag();
        }

        btn.setText(mCityList[position]);

        return view;
    }
}
