package com.tiancikeji.zaoke.ui.adapter;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
public class MultiExpandableAdapter extends SimpleAdapter {

	public static final String KEY_LEVEL = "KEY_LEVEL"; 
	public static final String KEY_EXPANDED = "KEY_EXPANDED";
	public static final int CHILD_BASE_OFFSET = 30;
	
	private int mChildOffset;
	private List<Map<String,Object>> mTreeList;
	public MultiExpandableAdapter(Context context,List<Map<String, Object>> list, 
			int resource,String[] from, int[] to) {
		super(context, list, resource, from, to);
		mTreeList = list;
		mChildOffset = CHILD_BASE_OFFSET;
	}
	@Override
	public int getCount() {
		if(mTreeList == null){
			return 0;
		}
		return mTreeList.size();
	}
	@Override
	public Object getItem(int position) {
		if(mTreeList != null && mTreeList.size() > 0 
			&& position < mTreeList.size() && position > -1 ){
			return mTreeList.get(position);
		}
		return null;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		Log.i("adapter", "execute......");
		return getOffsetChangeView(position, convertView, parent);
	}
	protected View getOffsetChangeView(int position, View convertView,ViewGroup parent) {
		if(convertView != null){
			Map<String,Object> item = mTreeList.get(position);
			Integer level = (Integer)item.get(KEY_LEVEL);
			if(level instanceof Integer){
				convertView.setPadding( level.intValue() * mChildOffset, 0, 0, 0);
			}
		}
		return convertView;
	}
	public void addChildListData(List<Map<String,Object>> childList,int parent){
		if(mTreeList ==null || mTreeList.size() == 0){
			return;
		}
		if(parent < 0 || parent >= mTreeList.size()){
			return;
		}
		Map<String,Object> item = mTreeList.get(parent);
		boolean expanded = (Boolean)item.get(KEY_EXPANDED);
		if(expanded) return;

		mTreeList.addAll(parent + 1, childList);
		item.put(KEY_EXPANDED, true);
		notifyDataSetChanged();
	}
	public void removeChildListData(int parent){
		if(mTreeList ==null || mTreeList.size() == 0){
			return;
		}
		if(parent < 0 || parent >= mTreeList.size()){
			return;
		}
		Map<String,Object> item = mTreeList.get(parent);
		boolean expanded = (Boolean)item.get(KEY_EXPANDED);
		if(!expanded) return;
		
		int pLevel = (Integer)item.get(KEY_LEVEL);
		for(int i = parent + 1 ; i < mTreeList.size();){
			Map<String,Object> temp = mTreeList.get(i);
			int cLevel = (Integer)temp.get(KEY_LEVEL);
			if(cLevel > pLevel){
				temp.put(KEY_EXPANDED, false);
				mTreeList.remove(i);
			}else{
				break;
			}
		}
		item.put(KEY_EXPANDED, false);
		notifyDataSetChanged();
	}
	public boolean isExpanded(int position){
		if(mTreeList ==null || mTreeList.size() == 0){
			return false;
		}
		if(position < 0 || position >= mTreeList.size()){
			return false;
		}
		Map<String,Object> item = mTreeList.get(position);
		return (Boolean)item.get(KEY_EXPANDED);
	}
	public void setChildOffset(int offset){
		mChildOffset = offset;
		notifyDataSetChanged();
	}

}
