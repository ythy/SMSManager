package com.ythy.smsmanager.receiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.ythy.smsmanager.service.ServiceCollectSms;
import com.ythy.smsmanager.vo.SMSInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	public static final String INTENT_SMS_RECEIVE_LIST = "SMS_RECEIVE_LIST";
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			SmsMessage[] messages = getMessagesFromIntent(intent);
			
			Intent smsIntent = new Intent(context,
					ServiceCollectSms.class);
			smsIntent.addFlags(ServiceCollectSms.FLAG_RECEIVE);
			
			ArrayList<SMSInfo> infos = new ArrayList<SMSInfo>();
			for (SmsMessage message : messages) {
				SMSInfo smsinfo = new SMSInfo();
				SimpleDateFormat dateFormat = new SimpleDateFormat(   
	                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA);   
	            Date d = new Date(message.getTimestampMillis());   
				smsinfo.setDate(dateFormat.format(d));
				smsinfo.setPhoneNumber(message.getDisplayOriginatingAddress());
				smsinfo.setSmsbody(message.getDisplayMessageBody());
				infos.add(smsinfo);
			}
			smsIntent.putParcelableArrayListExtra(INTENT_SMS_RECEIVE_LIST, infos);
			context.startService(smsIntent);
		}
	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

}
