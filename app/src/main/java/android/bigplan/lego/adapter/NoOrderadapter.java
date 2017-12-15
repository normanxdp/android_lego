package android.bigplan.lego.adapter;

import android.app.Activity;
import android.bigplan.lego.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class NoOrderadapter extends BaseAdapter /*implements HandlerHuiDiao */{
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		view = View.inflate(context, R.layout.item_null_qiangdan, null);
		return view;
	}
	private View view;
	private Context context;
	private Activity activity;
	public NoOrderadapter(Activity activity,Context context) {
		this.context = context;
		this.activity = activity;
	}

}
