package com.ythy.smsmanager.provider;

import java.util.HashMap;

import com.ythy.smsmanager.provider.Providerdata.SMS;
import com.ythy.smsmanager.util.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class SMSProvider extends ContentProvider   {
	
	private DBHelper dbHelper;
	private static UriMatcher sUriMatcher = null; 
	private static final int SMSINFO_COLLECTION = 1;  
	private static final int SMSINFO_COLLECTION_ID = 2; 
	private static HashMap<String, String> smsProjectionMap; 
	
	static{ 
	     
		 sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);  
		 sUriMatcher.addURI(Providerdata.AUTHORITY, SMS.TABLE_NAME, SMSINFO_COLLECTION);  
		 sUriMatcher.addURI(Providerdata.AUTHORITY, SMS.TABLE_NAME + "/#", SMSINFO_COLLECTION_ID); 
		 smsProjectionMap = new HashMap<String, String>();
		 smsProjectionMap.put(SMS.COLUMN_NUMBER, SMS.COLUMN_NUMBER);
		 smsProjectionMap.put(SMS.COLUMN_DATE, SMS.COLUMN_DATE);
		 smsProjectionMap.put(SMS.COLUMN_SMSBODY, SMS.COLUMN_SMSBODY);
		 smsProjectionMap.put(SMS.ID, SMS.ID);
	} 
	
	

	    
	@Override  
	public boolean onCreate() { 
		dbHelper = new DBHelper(getContext(), Providerdata.DATABASE_NAME, null, Providerdata.DATABASE_VERSION); 
		return true;  
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
        case SMSINFO_COLLECTION:
            count = db.delete(SMS.TABLE_NAME, selection, selectionArgs);
            break;
        case SMSINFO_COLLECTION_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(SMS.TABLE_NAME,
            		SMS.ID + "=" + noteId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                    + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
	       case SMSINFO_COLLECTION:
	           return SMS.CONTENT_TYPE;
	       case SMSINFO_COLLECTION_ID:
	           return SMS.CONTENT_TYPE_ITEM;
	       default:
	           throw new IllegalArgumentException("Unknown uri" + uri);
	       }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();  
		long rowId = db.insert(SMS.TABLE_NAME, SMS.ID,  values); 
		if(rowId > 0){ 
			Uri insertUri=ContentUris.withAppendedId(SMS.CONTENT_URI,  rowId); 
			getContext().getContentResolver().notifyChange(insertUri, null); 
			return insertUri;  
		}  
		return null;  
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder qBuilder=new SQLiteQueryBuilder(); 
		
		switch(sUriMatcher.match(uri)){ 
			case SMSINFO_COLLECTION: 
				qBuilder.setTables(SMS.TABLE_NAME); 
				qBuilder.setProjectionMap(smsProjectionMap);  
				break;  
			case SMSINFO_COLLECTION_ID: 
				qBuilder.setTables(SMS.TABLE_NAME); 
				qBuilder.setProjectionMap(smsProjectionMap);  
				qBuilder.appendWhere(SMS._ID + "=" + uri.getPathSegments().get(1));  
				break; 
			default: 
				throw new IllegalArgumentException("Unknown url" + uri);  
		}  
		 
		 
		String orderby;  
		if(TextUtils.isEmpty(sortOrder)){ 
			orderby = SMS.DEFAULT_SORT_ORDER;  
		}else{ 
			orderby = sortOrder;  
		}  
		 
		SQLiteDatabase dbDatabase = dbHelper.getReadableDatabase();  
		Cursor cursor=qBuilder.query(dbDatabase, projection, selection, selectionArgs, null, null, orderby); 
		cursor.setNotificationUri(getContext().getContentResolver(), uri); 
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case SMSINFO_COLLECTION:
            count = db.update(SMS.TABLE_NAME, values, selection, selectionArgs);
            break;

        case SMSINFO_COLLECTION_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(SMS.TABLE_NAME, values,
            		SMS.ID + "=" + noteId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                    + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

	}  


}
