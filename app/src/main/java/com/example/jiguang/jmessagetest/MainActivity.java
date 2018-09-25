package com.example.jiguang.jmessagetest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import cn.jpush.im.android.api.JMessageClient;


public class MainActivity extends Activity {

    private static final String TAG="MainActivity";

    private Button login_b;

    private Spinner user_s;
    private Spinner other_user_s;

    private Button logout_b;
    private Button get_user_info_b;

    private Button user_management_b;
    private Spinner user_management_command_s;

    private Button group_management_b;
    private Spinner group_command_s;

    private Button convsersation_test_b;
    private Spinner conversation_command_s;

    private Button message_test_b;
    private Spinner message_command_s;

    private Button friend_test_b;
    private Spinner friend_command_s
            ;
    private Button test_b;
    private EditText test_e;

    private TextView log_tv;

    private EventHanlder eventHanlder;//事件处理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate()");
        setContentView(R.layout.activity_main);

        initViews();

        //注册事件监听
        eventHanlder=new EventHanlder(log_tv);
        JMessageClient.registerEventReceiver(eventHanlder);

    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(eventHanlder);//解注册

        Log.i(TAG,"onDestory()");
        super.onDestroy();
    }


    /**
     * 初始化界面控件
     */
    private void initViews(){
        Log.i(TAG,"initViews()");

        //先初始化log_tv
        log_tv=(TextView)findViewById(R.id.log_tv);
        View.OnClickListener listener=new MyOnClickListener();

        login_b=(Button)findViewById(R.id.login_b);
        login_b.setOnClickListener(listener);

        //用户选择
        user_s=(Spinner)findViewById(R.id.user_s);
        user_s.setSelection(0,true);
        user_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Log.i(TAG,"onItemSelected() my user:"+pos);
                Condition.setMyUser(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //好友选择
        other_user_s=(Spinner)findViewById(R.id.other_user_s);
        other_user_s.setSelection(0,true);
        other_user_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Log.i(TAG,"onItemSelected() other user:"+pos);
                Condition.setFriend(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        logout_b=(Button)findViewById(R.id.logout_b);
        logout_b.setOnClickListener(listener);

        get_user_info_b=(Button)findViewById(R.id.get_user_info_b);
        get_user_info_b.setOnClickListener(listener);

        //用户管理
        user_management_b=(Button)findViewById(R.id.user_management_b);
        user_management_b.setOnClickListener(listener);
        user_management_command_s=(Spinner)findViewById(R.id.user_management_command_s);
        user_management_command_s.setSelection(0,true);

        //群组管理
        group_management_b=(Button)findViewById(R.id.group_management_b);
        group_management_b.setOnClickListener(listener);
        group_command_s=(Spinner)findViewById(R.id.group_management_command_s);
        group_command_s.setSelection(0,true);

        //会话管理
        convsersation_test_b =(Button)findViewById(R.id.conversation_test_b);
        convsersation_test_b.setOnClickListener(listener);
        conversation_command_s=(Spinner)findViewById(R.id.conversation_command_s);
        conversation_command_s.setSelection(0,true);

        //消息管理
        message_test_b=(Button)findViewById(R.id.message_test_b);
        message_test_b.setOnClickListener(listener);
        message_command_s=(Spinner)findViewById(R.id.message_command_s);
        message_command_s.setSelection(0,true);

        //好友管理
        friend_test_b=(Button)findViewById(R.id.friend_test_b);
        friend_test_b.setOnClickListener(listener);
        friend_command_s=(Spinner)findViewById(R.id.friend_command_s);
        friend_command_s.setSelection(0,true);

        test_b=(Button)findViewById(R.id.test_b);
        test_b.setOnClickListener(listener);
        test_e=(EditText)findViewById(R.id.test_e);
    }

    /**
     * 按钮点击监听
     */
    private class MyOnClickListener implements View.OnClickListener {

        UserManagement userManagement=new UserManagement(log_tv);

        GroupManagement groupManagement=new GroupManagement(log_tv);

        ConversationManagement conversationManagement=
                new ConversationManagement(
                        log_tv
                );

        MessageManagement messageManagement=
                new MessageManagement(
                        log_tv
                );

        FriendManagement friendManagement=
                new FriendManagement(
                        log_tv
                );

        OtherTest otherTest=new OtherTest(log_tv);


        @Override
        public void onClick(View view) {
            String command;

            Log.i(TAG, "onClick(View)");
            switch (view.getId()) {
                case R.id.login_b:
                    userManagement.exe(1);
                    break;

                case R.id.logout_b:
                    userManagement.exe(3);
                    break;

                case R.id.get_user_info_b:
                    userManagement.exe(4);
                    break;

                case R.id.user_management_b:
                    userManagement.exe(
                            conversation_command_s.getSelectedItemPosition()
                    );
                    break;

                case R.id.group_management_b:
                    groupManagement.exe(
                            group_command_s.getSelectedItemPosition()
                    );
                    break;

                case R.id.conversation_test_b:
                    conversationManagement.exe(
                            conversation_command_s.getSelectedItemPosition()
                    );

                    break;

                case R.id.message_test_b:
                    messageManagement.exe(
                            message_command_s.getSelectedItemPosition()
                    );
                    break;

                case R.id.friend_test_b:
                    friendManagement.exe(
                            friend_command_s.getSelectedItemPosition()
                    );

                    break;

                case R.id.test_b:

                    command=test_e.getText().toString().trim();
                    if(command!=null&&TextUtils.isEmpty(command)==false){
                        otherTest.exe(Integer.parseInt(command));
                    }

                    break;

                default:
                    Log.e(TAG,"view.getId() no case");

            }
        }

    }
}
