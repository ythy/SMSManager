package com.ythy.smsmanager.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Providerdata {

    public static final String AUTHORITY="com.ythy.smsmanager.provider.smsprovider"; 

    //���ݿ����� 
    public static final String DATABASE_NAME = "SMS.db"; 
    //���ݿ�İ汾  
    public static final int DATABASE_VERSION = 1; 
    
    
    public static final class SMS implements BaseColumns{

       //����
       public static final String TABLE_NAME = "sms_info";

       //���ʸ�ContentProvider��URI
       public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/" + TABLE_NAME);
       
       //����mimeType  vnd.android.cursor.dir/��ͷ���ض�������    vnd.android.cursor.item/��ͷ���ص�������
       public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.ythy.SMS"; 
       public static final String CONTENT_TYPE_ITEM="vnd.android.cursor.item/vnd.ythy.SMS";

       //����
       public static final String ID = "_id"; 
       public static final String COLUMN_DATE = "date";   
       public static final String COLUMN_SMSBODY = "smsBody";   
       public static final String COLUMN_NUMBER = "phoneNumber"; 
      
       //Ĭ������ʽ
       public static final String DEFAULT_SORT_ORDER = "date ASC";

    }

 
}
