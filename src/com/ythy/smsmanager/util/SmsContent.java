package com.ythy.smsmanager.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.ythy.smsmanager.vo.SMSInfo;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsContent {
	private Context mContext;
	private Uri uri;
	List<SMSInfo> infos;

	public SmsContent(Context context, Uri uri) {
		infos = new ArrayList<SMSInfo>();
		this.mContext = context;
		this.uri = uri;
	}

	/**
	 *获取短信的各种信息
	 * 
	 */
	public List<SMSInfo> getSmsInfo() {
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };
		Cursor cusor = mContext.getContentResolver().query(uri, projection, null, null,
				"date desc");
		int nameColumn = cusor.getColumnIndex("person");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		if (cusor != null) {
			while (cusor.moveToNext()) {
				SMSInfo smsinfo = new SMSInfo();
				smsinfo.setName(cusor.getString(nameColumn));
				SimpleDateFormat dateFormat = new SimpleDateFormat(   
                        "yyyy-MM-dd HH:mm:ss", Locale.CHINA);   
                Date d = new Date(Long.parseLong(cusor.getString(dateColumn)));   
				smsinfo.setDate(dateFormat.format(d));
				smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn));
				smsinfo.setSmsbody(cusor.getString(smsbodyColumn));
				infos.add(smsinfo);
			}
			cusor.close();
		}
		return infos;
	}
}