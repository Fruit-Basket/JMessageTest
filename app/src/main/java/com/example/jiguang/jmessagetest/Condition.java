package com.example.jiguang.jmessagetest;


import android.util.Log;

public class Condition {
	private static final Condition instance=new Condition();
	private static final String TAG="Condition";

	public static final String APP_KEKY="6405d90516a86262e2f52dad";

	private static final String USER_NAME_1 ="zhouxj1";
	private static final String PASSWORD_1 ="123456";

	private static final String USER_NAME_2="zhouxj2";
	private static final String PASSWORD_2="123456";

	private static final String USER_NAME_3="zhouxj3";
	private static final String PASSWORD_3="123456";

	private static final String USER_NAME_4="zhouxj4";
	private static final String PASSWORD_4="123456";

	//当前用户
	public static String userName=USER_NAME_1;
	public static String userPassword=PASSWORD_1;

	//好友用户
	public static String friendName =USER_NAME_1;
	public static String friendPassword =USER_NAME_1;

	//群ID
	public static final long GROUP_1=26886409;//私有
	public static final long GROUP_2=26886413;//公有
	public static final long GROUP_TEST=28457901;
	
	private Condition() {}
	
	public Condition getInstance() {
		return instance;
	}


	/**
	 * 设置当前用户名和密码
	 * @param command
	 */
	public static void setMyUser(int command){
		if(command==0){
			userName=Condition.USER_NAME_1;
			userPassword =Condition.PASSWORD_1;
		}
		else if(command==1){
			userName=Condition.USER_NAME_2;
			userPassword =Condition.PASSWORD_2;
		}
		else if(command==2){
			userName=Condition.USER_NAME_3;
			userPassword =Condition.PASSWORD_3;
		}
		else if(command==3){
			userName=Condition.USER_NAME_4;
			userPassword =Condition.PASSWORD_4;
		}
		else{
			Log.w(TAG,"user command error");
		}
	}

	/**
	 * 设置好友用户的名字和密码
	 * @param command
	 */
	public static void setFriend(int command){
		if(command==0){
			friendName =Condition.USER_NAME_1;
			friendPassword =Condition.PASSWORD_1;
		}
		else if(command==1){
			friendName =Condition.USER_NAME_2;
			friendPassword =Condition.PASSWORD_2;
		}
		else if(command==2){
			friendName =Condition.USER_NAME_3;
			friendPassword =Condition.PASSWORD_3;
		}
		else if(command==3){
			friendName =Condition.USER_NAME_4;
			friendPassword =Condition.PASSWORD_4;
		}
		else{
			Log.w(TAG,"user command error");
		}
	}

}
