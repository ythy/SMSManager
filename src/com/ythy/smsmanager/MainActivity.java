package com.ythy.smsmanager;

import java.util.HashMap;
import java.util.List;

import com.ythy.smsmanager.adapter.DataListAdapter;
import com.ythy.smsmanager.provider.Providerdata;
import com.ythy.smsmanager.service.ServiceCollectSms;
import com.ythy.smsmanager.util.DBHelper;
import com.ythy.smsmanager.vo.SMSInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity{

	private SharedPreferences sp ;
	public static final String SP_SMSINFO = "smsinfo";
	public static final String SP_VALUE_IS_INIT = "IS_INIT"; 
	private ListView mainListview;
	private Button btnGroupSet;
	private LinearLayout llGroupSet;
	private InitSMSReceiver mybroadcast; //动态监听
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.smsmanager_smslistall);
		mainListview = (ListView) findViewById(R.id.lv_smslistall);
		mainListview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				return false;
			}
			
		});
		
		llGroupSet =  (LinearLayout) findViewById(R.id.ll_groupSetParent); 
		 
		btnGroupSet = (Button) findViewById(R.id.btn_set);
		btnGroupSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(llGroupSet.getVisibility() == View.GONE)
					llGroupSet.setVisibility(View.VISIBLE);
				else
					llGroupSet.setVisibility(View.GONE);
			}
		});
		
		mybroadcast = new InitSMSReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ServiceCollectSms.ACTION_SMS_INIT);
		registerReceiver(mybroadcast, filter);
		
		sp = this.getSharedPreferences(SP_SMSINFO, Context.MODE_PRIVATE);
		if(!sp.getBoolean(SP_VALUE_IS_INIT, false))
		{
			Intent smsIntent = new Intent(MainActivity.this,
					ServiceCollectSms.class);
			smsIntent.addFlags(ServiceCollectSms.FLAG_INIT);
			startService(smsIntent);
		}
		else
		{
			Runneble_init r = new Runneble_init();
			new Thread(r).start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class Runneble_init implements Runnable {

		public void run() {

			HashMap<String, String> peopleMap = getPeopleList();
			DBHelper mDBHelper = new DBHelper(MainActivity.this, Providerdata.DATABASE_NAME, null, Providerdata.DATABASE_VERSION);
			List<SMSInfo> infos = mDBHelper.querySMS(null, peopleMap);
			mDBHelper.Close();
		
			Message msg = new Message();  
            msg.what = 1;  
            msg.obj = infos;
            handler.sendMessage(msg); 
		}
	}
	
	private HashMap<String, String> getPeopleList() {
		HashMap<String, String> map = new HashMap<String, String>();
		Cursor cursor = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		while (cursor.moveToNext()) {
			String phoneName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // 电话姓名
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String hasPhone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone.compareTo("1") == 0) {
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					map.put(phoneNumber, phoneName);
				}
				phones.close();
			}
		}

		return map;
	}
	
	/**
	 *  activity 关联 receiver 监听手机即时短信等
	 *
	 */
	private class InitSMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ServiceCollectSms.ACTION_SMS_INIT)) {
				Runneble_init r = new Runneble_init();
				new Thread(r).start();
				
				if(!sp.getBoolean(SP_VALUE_IS_INIT, false))
				{
					sp.edit().putBoolean(SP_VALUE_IS_INIT, true).commit();
				}
			}
			
		}
	}
	
	private Handler handler = new Handler(){
		@Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            if(msg.what == 1){  
            	DataListAdapter dataListAdapter = new DataListAdapter(
    					MainActivity.this, (List<SMSInfo>)msg.obj);
    			mainListview.setAdapter(dataListAdapter);
            }  
        }  
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("MainActivity", "onDestroy");
		unregisterReceiver(mybroadcast);
	}

}
