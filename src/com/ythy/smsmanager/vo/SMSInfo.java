package com.ythy.smsmanager.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class SMSInfo implements Parcelable{
	/** 
	  * 短信内容 
	  */  
	 private String smsbody;  
	 /** 
	  * 发送短信的电话号码 
	  */  
	 private String phoneNumber;  
	 /** 
	  * 发送短信的日期和时间 
	  */  
	 private String date;  
	 /** 
	  * 发送短信人的姓名 
	  */  
	 private String name;  

	 public String getSmsbody() {  
	     return smsbody;  
	 }  

	 public void setSmsbody(String smsbody) {  
	     this.smsbody = smsbody;  
	 }  

	 public String getPhoneNumber() {  
	     return phoneNumber;  
	 }  

	 public void setPhoneNumber(String phoneNumber) {  
	     this.phoneNumber = phoneNumber;  
	 }  

	 public String getDate() {  
	     return date;  
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public static final Parcelable.Creator<SMSInfo> CREATOR = new Creator<SMSInfo>() {
		
		//此处变量顺序不能错位  需要和 writeToParcel 一直
		@Override
		public SMSInfo createFromParcel(Parcel source) {
			SMSInfo mSMSInfo = new SMSInfo();
			mSMSInfo.smsbody = source.readString();
			mSMSInfo.phoneNumber = source.readString();
			mSMSInfo.date = source.readString();
			mSMSInfo.name = source.readString();
			return mSMSInfo;
		}
		
		@Override
		public SMSInfo[] newArray(int size) {
			return new SMSInfo[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(smsbody);  
		dest.writeString(phoneNumber);  
		dest.writeString(date);  
		dest.writeString(name); 
	} 
}
