package com.example.lijun.help.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lijun.help.R;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lijun on 2017/12/23.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText user;
    private EditText password;
    private Button login;
    private Button forget;
    private Button register;
    private String url;
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user = (EditText) findViewById(R.id.num);
        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String userName = user.getText().toString();
                    if (userName.equals("")) {
                        TastyToast.makeText(getApplicationContext(), "用户名不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }else {
                     //   checkNameAndEmail(userName);//用户名检测
                    }
                }
            }
        });

        password = (EditText) findViewById(R.id.pass);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b==false){
                    String pwd = password.getText().toString();
                    if (pwd.equals("")) {
                        TastyToast.makeText(getApplicationContext(), "密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user.getText().toString();
                String pwd = password.getText().toString();
                if (userName.equals("")){
                    TastyToast.makeText(getApplicationContext(), "用户名不能为空", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else if (pwd.equals("")){
                    TastyToast.makeText(getApplicationContext(), "密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else {
                    checkPassAndName(userName,pwd);// 用户名和密码检测
                }
            }
        });

        forget = (Button) findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this,ForgetPasActivity.class);
                startActivity(it);
                finish();
            }
        });

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
    public void checkPassAndName(String username,String pwd){
        url = "http://10.0.2.2:8082/Home/LogApi/Login";
        try {
            data = "login="+ URLEncoder.encode(username, "UTF-8")+
                    "&password="+URLEncoder.encode(pwd, "UTF-8");
             Log.i("mes",data);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCheckPwdAndName.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckPwdAndName =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("success")!=""){
                    Log.i("22",json.optString("success"));
                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent it=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(it);
                    User.uToken = json.optString("token");          //赋值token
                    finish();
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };
}
