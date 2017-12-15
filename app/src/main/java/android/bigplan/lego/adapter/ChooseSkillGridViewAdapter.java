package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by gg on 16-4-23.
 */
public class ChooseSkillGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int mResArray[];
    private int mResSelectedArray[];

    private int mChooseSkill;

    public ChooseSkillGridViewAdapter(Context ctx, int resArray[], int resSelectedArray[]) {
        mContext = ctx;
        mResArray = resArray;
        mResSelectedArray = resSelectedArray;
        mChooseSkill = 0;
    }

    public void chooseSkill(int index) {
        mChooseSkill = index;
        notifyDataSetChanged();
    }

    public int getChooseSkill() {
        return mChooseSkill;
    }

    @Override
    public int getCount() {
        return mResArray.length;
    }

    @Override
    public Object getItem(int position) {
        return mResArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageView imageView = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_choose_skill, null);

            imageView = (ImageView) view.findViewById(R.id.iv_skill);
            view.setTag(imageView);
        } else {
            imageView = (ImageView) view.getTag();
        }

        if (position == mChooseSkill) {
            imageView.setBackgroundResource(mResSelectedArray[position]);
        } else {
            imageView.setBackgroundResource(mResArray[position]);
        }

        return view;
    }
}
