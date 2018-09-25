package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetBlacklistCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 好友管理
 */
public class FriendManagement {

    private static final String TAG="FriendManagement";

    private TextView log_tv;
    private MyCallback myCallback=new MyCallback();

    public FriendManagement(TextView log_tv){
        this.log_tv=log_tv;
    }

    /**
     * 好友测试
     * @param command
     */
    public void exe(int command){
        switch(command){
            case 0://发送添加好友请求
                sendFriendRequest();
                break;

            case 1://接受好友请求
                acceptRequest();
                break;

            case 2://好友列表
                getFriendList();
                break;

            case 3://删除好友
                removeFromFriendList();
                break;

            case 4://加入黑名单
                addUsersToBlacklist();
                break;

            case 5://黑名单列表
                getBlacklist();
                break;

            default:
        }
    }

    /**
     * 发送好友请求
     */
    private void sendFriendRequest(){
        ContactManager.sendInvitationRequest(
                Condition.friendName,
                Condition.APP_KEKY,
                "我是XXX",
                myCallback
        );
    }

    /**
     * 接受好友请求
     */
    private void acceptRequest(){
        log_tv.append("接受好友请求：");
        ContactManager.acceptInvitation(Condition.friendName,Condition.APP_KEKY,myCallback);
    }

    /**
     * 好友列表
     */
    private void getFriendList(){
        Log.i(TAG,"getFriendList()");

        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> list) {
                Log.i(TAG,"gotResult()");

                if(log_tv!=null){
                    if(responseCode==0){

                        StringBuilder stringBuilder=new StringBuilder("共"+list.size()+"位好友：\n");
                        int i=1;
                        for(UserInfo userInfo:list){
                            stringBuilder.append((i++)+": "+userInfo.getUserName()+"\n");
                        }
                        log_tv.append(stringBuilder.toString());
                    }
                    else{
                        log_tv.append("Friend Feilure\n");
                    }
                }

                Log.d(TAG,"Response code= "+responseCode);
                Log.d(TAG,"Response message: "+responseMessage);
            }
        });
    }

    /**
     * 删除好友
     */
    private void removeFromFriendList(){
        Log.i(TAG,"removeFromFriendList()");

        ///
        GetUserInfoCallback getUserInfoCallback=new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo userInfo) {

                if(responseCode==0){
                    Log.i(TAG,"GetUserInfoCallback OK\n");

                    userInfo.removeFromFriendList(myCallback);//真正删除好友的地方，在回调用查看是否确实删除成功
                }
                else{
                    Log.i(TAG,"GetUserInfoCallback Feilure\n");
                }
            }
        };

        JMessageClient.getUserInfo(
                Condition.friendName,//要删除的好友
                getUserInfoCallback
        );
    }

    /**
     * 把用户添加到黑名单中
     */
    private void addUsersToBlacklist(){

        List<String> userNames=new ArrayList<>();
        userNames.add(Condition.friendName);

        JMessageClient.addUsersToBlacklist(
                userNames,
                myCallback
        );
    }

    private void getBlacklist(){
        JMessageClient.getBlacklist(new GetBlacklistCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> list) {
                if(responseCode==0){
                    StringBuilder stringBuilder=new StringBuilder("共"+list.size()+"个黑名单：\n");

                    for(UserInfo userInfo:list){
                        stringBuilder.append(userInfo.getUserName()+"\n");
                    }
                    log_tv.append(stringBuilder.toString());
                }
                else{
                    log_tv.append("获取黑名单失败\n");
                }
            }
        });
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
                    log_tv.append("Friend OK\n");
                }
                else{
                    log_tv.append("Friend Feilure\n");
                }
            }

            Log.d(TAG,"Response code= "+responseCode);
            Log.d(TAG,"Response message: "+responseMessage);
        }
    }
}
