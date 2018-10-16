package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class GroupManagement {
    private static final String TAG="GroupManagement";
    private MyCallback myCallback=new MyCallback();
    private TextView log_tv;

    private long groupId=Condition.GROUP_TEST;//群Id
    private long groupTestId=Condition.GROUP_TEST;

    public GroupManagement(TextView log_tv){
        this.log_tv=log_tv;
    }

    public void exe(int command){
        switch(command) {
            case 0://申请加入群聊
                applyJoinGroup();
                break;

            case 1://退出群聊
                exitGroup();
                break;

            case 2://群主解散群组
                dissolveGroup();
                break;

            case 3://群成员列表
                getGroupMembers();
                break;

            case 4://群列表
                getGroups();
                break;

            case 5://获取群消息
                getGroupInfo();
                break;

            case 6://删除群成员
                removeGroupMembers();
                break;

            default:

        }
    }

    /**
     * 申请入群
     */
    private void applyJoinGroup(){
        JMessageClient.applyJoinGroup(groupId,"group apply from "+Condition.userName,myCallback);
    }

    /**
     * 退出群聊
     */
    private void exitGroup(){
        JMessageClient.exitGroup(groupId,myCallback);
    }

    /**
     * 群主解散群组
     */
    private void dissolveGroup(){
        JMessageClient.adminDissolveGroup(
                groupTestId,//指定群Id
                myCallback
        );
    }

    /**
     * 获取群成员列表
     */
    private void getGroupMembers(){
        JMessageClient.getGroupMembers(
                groupId,//群Id
                new GetGroupMembersCallback(){

                    @Override
                    public void gotResult(int responseCode, String responseMessage, List<UserInfo> list) {
                        Log.d(TAG,"Response code= "+responseCode);
                        Log.d(TAG,"Response message: "+responseMessage);

                        if(log_tv!=null){
                            if(responseCode==0){

                                if(list!=null&&list.size()>0){
                                    StringBuilder stringBuilder=new StringBuilder("群成员列表：\n");
                                    int i=0;
                                    for(UserInfo info:list){
                                        stringBuilder.append("成员"+(++i)+": "+info.getUserName()+"\n");
                                    }
                                    log_tv.append(stringBuilder.toString());
                                    Log.d(TAG,stringBuilder.toString());

                                }
                                else{
                                    log_tv.append("Grouplist=null\n");
                                    Log.d(TAG,"Grouplist=null\n");
                                }
                            }
                            else{
                                log_tv.append("Group Feilure\n");
                            }
                        }
                    }
                }
        );
    }

    /**
     * 获取当前用户所在的群
     */
    private void getGroups(){
        JMessageClient.getGroupIDList(
                new GetGroupIDListCallback(){

                    @Override
                    public void gotResult(int responseCode, String responseMessage, List<Long> list) {
                        if(log_tv!=null){
                            if(responseCode==0){

                                if(list!=null&&list.size()>0){
                                    StringBuilder stringBuilder=new StringBuilder("群成员列表：\n");
                                    int i=0;
                                    for(long groupId:list){
                                        stringBuilder.append("群"+(++i)+": "+groupId+"\n");
                                    }
                                    log_tv.append(stringBuilder.toString());
                                }
                                else{
                                    log_tv.append("Grouplist=null\n");
                                }
                            }
                            else{
                                log_tv.append("Group Feilure\n");
                            }
                        }

                        Log.d(TAG,"Response code= "+responseCode);
                        Log.d(TAG,"Response message: "+responseMessage);
                    }
                }
        );
    }

    /**
     * 查询群信息
     */
    private void getGroupInfo(){
        JMessageClient.getGroupInfo(
                groupId,
                new GetGroupInfoCallback(){

                    @Override
                    public void gotResult(int responseCode, String responseMessage, GroupInfo groupInfo) {
                        if(log_tv!=null){
                            if(responseCode==0){
                                log_tv.append("群名称："+groupInfo.getGroupName()+"\n");
                            }
                            else{
                                log_tv.append("Group Feilure\n");
                            }
                        }

                        Log.d(TAG,"Response code= "+responseCode);
                        Log.d(TAG,"Response message: "+responseMessage);
                    }
                }
        );
    }

    /**
     * 删除群成员
     */
    private void removeGroupMembers(){

        List<String> userNameList=new ArrayList<>();
        userNameList.add(Condition.friendName);

        JMessageClient.removeGroupMembers(
                groupId,
                userNameList,
                myCallback
        );
    }


    private void test(){
        //JMessageClient
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
                    log_tv.append("Group OK\n");
                }
                else{
                    log_tv.append("Group Feilure\n");
                }
            }

            Log.d(TAG,"Response code= "+responseCode);
            Log.d(TAG,"Response message: "+responseMessage);
        }
    }
}
