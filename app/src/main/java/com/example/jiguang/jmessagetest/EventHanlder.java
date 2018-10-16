package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import java.util.List;

import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.GroupApprovalEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MyInfoUpdatedEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * 事件处理
 */
public class EventHanlder {
    private static final String TAG="EventHanlder";

    private TextView log_tv;

    public EventHanlder(TextView log_tv){
        this.log_tv=log_tv;
    }

    /**
     * 在线消息事件
     * @param event
     */
    public void onEvent(MessageEvent event) {
        Log.i(TAG, "onEvent(MessageEvent):在线消息事件");

        Message message = ((MessageEvent)event).getMessage();
        StringBuilder stringBuilder = new StringBuilder("收到消息：\n");

        messageHandle(message,stringBuilder);

        log_tv.append(stringBuilder.toString());
        Log.i(TAG,stringBuilder.toString());

    }

    /**
     * 离线消息事件
     **/
    public void onEvent(OfflineMessageEvent event) {
        Log.i(TAG,"onEvent(OfflineMessageEvent)：离线消息事件");
        //获取事件发生的会话对象
        Conversation conversation = ((OfflineMessageEvent)event).getConversation();
        List<Message> newMessageList = ((OfflineMessageEvent)event).getOfflineMessageList();//获取此次离线期间会话收到的新消息列表

        StringBuilder stringBuilder=new StringBuilder("收到离线消息"+newMessageList.size()+"条:\n");

        for(Message message:newMessageList){
            messageHandle(message,stringBuilder);
        }

        Log.i(TAG,stringBuilder.toString());
        log_tv.append(stringBuilder.toString());
    }

    /**
     * 处理好友事件
     * @param event
     */
    public void onEvent(ContactNotifyEvent event) {
        Log.i(TAG,"onEvent(ContactNotifyEvent)：好友事件");

        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                log_tv.append("收到添加好友请求："+fromUsername+", 理由："+reason+"\n");
                break;
            case invite_accepted://对方接收了你的好友邀请
                log_tv.append("对方接受了你的好友请求\n");
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                log_tv.append("对方拒绝了你的请求\n");
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                log_tv.append("对方将你删掉："+fromUsername+"\n");
                break;
            case contact_updated_by_dev_api:
                log_tv.append("收到开发者API更新好友事件\n");
                break;
            default:
                log_tv.append("收到未知好友事件\n");
                break;
        }

        log_tv.append("来自用户："+fromUsername+", 理由："+reason+"\n");
    }

    public void onEvent(MyInfoUpdatedEvent event){
        Log.i(TAG,"onEvent(MyInfoUpdatedEvent):用户信息更新事件");
        log_tv.append("用户消息已新\n");
    }

    /**
     * 会话刷新事件
     * @param event
     */
    public void onEvent(ConversationRefreshEvent event){
        Log.i(TAG,"onEvent(ConversationRefreshEvent)：会话刷新事件");
        log_tv.append("会话刷新事件\n");

        Log.i(TAG,"会话好友消息："+event.getConversation().getTargetInfo().toString());

        log_tv.append("理由："+event.getReason().name());
        Log.i(TAG,"理由："+event.getReason().name());
    }

    /**
     * 群审批事件
     * @param groupApprovalEvent
     */
    public void onEvent(GroupApprovalEvent groupApprovalEvent){
        Log.i(TAG,"onEvent(GroupApprovalEvent)：群审批事件");
        Log.i(TAG,groupApprovalEvent.getType().name());
        Log.i(TAG,groupApprovalEvent.getFromUsername());

        log_tv.append("群审批事件\n");

        //直接拒绝入群
        /*groupApprovalEvent.refuseGroupApproval(groupApprovalEvent.getFromUsername(), groupApprovalEvent.getfromUserAppKey(), "", new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if(log_tv!=null){
                    if(responseCode==0){
                        Log.d(TAG,"拒绝入群成功");
                    }
                    else{
                        Log.d(TAG,"拒绝入群失败");
                    }
                }
                Log.d(TAG,"Response code= "+responseCode);
                Log.d(TAG,"Response message: "+responseMessage);
            }
        });*/

        //直接同意入群
        groupApprovalEvent.acceptGroupApproval(groupApprovalEvent.getFromUsername(), groupApprovalEvent.getfromUserAppKey(), new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        if(log_tv!=null){
                            if(responseCode==0){
                                Log.d(TAG,"入群成功");
                            }
                            else{
                                Log.d(TAG,"入群失败");
                            }
                        }
                        Log.d(TAG,"Response code= "+responseCode);
                        Log.d(TAG,"Response message: "+responseMessage);
                    }
                }
        );

    }

    /**
     * 消息处理
     * @param message
     * @param stringBuilder
     */
    private void messageHandle(Message message, StringBuilder stringBuilder ){
        Log.i(TAG,"messageHandler(Message,StringBuilder)");
        if(message==null||stringBuilder==null){
            Log.w(TAG,"message==null||stringBuilder==null");
            return;
        }

        Log.i(TAG,"mesasge.getId()="+message.getId());
        stringBuilder.append("发送方username：" + message.getFromUser().getUserName() + "\n");
        //stringBuilder.append("发送方NickName：" + message.getFromUser().getNickname() + "\n");


        if(message.getTargetInfo()!=null){
            Log.i(TAG,"msg.getTargetInfo()!=null");
            //GroupInfo info=(GroupInfo)message.getTargetInfo();
            //Log.i(TAG,"gourp id="+info.getGroupID());
        }
        else{
            Log.i(TAG,"msg.getTargetInfo()==null");
        }

        stringBuilder.append("内容：");


        switch (message.getContentType()){
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) message.getContent();
                stringBuilder.append(textContent.getText()+"\n");

                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) message.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                stringBuilder.append("图片消息\n");
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) message.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                stringBuilder.append("语音消息\n");
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) message.getContent();
                stringBuilder.append("自定义消息\n");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent)message.getContent();

                switch (eventNotificationContent.getEventNotificationType()){
                    case group_member_added:
                        //群成员加群事件

                        stringBuilder.append("群成员加群事件\n");

                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        stringBuilder.append("群成员被踢事件\n");
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        stringBuilder.append("群成员退群事件\n");
                        break;
                    case group_info_updated://since 2.2.1
                        //群信息变更事件
                        stringBuilder.append("群信息变更事件\n");
                        break;

                    case group_dissolved:
                        stringBuilder.append("群解散\n");
                        break;

                    default:
                        stringBuilder.append("未知事件\n");
                }

                Log.i(TAG,"Event Text:"+eventNotificationContent.getEventText());
                Log.i(TAG,"User Name:"+eventNotificationContent.getUserNames());
                Log.i(TAG,"Json: "+eventNotificationContent.toJson());
                Log.i(TAG,eventNotificationContent.getStringExtras().toString());
                Log.i(TAG,eventNotificationContent.getBooleanExtras().toString());
                Log.i(TAG,eventNotificationContent.getNumberExtras().toString());

                break;
            default:
                stringBuilder.append("未知消息类型\n");
        }
    }

}
