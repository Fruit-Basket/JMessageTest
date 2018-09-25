package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class ConversationManagement {

    private static final String TAG="ConversationManagement";
    private MyCallback myCallback=new MyCallback();
    private TextView log_tv;

    private long groupToChat=Condition.GROUP_2;//创建群聊会话
    private long groupConversationToDelete=Condition.GROUP_2;//删除群聊会话

    public ConversationManagement(TextView log_tv){
        this.log_tv=log_tv;
    }

    /**
     * 会话管理
     * @param command
     */
    public void exe(int command){
        boolean status;

        switch(command) {
            case 0://创建单聊会话
                createSingleConversation();
                break;

            case 1://创建群聊会话
                createGroupConversation();
                break;

            case 2://查询所有会话
                getAllConversation();
                break;

            case 3://删除单聊会话
                status=JMessageClient.deleteSingleConversation(Condition.friendName);
                if(status==true){
                    log_tv.append("delete OK\n");
                }
                else{
                    log_tv.append("delete feilure\n");
                }
                break;

            case 4://删除群聊会话
                status=JMessageClient.deleteGroupConversation(Condition.GROUP_2);
                if(status==true){
                    log_tv.append("delete OK\n");
                }
                else{
                    log_tv.append("delete feilure\n");
                }
                break;

            case 5://单个会话的未读消息总数
                int unReadMsgCnt=JMessageClient
                        .getSingleConversation(Condition.friendName)
                        .getUnReadMsgCnt();
                log_tv.append("unReadMsgCnt="+unReadMsgCnt+"\n");
                break;

            case 6://重置会话未读消息数
                JMessageClient
                        .getSingleConversation(Condition.friendName)
                        .resetUnreadCount();
                break;

            case 7://设置单个会话的未读消息数
                if(JMessageClient
                        .getSingleConversation(Condition.friendName)
                        .setUnReadMessageCnt(5)==true){
                    log_tv.append("OK\n");
                }
                else{
                    log_tv.append("Feilure\n");
                }
                break;



            default:
                log_tv.append("conversationTest command error!\n");
        }
    }

    /**
     * 查询所有会话
     */
    private void getAllConversation(){
        List<Conversation> lists=
                JMessageClient.getConversationList();
        StringBuilder stringBuilder=new StringBuilder("All conversation:\n");
        int i=1;
        for(Conversation conversation:lists){
            stringBuilder.append((i++)+": "+conversation.getTitle()+"\n");

            //获取会话对象中的群ID
            if(conversation.getTargetInfo()!=null){
                if(conversation.getTargetInfo() instanceof GroupInfo){
                    GroupInfo groupInfo=(GroupInfo)conversation.getTargetInfo();
                    groupInfo.getGroupID();//获取群ID
                }
                else{
                    Log.i(TAG,"conversation.getTargetInfo() instanceof GroupInfo==false");
                }
            }
            else{
                Log.w(TAG,"conversation.getTargetInfo()==null");
            }

        }
        log_tv.append(stringBuilder.toString());
    }

    /**
     * 获取创建单聊会话
     */
    private void createSingleConversation(){
        Log.i(TAG,"createSingleConversation()");

        Conversation conversation;
        UserInfo userInfo;

        conversation=JMessageClient.getSingleConversation(
                Condition.friendName//用户的username
        );

        if(conversation==null){
            Log.i(TAG,"conversation==null");
            conversation=Conversation.createSingleConversation(
                    Condition.friendName);
            /*
            如果利用Conversation.createSingleConversation()创建新会话时，要下载好友的信息，所以立即conversation.getTargetInfo()会得不到所有的好友信息，因为好友的信息还没完全下载下来
            */
        }
        else{
            Log.i(TAG,"conversation!=null");
        }

        userInfo=(UserInfo)conversation.getTargetInfo();
        if(userInfo!=null){
            Log.i(TAG,"friend's name="+userInfo.getUserName());
            Log.i(TAG,"signiture="+userInfo.getSignature());
        }
        else{
            Log.i(TAG,"userInfo==null");
        }

        Log.i(TAG,"friend's info="+userInfo.toString());
    }

    /**
     * 创建群聊会话
     */
    private void createGroupConversation(){
        Log.i(TAG,"createGroupConversation()");

        Conversation conversation;

        conversation=JMessageClient.getGroupConversation(
                Condition.GROUP_2//群ID
        );
        if(conversation==null){
            conversation=Conversation.createGroupConversation(Condition.GROUP_2);
        }
        else{
            Log.i(TAG,"conversation!=null");
        }

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
                    log_tv.append("Conversation OK\n");
                }
                else{
                    log_tv.append("Conversation Feilure\n");
                }
            }

            Log.d(TAG,"Response code= "+responseCode);
            Log.d(TAG,"Response message: "+responseMessage);
        }
    }

}
