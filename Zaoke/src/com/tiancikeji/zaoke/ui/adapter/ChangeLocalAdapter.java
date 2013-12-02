package com.tiancikeji.zaoke.ui.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.base.LocalBase;
import com.tiancikeji.zaoke.httpservice.base.OrderBase;
import com.tiancikeji.zaoke.ui.ChangeLocalActivity;
import com.tiancikeji.zaoke.ui.OrderActivity;
import com.tiancikeji.zaoke.ui.R;
import com.tiancikeji.zaoke.ui.ShopingCartActivity;
import com.tiancikeji.zaoke.util.ExitApplication;

public class ChangeLocalAdapter extends BaseAdapter {

	private Context mContext;
	private List<LocalBase> pick_locs;
	private LayoutInflater mInflater;
	private OrderBase orderBase;
	public ChangeLocalAdapter(Context mContext, List<LocalBase> pick_locs,OrderBase orderBase) {
		super();
		this.mContext = mContext;
		this.pick_locs = pick_locs;
		mInflater = LayoutInflater.from(mContext);
		this.orderBase = orderBase;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pick_locs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pick_locs.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LocalBase locs = pick_locs.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.change_local_item, null);
			viewHolder = new ViewHolder();

			viewHolder.title = (TextView) convertView.findViewById(R.id.change_local_item_title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.change_local_item_image);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(locs.getPick_loc_name());
		int view_visibililty_value = 0;
		boolean ifToChangeLocalActivity = true;
		if (locs.getPick_loc_list() != null && locs.getPick_loc_list().size() != 0) {
			view_visibililty_value = View.VISIBLE;
		} else {
			view_visibililty_value = View.GONE;
			ifToChangeLocalActivity = false;
		}
		viewHolder.image.setVisibility(view_visibililty_value);
		convertView.setOnClickListener(new ConvertViewClicker(locs,ifToChangeLocalActivity));
		return convertView;
	}
	
	private class ConvertViewClicker implements OnClickListener {
		private  LocalBase locs;
		private  boolean ifToChangeLocalActivity;
		
		public ConvertViewClicker(LocalBase locs,boolean ifToChangeLocalActivity) {
			super();
			this.locs = locs;
			this.ifToChangeLocalActivity = ifToChangeLocalActivity;
		}

		@Override
		public void onClick(View v) {
			if(this.ifToChangeLocalActivity){
				Intent toChangeLocalActivity = new Intent();
				toChangeLocalActivity.setClass(mContext, ChangeLocalActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("isRoot", false);
				
				orderBase = (OrderBase) bundle.getSerializable("orderBase");

				bundle.putSerializable("orderBase", orderBase);
				bundle.putSerializable("pick_locs", (Serializable) locs.getPick_loc_list());
				toChangeLocalActivity.putExtras(bundle);
				mContext.startActivity(toChangeLocalActivity);
			}else{
				Intent toShopingcartActivity = new Intent(mContext, ShopingCartActivity.class);
				Bundle bundle = new Bundle();
				orderBase = (OrderBase) bundle.getSerializable("orderBase");
				bundle.putSerializable("orderBase", orderBase);
				toShopingcartActivity.putExtras(bundle);
				ExitApplication.getInstance().exit();
				mContext.startActivity(toShopingcartActivity);
			}
			 

			// TODO Auto-generated method stub
//			AccountService as = new AccountService(mContext);
//			Dbaccount account = as.getAccount();
//			account.setLocal(locs.getPick_loc_name());
//			account.setStarttime(locs.getPick_start_time());
//			account.setEndtime(locs.getPick_end_time());
//			account.setLocid(locs.getPick_loc_id());
//			as.saveOrUpdate(account);
//			ExitApplication.getInstance().exit();
		}
		
		
	}

	class ViewHolder {

		TextView title;
		ImageView image;
	}

}
