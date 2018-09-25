package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.DeviceInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 用户管理
 */
public class UserManagement {
    private static final String TAG="UserManager";

    private TextView log_tv;
    private MyCallback myCallback=new MyCallback();

    public UserManagement(TextView log_tv){
        this.log_tv=log_tv;
    }

    public void exe(int command){
        switch(command){
            case 0:
                registration();
                break;

            case 1:
                login();
                break;

            case 2:
                loginGetDeviceInfo();
                break;

            case 3:
                logout();
                break;

            case 4:
                getUserInfo(1);
                break;

            case 5:
                updateInfo();
                break;

            case 6:
                updateAvatar();
                break;

            case 7:
                updatePassword();
                break;

            default:
        }
    }

    /**
     * 注册
     */
    private void registration(){

        RegisterOptionalUserInfo info=new RegisterOptionalUserInfo();
        info.setSignature("signature");
        info.setAddress("address");
        info.setAvatar("avater");
        info.setBirthday(0621l);
        info.setGender(UserInfo.Gender.female);
        info.setNickname("nickname");
        info.setRegion("region");

        JMessageClient.register(
                Condition.userName,
                Condition.userPassword,
                info,
                myCallback
        );
    }

    /**
     * 用户登录
     */
    private void login(){
        JMessageClient.login(
                Condition.userName,
                Condition.userPassword,
                myCallback
        );
    }

    /**
     * 用户登陆，并且在回调中获取用户账号所登陆过的设备信息
     */
    private void loginGetDeviceInfo() {

        RequestCallback<List<DeviceInfo>> requestCallback=new RequestCallback<java.util.List<DeviceInfo>>(){

            @Override
            public void gotResult(int responseCode, String responseMessage, List<DeviceInfo> deviceInfos) {

                if(responseCode==0){
                    log_tv.append("RequestCallback OK\n");
                }
                else{
                    log_tv.append("RequestCallback Feilure\n");
                }

                StringBuilder stringBuilder=new StringBuilder("登录过的设备：\n\n");

                for(DeviceInfo deviceInfo:deviceInfos){
                    stringBuilder.append("平台："+deviceInfo.getPlatformType()+"\n");
                    stringBuilder.append("在线状态（1->在线，0->不在线）："+deviceInfo.getOnlineStatus()+"\n");
                    stringBuilder.append("该设备是否别当前设备踢下线（1->是，0->否）："+deviceInfo.getFlag()+"\n");
                    stringBuilder.append("\n");
                }

                log_tv.append(stringBuilder.toString());
            }
        };

        JMessageClient.login(
                Condition.userName,
                Condition.userPassword,
                requestCallback
        );

    }

    /**
     * 退出登录
     */
    private void logout(){
        JMessageClient.logout();
        log_tv.append("log out\n");
    }



    /**
     * 获取用户信息
     * @param command 1->当前用户信息；2->同一应用内特定的用户信息；3->别的应用的特定用户信息
     */
    private void getUserInfo(int command){

        //构建回调类
        GetUserInfoCallback getUserInfoCallback=new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo userInfo) {

                if(responseCode==0){
                    log_tv.append("GetUserInfoCallback OK\n");
                }
                else{
                    log_tv.append("GetUserInfoCallback Feilure\n");
                }

                StringBuilder stringBuilder=new StringBuilder();
                if(userInfo!=null){
                    stringBuilder.append("用户信息：\n");
                    stringBuilder.append("user name: "+userInfo.getUserName()+"\n");
                    stringBuilder.append("id: "+userInfo.getUserID()+"\n");
                    stringBuilder.append("signature:"+userInfo.getSignature()+"\n");
                    stringBuilder.append("gender: "+userInfo.getGender()+"\n");
                }
                else{
                    stringBuilder.append("无此用户\n");
                }
                log_tv.append(stringBuilder.toString());
            }
        };

        if(command==2){//同一个应用
            JMessageClient.getUserInfo(
                    Condition.userName,
                    getUserInfoCallback
            );

        }
        else if(command==3){//跨应用
            //JMessageClient.getUserInfo(String username, String appKey, GetUserInfoCallback callback);
        }
        else{//自己的用户信息
            UserInfo userInfo=JMessageClient.getMyInfo();

            StringBuilder stringBuilder=new StringBuilder("用户信息：\n");
            stringBuilder.append("user name: "+userInfo.getUserName()+"\n");
            stringBuilder.append("user id: "+userInfo.getUserID()+"\n");
            stringBuilder.append("signature:"+userInfo.getSignature()+"\n");
            stringBuilder.append("user gender: "+userInfo.getGender()+"\n");

            log_tv.append(stringBuilder.toString());
        }

    }

    /**
     *更新当前用户的信息
     */
    private void updateInfo(){///

        UserInfo info=UserInfo.fromJson(
                "{\n" +
                        "\"nickname\":\"Xuejin Zhou\",\n" +
                        "\"birthday\":\"0621\",\n" +
                        "\"signature\":\"hello world\",\n" +
                        "\"gender\":\"female\",\n" +
                        "\"region\":\"China\",\n" +
                        "\"address\":\"Shenzhen\"\n" +
                        "}"
        );

        JMessageClient.updateMyInfo(
                UserInfo.Field.all,
                info,
                myCallback);
    }

    /**
     * 更新用户头像
     */
    private void updateAvatar(){
        ///
        File avatar=null;
        JMessageClient.updateUserAvatar(avatar, myCallback);
    }

    /**
     * 更新用户密码
     */
    private void updatePassword(){
        JMessageClient.updateUserPassword(
                Condition.userPassword,
                Condition.userPassword,
                myCallback
        );

    }


    /**
     * 自定义回调类
     */
    private class MyCallback extends BasicCallback {

        private static final String TAG = "MyCallback";

        @Override
        public void gotResult(int responseCode, String responseMessage) {
            Log.i(TAG,"gotResult()");

            if(log_tv!=null){
                if(responseCode==0){
                    log_tv.append("User OK\n");
                }
                else{
                    log_tv.append("User Feilure\n");
                }
            }

            Log.d(TAG,"Response code= "+responseCode);
            Log.d(TAG,"Response message: "+responseMessage);
        }
    }
}
