/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.bigplan.lego.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * 首页轮播ViewPager适配器
 * @author FingerArt FingerArt.me
 * @date 2015年12月24日 下午6:15:19
 */
public class AbViewPagerAdapter extends PagerAdapter {
	
	/** The m context. */
	private Context mContext;
	
	/** The m list views. */
	private ArrayList<View> mListViews = null;

	/**
	 * Instantiates a new ab view pager adapter.
	 * @param context the context
	 * @param mListViews the m list views
	 */
	@SuppressLint("UseSparseArrays")
	public AbViewPagerAdapter(Context context, ArrayList<View> mListViews) {
		this.mContext = context;
		this.mListViews = mListViews;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position);
	}

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mListViews==null? 0 : mListViews.size();
	}

	/**
	 * 描述：Object是否对应这个View.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return true, if is view from object
	 * @see PagerAdapter#isViewFromObject(View, Object)
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	/**
	 * 描述：显示View.
	 *
	 * @param container the container
	 * @param position the position
	 * @return the object
	 * @see PagerAdapter#instantiateItem(View, int)
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		View v = mListViews.get(position%getCount());
		((ViewPager) container).addView(v);
		return v;
	}

	/**
	 * 描述：移除View.
	 *
	 * @param container the container
	 * @param position the position
	 * @param object the object
	 * @see PagerAdapter#destroyItem(View, int, Object)
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}
}
