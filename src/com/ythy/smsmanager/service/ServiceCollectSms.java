package com.ythy.smsmanager.service;

import java.util.List;

import com.ythy.smsmanager.provider.Providerdata;
import com.ythy.smsmanager.receiver.SMSReceiver;
import com.ythy.smsmanager.util.DBHelper;
import com.ythy.smsmanager.util.SmsContent;
import com.ythy.smsmanager.vo.SMSInfo;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class ServiceCollectSms extends Service {
	
	/**
	 * 收件箱短信
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	public static final String ACTION_SMS_INIT = "com.ythy.smsmanager.initsmslist";
	
	public static final int FLAG_INIT = 0; //初始查询短信数据库
	public static final int FLAG_RECEIVE = 1; //接收短消息
	
	private List<SMSInfo> smsInfos;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(intent.getFlags() == FLAG_INIT)
		{
			Uri uri = Uri.parse(SMS_URI_INBOX);
			SmsContent sc = new SmsContent(this, uri);
			smsInfos = sc.getSmsInfo();
		}
		else if(intent.getFlags() == FLAG_RECEIVE)
		{
			smsInfos = intent.getParcelableArrayListExtra(SMSReceiver.INTENT_SMS_RECEIVE_LIST);
		}
		
		Runneble_InsertDB r = new Runneble_InsertDB();
		new Thread(r).start();
		
		return Service.START_NOT_STICKY;
	}
	
	
	private class Runneble_InsertDB implements Runnable{
		
		public void run() {
			DBHelper mDBHelper = new DBHelper(  
					ServiceCollectSms.this, Providerdata.DATABASE_NAME, null, Providerdata.DATABASE_VERSION);  
			for(int i = 0; i < smsInfos.size(); i++)
			{
				mDBHelper.addSMS(smsInfos.get(i).getDate(), smsInfos.get(i).getPhoneNumber()
						, smsInfos.get(i).getSmsbody());
			}
			mDBHelper.Close();
			 
			Intent serviceIntent = new Intent();
		    serviceIntent.setAction(ACTION_SMS_INIT);
		    sendBroadcast(serviceIntent); 
			
			stopSelf();
		}
	}
}
