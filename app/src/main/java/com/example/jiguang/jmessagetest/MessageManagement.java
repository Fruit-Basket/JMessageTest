package com.example.jiguang.jmessagetest;

import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

/**
 * 消息管理
 */
public class MessageManagement {

    private static final String TAG="MessageManagement";
    private TextView log_tv;

    private MyCallback myCallback=new MyCallback();

    private long groupToSendMessage=Condition.GROUP_2;

    public MessageManagement(TextView log_tv){
        this.log_tv=log_tv;
    }

    /**
     * 消息管理
     * @param command
     */
    public void exe(int command){
        switch(command){
            case 0://发送消息
                sendMessage();
                break;

            case 1://使用会话发送消息
                sendMessageByConversation();
                break;

            case 2://给自己发送消息
                sendMessageToMyself();
                break;

            case 3://获取好友会话中的所有消息
                getAllMessage();
                break;

            case 4://获取所有会话的所有信息
                getAllConversationMessage();
                break;

            case 5:
                log_tv.append("所有未读消息数："+JMessageClient.getAllUnReadMsgCount()+"\n");
                break;

            case 6://对群发送消息
                sendGroupMessage();
                break;

            case 7://删除单聊会话中的最后一条消息
                deleteConversationMessage();
                break;

            case 8://发送带敏感词的自定义消息
                messageTest1();
                break;

            default:
                log_tv.append("messageTest command error! "+command+"\n");
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage(){
        Log.i(TAG,"sendMessage()");
        //构建消息
        Message textMessage= JMessageClient.createSingleTextMessage(
                Condition.friendName,
                //不能给好友发送敏感词
                //"敏感词"+
                "message: to "+Condition.friendName+" from "+Condition.userName
        );
        textMessage.setOnSendCompleteCallback(myCallback);

        Log.i(TAG,"textMessage.getFromUser().getUserName()="+textMessage.getFromUser().getUserName());
        Log.i(TAG,"((UserInfo)textMessage.getTargetInfo()).getUserName()="+((UserInfo)textMessage.getTargetInfo()).getUserName());

        //设置消息发送选项
        MessageSendingOptions options = new MessageSendingOptions();
        options.setShowNotification(true);
        options.setCustomNotificationEnabled(true);
        options.setNotificationTitle("通知标题");
        options.setNotificationText("通知内容");
        options.setNotificationAtPrefix("前缀");

        JMessageClient.sendMessage(textMessage,options);
        //JMessageClient.sendMessage(textMessage);
    }

    /**
     * 使用会话发送消息
     */
    private void sendMessageByConversation(){
        Conversation conversation=Conversation.createSingleConversation(
                Condition.friendName
        );
        CustomContent content = new CustomContent();
        content.setNumberValue("Number",100);

        //创建消息后，消息不会发送给好友，但会保存到本地会话中
        Message message=conversation.createSendMessage(content);
        message.setOnSendCompleteCallback(myCallback);

        JMessageClient.sendMessage(message);//这个语句发送消息
    }

    /**
     * 给自己发送消息
     */
    private void sendMessageToMyself(){
        Log.i(TAG,"sendMessageToMyself");
        //构建消息
        Message message=JMessageClient.createSingleTextMessage(
                Condition.userName,
                "message: to "+Condition.userName+" from "+Condition.userName
        );
        message.setOnSendCompleteCallback(myCallback);

        //打印消息方向
        if(message.getDirect()== MessageDirect.receive){
            Log.i(TAG,"receiver");
        }
        else if(message.getDirect()== MessageDirect.send){
            Log.i(TAG,"send");
        }

        //设置消息发送选项
        MessageSendingOptions optionsMyself = new MessageSendingOptions();
        optionsMyself.setShowNotification(true);

        JMessageClient.sendMessage(message,optionsMyself);
    }

    /**
     * 获取会话中所有消息
     */
    private void getAllMessage(){
        Conversation conversation=JMessageClient.getSingleConversation(Condition.friendName);

        if(conversation==null){
            log_tv.append("会话为空\n");
            return;
        }

        List<Message> MessageList=conversation.getAllMessage();

        StringBuilder stringBuilder=new StringBuilder("会话: "+Condition.userName+" -> "+Condition.friendName +" :\n");
        stringBuilder.append("包含的未读消息数："+conversation.getUnReadMsgCnt()+"\n");
        int i=1;

        for(Message message:MessageList){
            MessageContent messageContent=message.getContent();

            switch(messageContent.getContentType()){
                case text:
                    stringBuilder.append((i++)+": "+((TextContent)messageContent).getText()+"\n");
                    break;

                default:
                    stringBuilder.append((i++)+": 非文本消息\n");
            }
        }

        log_tv.append(stringBuilder.toString());
    }

    /**
     * 获取所有会话的所有消息
     */
    private void getAllConversationMessage(){
        List<Conversation> conversationList=
                JMessageClient.getConversationList();
        StringBuilder stringBuilder=new StringBuilder("All convasation and message:\n");
        for(Conversation conversationTem:conversationList){
            stringBuilder.append("会话: "+conversationTem.getTitle()+"\n");

            //获取单个会话中所有的消息
            List<Message> lists=conversationTem.getAllMessage();
            int i=1;

            for(Message message:lists){
                MessageContent messageContent=message.getContent();

                switch(messageContent.getContentType()){
                    case text:
                        stringBuilder.append((i++)+": "+((TextContent)messageContent).getText()+"\n");
                        break;

                    default:
                        stringBuilder.append((i++)+": 非文本消息\n");
                }
            }

            stringBuilder.append("\n");
        }

        log_tv.append(stringBuilder.toString());
    }

    /**
     * 对群发送消息
     */
    private void sendGroupMessage(){
        Message textMessage=JMessageClient.createGroupTextMessage(
                groupToSendMessage,
                "敏感词"
                );
        textMessage.setOnSendCompleteCallback(myCallback);

        JMessageClient.sendMessage(textMessage);
    }

    /**
     * 删除单聊会话中的最后一条消息
     */
    private void deleteConversationMessage(){
        Log.i(TAG,"deleteConversationMessage()");

        Conversation conversation=Conversation.createSingleConversation(
                Condition.friendName
        );

        Message message=conversation.getLatestMessage();
        if(message!=null){
            conversation.deleteMessage(message.getId());
            log_tv.append("删除会话消息成功\n");
        }
        else{
            log_tv.append("删除失败\n");
        }
    }

    /**
     * 发送带敏感词的自定义消息
     */
    private void messageTest1(){
        Conversation conversation=Conversation.createSingleConversation(
                Condition.friendName//好友的username
        );

        CustomContent content = new CustomContent();
        //content.setStringValue("text","敏感词");//带上这个就发送不成功
        content.setStringValue("content","敏感词");
        content.setStringValue("content_2","自定义消息");

        Map<String,String> map=new HashMap<>();
        map.put("map_content","敏感词");
        map.put("map_content_2","自定义消息");
        content.setAllValues(map);

        Message message=conversation.createSendMessage(content);
        message.setOnSendCompleteCallback(myCallback);//自定义回调

        JMessageClient.sendMessage(message);
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
                    log_tv.append("Messege OK\n");
                }
                else{
                    log_tv.append("Message Feilure\n");
                }
            }

            Log.d(TAG,"Response code= "+responseCode);
            Log.d(TAG,"Response message: "+responseMessage);
        }
    }
}
