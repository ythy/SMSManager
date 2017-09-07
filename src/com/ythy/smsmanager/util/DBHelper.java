package com.ythy.smsmanager.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ythy.smsmanager.provider.Providerdata.SMS;
import com.ythy.smsmanager.vo.SMSInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {   
    
    private Context mContext;
	
    public DBHelper(Context context, String name,    
            CursorFactory factory, int version) {   
        super(context, name, factory, version);
        this.getWritableDatabase(); 
        mContext = context;
    }
    
    /**
     * should be invoke when you never use DBhelper
     * To release the database and etc.
     */
    public void Close() {
    	this.getWritableDatabase().close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {   
        db.execSQL("CREATE TABLE IF NOT EXISTS "    
                + SMS.TABLE_NAME + " ("    
                + SMS.ID + " INTEGER PRIMARY KEY,"    
                + SMS.COLUMN_DATE + " VARCHAR,"  
                + SMS.COLUMN_SMSBODY + " VARCHAR," 
                + SMS.COLUMN_NUMBER + " VARCHAR)");   
    }   
    
    @Override
    public void onUpgrade(SQLiteDatabase db,    
            int oldVersion, int newVersion) {   
        //db.execSQL("DROP TABLE IF EXISTS " + SMS.TABLE_NAME);   
        //onCreate(db);   
    }
    
    public void addSMS(String date, String number, String body) {
    	ContentValues values = new ContentValues();   
        values.put(SMS.COLUMN_DATE, date);   
        values.put(SMS.COLUMN_NUMBER, number);   
        values.put(SMS.COLUMN_SMSBODY, body); 
        if(!hasSmsContent(number, date))
        {
//        	this.getWritableDatabase().insert(
//        			SMS.TABLE_NAME, SMS.ID, values);  
        	 mContext.getContentResolver().insert(SMS.CONTENT_URI, values);
        }
        
       

    }
    
    private boolean hasSmsContent(String number, String date) {
    	
    	String[] projection = new String[] {SMS.ID};
    	String selection = SMS.COLUMN_NUMBER + "=?" + " AND " 
    					+  SMS.COLUMN_DATE + "=?";
    	String[] selectionArg = new String[] {number, date};
    	
    	Cursor cusor = mContext.getContentResolver().query(SMS.CONTENT_URI, projection, selection, selectionArg, null);
    	
//		Cursor cusor = this.getWritableDatabase().query(SMS.TABLE_NAME, projection, selection, selectionArg, null, null,
//				SMS.COLUMN_DATE + " ASC");
		
		if (cusor != null && cusor.getCount() > 0 )
			return true;
		else
			return false;
    }

    public List<SMSInfo> querySMS(String group, HashMap<String, String> peolle) {
    	
    	List<SMSInfo> infos = new ArrayList<SMSInfo>();
    	String[] projection = new String[] { SMS.ID, SMS.COLUMN_DATE, SMS.COLUMN_NUMBER,
    			SMS.COLUMN_SMSBODY};
    	
    	Cursor cusor = mContext.getContentResolver().query(SMS.CONTENT_URI, projection, null, null, null);

//		Cursor cusor = this.getWritableDatabase().query(SMS.TABLE_NAME, projection, null, null, null, null,
//				SMS.COLUMN_DATE + " ASC");
		
		int dateColumn = cusor.getColumnIndex(SMS.COLUMN_DATE);
		int phoneNumberColumn = cusor.getColumnIndex(SMS.COLUMN_NUMBER);
		int smsbodyColumn = cusor.getColumnIndex(SMS.COLUMN_SMSBODY);
		
		if (cusor != null) {
			while (cusor.moveToNext()) {
				SMSInfo smsinfo = new SMSInfo();
				smsinfo.setName((String) peolle.get(cusor.getString(phoneNumberColumn)));
				smsinfo.setDate(cusor.getString(dateColumn));
				smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn));
				smsinfo.setSmsbody(cusor.getString(smsbodyColumn));
				infos.add(smsinfo);
			}
			cusor.close();
		}
		return infos;
    }
} 