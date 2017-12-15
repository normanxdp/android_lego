package android.bigplan.lego.adapter;

import android.bigplan.lego.R;
import android.bigplan.lego.fragment.HomeMainStaffFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeMainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mDatas;
    private static String[] mtitles = {"离我最近"};
    public HomeMainFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mDatas = new ArrayList<>();
        mDatas.add(newInstance(R.layout.fragment_home_main, ArroundStaffListViewAdapter.AroundAdapterType.AroundStaff0));
 //       mDatas.add(newInstance(R.layout.fragment_home_main, ArroundStaffListViewAdapter.AroundAdapterType.AroundStaff1));
    }
    public Fragment newInstance(int home_main,ArroundStaffListViewAdapter.AroundAdapterType Type) {
        Bundle bundle  = new Bundle();
        bundle.putInt("home_main", home_main);
        String uri = Type.getUri();
        String type = Type.getType();
        bundle.putString("values", uri);
        bundle.putString("type", type);

        HomeMainStaffFragment fragment = new HomeMainStaffFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mtitles[position];
    }
}
