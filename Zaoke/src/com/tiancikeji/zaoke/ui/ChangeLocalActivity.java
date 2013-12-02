package com.tiancikeji.zaoke.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.LocListHttp;
import com.tiancikeji.zaoke.httpservice.base.LocListBase;
import com.tiancikeji.zaoke.httpservice.base.LocalBase;
import com.tiancikeji.zaoke.httpservice.base.OrderBase;
import com.tiancikeji.zaoke.ui.adapter.ChangeLocalAdapter;
import com.tiancikeji.zaoke.ui.adapter.MultiExpandableAdapter;
import com.tiancikeji.zaoke.util.ExitApplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChangeLocalActivity extends AbstractActivity  {

	private ListView change_local_listView;
	private LocListBase llb = new LocListBase();
	private OrderBase orderBase;
	
	private MultiExpandableAdapter mAdapter;
	private List<Map<String,Object>> dataList;
	private List<Map<String,Object>> child1;
	private List<Map<String,Object>> child2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_local);
		setUpView();

		Bundle bundle = getIntent().getExtras();
		orderBase = (OrderBase) bundle.getSerializable("orderBase");
		bundle.putSerializable("orderBase", orderBase);
//		ExitApplication.getInstance().addActivity(ChangeLocalActivity.this);
//		if (bundle.getBoolean("isRoot")) {
//			showProgressDialog("正在获取地址...");
//			new Thread(new LocListHttp(this, new mHandler())).start();
//		} 
//		else {
//			ChangeLocalAdapter adapter = new ChangeLocalAdapter(ChangeLocalActivity.this, (List<LocalBase>) bundle.getSerializable("pick_locs"),orderBase);
//			change_local_listView.setAdapter(adapter);
//		}
	}

	public void setUpView() {
		change_local_listView = (ListView) this.findViewById(R.id.change_local_listView);
		change_local_listView.setOnItemClickListener(new ItemClickListner());
		new Thread(new LocListHttp(this, new mHandler())).start();
	}

	private List<Map<String, Object>> getListData(List<LocalBase> locations) {
		
		dataList = new ArrayList<Map<String,Object>>();
		for(int i = 0 ;i <locations.get(0).getPick_loc_list().get(0).getPick_loc_list().size() ; i++){
			Map<String,Object> itemi = new HashMap<String, Object>();
			itemi.put(MultiExpandableAdapter.KEY_LEVEL, 1);
			itemi.put(MultiExpandableAdapter.KEY_EXPANDED, false);
			itemi.put("title", locations.get(0).getPick_loc_list().get(0).getPick_loc_list().get(i).getPick_loc_name());
			itemi.put("obj", locations.get(0).getPick_loc_list().get(0).getPick_loc_list().get(i));
			dataList.add(itemi);
		}
		
//		createChild(locations);
		
		return dataList;
	}
	
//	private void createChild(List<LocalBase> locations) {
//		child1 = new ArrayList<Map<String,Object>>();
//		Map<String,Object> item11 = new HashMap<String, Object>();
//		
//		item11.put(MultiExpandableAdapter.KEY_LEVEL, 2);
//		item11.put(MultiExpandableAdapter.KEY_EXPANDED, true);
//		
//		item11.put("title", locations.get(0).getPick_loc_list().get(0).getPick_loc_name());
//		item11.put("obj", locations.get(0).getPick_loc_list().get(0));
//		
//		child1.add(item11);
//		
//		child2 = new ArrayList<Map<String,Object>>();
//		Map<String,Object> item2 = new HashMap<String, Object>();
//		
//		item2.put(MultiExpandableAdapter.KEY_LEVEL, 3);
//		item2.put(MultiExpandableAdapter.KEY_EXPANDED, true);
//		
//		item2.put("title", locations.get(0).getPick_loc_list().get(0).getPick_loc_list().get(0).getPick_loc_name());
//		item2.put("obj", locations.get(0).getPick_loc_list().get(0).getPick_loc_list().get(0));
//		
//		child2.add(item2);
//
//	}
	
	private class ItemClickListner implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Map<String, Object> item = dataList.get(position);
//			String title = (String)item.get("title");
			LocalBase  locs = (LocalBase)item.get("obj");
//			boolean expanded = (Boolean)item.get(MultiExpandableAdapter.KEY_EXPANDED);
//			System.out.println(item.get(MultiExpandableAdapter.KEY_LEVEL));
//			if(!expanded){
//				if("朝阳区".equals(title)){
//					mAdapter.addChildListData(child1, position);
//				}
//				if("亮马桥".equals(title)){
//					mAdapter.addChildListData(child2, position);
//				}
//			}else{
//				mAdapter.removeChildListData(position);
//			}
//			if("3".equals(item.get(MultiExpandableAdapter.KEY_LEVEL).toString())){
				Intent toChangelocal = new Intent(ChangeLocalActivity.this,ShopingCartActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("orderBase", orderBase);
//				bundle.putString("cart_place_name_string", title);
				AccountService as = new AccountService(ChangeLocalActivity.this);
				Dbaccount account = as.getAccount();
				account.setLocal(locs.getPick_loc_name());
				account.setStarttime(locs.getPick_start_time());
				account.setEndtime(locs.getPick_end_time());
				account.setLocid(locs.getPick_loc_id());
				as.saveOrUpdate(account);
				toChangelocal.putExtras(bundle);
				startActivity(toChangelocal);
//			}
		}
	}
	
	
	
	
	
	private class mHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				displayResponse("服务器访问失败");
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showNoNetWork();
				break;
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				llb = (LocListBase) msg.obj;
				if (llb.getStatus() == 0) {
//					ChangeLocalAdapter adapter = new ChangeLocalAdapter(ChangeLocalActivity.this, llb.getPick_locs(),orderBase);
//					change_local_listView.setAdapter(adapter);
					String[] from  = {"icon","title"};
					int[] to = {R.id.item_text,R.id.item_text};
					mAdapter =  new MultiExpandableAdapter(ChangeLocalActivity.this, getListData(llb.getPick_locs()), R.layout.list_item,
						from, to);
					change_local_listView.setAdapter(mAdapter);
				} else {
					displayResponse("服务器无数据");
				}
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				displayResponse("网络访问超时");
				break;
			default:
				break;
			}
		}
	};

}
