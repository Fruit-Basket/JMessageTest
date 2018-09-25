package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

public class OtherTest {

    private static final String TAG="OtherTest";
    private TextView log_tv;

    public OtherTest(TextView log_tv){
        this.log_tv=log_tv;
    }

    public void exe(int command) {
        switch (command) {
            case 0:
                test0();
                break;


            default:

        }
    }

    private void test0(){

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
                new BasicCallback() {

                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        Log.d(TAG,"Response code= "+responseCode);
                        Log.d(TAG,"Response message: "+responseMessage);
                    }
                }
        );
    }
}
