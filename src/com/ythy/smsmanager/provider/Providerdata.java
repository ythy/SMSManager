package com.ythy.smsmanager.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Providerdata {

    public static final String AUTHORITY="com.ythy.smsmanager.provider.smsprovider"; 

    //数据库名称 
    public static final String DATABASE_NAME = "SMS.db"; 
    //数据库的版本  
    public static final int DATABASE_VERSION = 1; 
    
    
    public static final class SMS implements BaseColumns{

       //表名
       public static final String TABLE_NAME = "sms_info";

       //访问该ContentProvider的URI
       public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/" + TABLE_NAME);
       
       //新增mimeType  vnd.android.cursor.dir/开头返回多条数据    vnd.android.cursor.item/开头返回单条数据
       public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.ythy.SMS"; 
       public static final String CONTENT_TYPE_ITEM="vnd.android.cursor.item/vnd.ythy.SMS";

       //列名
       public static final String ID = "_id"; 
       public static final String COLUMN_DATE = "date";   
       public static final String COLUMN_SMSBODY = "smsBody";   
       public static final String COLUMN_NUMBER = "phoneNumber"; 
      
       //默认排序方式
       public static final String DEFAULT_SORT_ORDER = "date ASC";

    }

 
}
