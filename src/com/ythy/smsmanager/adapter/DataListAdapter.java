package com.ythy.smsmanager.adapter;

import java.util.List;

import com.ythy.smsmanager.vo.SMSInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataListAdapter extends BaseAdapter {

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	private Context mcontext;
	private LayoutInflater layoutInflator;
	private List<SMSInfo> list;

	public DataListAdapter() {
		// TODO Auto-generated constructor stub
	}

	public DataListAdapter(Context context, List<SMSInfo> items) {
		mcontext = context;
		layoutInflator = LayoutInflater.from(mcontext);
		list = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Component component = null;
		
		if (convertView == null) {
			convertView = layoutInflator.inflate(
					com.ythy.smsmanager.R.layout.smsmanager_smslist_data, null);
			component= new Component();
			component.tvSmsbody = (TextView) convertView
					.findViewById(com.ythy.smsmanager.R.id.tv_Smsbody);
			component.tvPhoneNumber = (TextView) convertView
					.findViewById(com.ythy.smsmanager.R.id.tv_PhoneNumber);
			component.tvDate = (TextView) convertView
					.findViewById(com.ythy.smsmanager.R.id.tv_Date);
			convertView.setTag(component);
		}
		else
		{
			component = (Component) convertView.getTag();  
		}
		
		try {
			component.tvSmsbody.setText(list.get(arg0).getSmsbody());
			if(list.get(arg0).getName() != null && !list.get(arg0).getName().equals(""))
				component.tvPhoneNumber.setText(list.get(arg0).getName());
			else
				component.tvPhoneNumber.setText(list.get(arg0).getPhoneNumber());
			component.tvDate.setText(list.get(arg0).getDate());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	private static class Component{
		 public TextView tvSmsbody;  
		 public TextView tvPhoneNumber;  
		 public TextView tvDate; 
	}
}
