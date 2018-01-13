package com.example.lijun.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class RegisterActivity extends AppCompatActivity {
    private Button btn_back;
    private Button btn_reg;
    private Button getCode;
    private EditText name;
    private EditText email;
    private EditText code;
    private EditText password;
    private EditText ensurePassword;
    private String url;
    private String data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //取消注册
        btn_back=(Button)findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        name = (EditText)findViewById(R.id.name);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String nameText = name.getText().toString();
                    if (nameText.equals("")) {

                        TastyToast.makeText(getApplicationContext(), "昵称不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }
                }
            }
        });

        email = (EditText)findViewById(R.id.email);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String emailText = email.getText().toString();
                    if (emailText.equals("")) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "邮箱不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }else {
                        checkEmailRepeat(emailText);//验证邮箱是否重复
                    }
                }
            }
        });

        getCode = (Button) findViewById(R.id.getCode);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                if (emailText.equals("")) {
//                    Toast toast = Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT);
//                    toast.show();
                    TastyToast.makeText(getApplicationContext(), "邮箱不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else {
                    sendCode(emailText);//发送验证码
                }
            }
        });

        code = (EditText)findViewById(R.id.code);
        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String codeText = code.getText().toString();
                    if (codeText.equals("")) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "验证码为6位", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "验证码为6位", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        password = (EditText)findViewById(R.id.password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String pwdText = password.getText().toString();
                    if (pwdText.equals("")) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        ensurePassword = (EditText)findViewById(R.id.ensurePassword);
        ensurePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String ensurePwdText = ensurePassword.getText().toString();
                    String pwdText = password.getText().toString();
                    if (!ensurePwdText.equals(pwdText)) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "两次密码输入不一致", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });
        btn_reg = (Button)findViewById(R.id.reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ensurePwdText = ensurePassword.getText().toString();
                String pwdText = password.getText().toString();
                if (!ensurePwdText.equals(pwdText)) {

                    TastyToast.makeText(getApplicationContext(), "两次密码输入不一致", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }else {
                    String emailText = email.getText().toString();
                    String codeText = code.getText().toString();
                    String nameText = name.getText().toString();
                    regUser(emailText,codeText,pwdText,nameText);//注册


                    /*注册成功跳转到登录*/
//                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
//                    startActivity(intent);
                }
            }
        });
    }

    public void checkEmailRepeat(String emailText){
        url = "http://10.0.2.2:8082/Home/RegApi/checkEmailRepeat";
        try {
            data = "email="+ URLEncoder.encode(emailText, "UTF-8");
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
                    handlerForCheckEmailRepeat.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckEmailRepeat =new Handler(){
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

    public void sendCode(String emailText){
        url = "http://10.0.2.2:8082/Home/RegApi/sendCode";
        try {
            data = "email="+ URLEncoder.encode(emailText, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForSendCode.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForSendCode =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            TastyToast.makeText(getApplicationContext(), "已发送至手机", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            Log.i("22",val);
        }
    };


    public void regUser(  String emailText,String codeText,String pwdText,String nameText){
        url = "http://10.0.2.2:8082/Home/RegApi/checkCode";
        try {
            data = "email="+ URLEncoder.encode(emailText, "UTF-8")+
                    "&password="+ URLEncoder.encode(pwdText, "UTF-8")+
                    "&code="+ URLEncoder.encode(codeText, "UTF-8")+
                    "&name="+ URLEncoder.encode(nameText, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForReg.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForReg =new Handler(){
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
                    Intent it=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(it);
                 //   SysApplication.getInstance().exit();
                    User.uToken = json.optString("token");          //赋值token
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
