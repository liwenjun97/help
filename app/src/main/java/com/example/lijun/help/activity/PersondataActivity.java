package com.example.lijun.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lijun.help.R;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;
import com.example.lijun.help.util.Screen;
import com.example.lijun.help.util.head_pic_choice;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lijun on 2017/12/24.
 */

public class PersondataActivity extends AppCompatActivity {
    private ImageView head_picture;
    private Button save;
    private Button chagepass;
    private ImageView imageView;
    private Button back;
    int width,height,left,right,top,bottom;
    private EditText name;
    private TextView sex;
    private TextView age;
    private TextView studentid;
    private TextView email;
    private String urlForChangeUser;
    private  String dataForChangeUser;
    private  String urlForGetUserInfo;
    private  String dataForGetUserInfo;





    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_data);
        //保证更改的信息得到刷新
        getUserInfo(User.uToken);


//        name = (EditText)findViewById(R.id.name);
//        sex=(TextView)findViewById(R.id.sex);
//        age=(TextView)findViewById(R.id.age);
//        email=(TextView)findViewById(R.id.email);
//        studentid=(TextView)findViewById(R.id.studentid);
//
//        name.setText(User.uName);
//        age.setText(String.valueOf(User.uAge));
//        email.setText(User.uEmail);
//        studentid.setText(User.ustudentid);
//        String sexStr = ((User.uSex==0) ? "女" : (User.uSex==2)? "其他" : "男");
//        sex.setText(sexStr);

        //选择头像
        head_picture =(ImageView)findViewById(R.id.head_picture);
        head_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersondataActivity.this,head_pic_choice.class));
            }
        });

        //点击保存按钮
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname=name.getText().toString();
                String ucity="";//没用到城市
                changeUserInfo(User.uToken,uname,ucity);
                //保存到服务器
            }
        });
        //点击修改密码 跳转到修改密码
        chagepass=(Button)findViewById(R.id.modify);
        chagepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersondataActivity.this,Changepass.class);
                startActivity(intent);
            }
        });
        //点击返回
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this.onDestroy();
                finish();
            }
        });
//
//        //设置头像大小和位置
//        width = (int) Screen.SCREEN_WIDTH/4;
//        height = (int) Screen.SCREEN_HEIGHT/7;
//        left = (int) Screen.SCREEN_WIDTH *3/8;
//        top = (int) Screen.SCREEN_HEIGHT *7 /30;
//        RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
//        layout.setMargins(left,top,0,0);
//
//        layout.width = width;
//        layout.height = height;
//
//        imageView.setLayoutParams(layout);
    }
    public void changeUserInfo(String token,String uname,String ucity){
        urlForChangeUser = "http://10.0.2.2:8082/Home/UserApi/changeUserInfo";
        try {
            dataForChangeUser = "name="+ URLEncoder.encode(uname, "UTF-8")+
                    "&city="+ URLEncoder.encode(ucity, "UTF-8")+
                    "&token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForChangeUser, dataForChangeUser);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForChangeUser.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForChangeUser =new Handler(){
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
                    new SweetAlertDialog(PersondataActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(json.optString("success"))
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            })
                            .show();

                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };
    public void getUserInfo(String token) {
        urlForGetUserInfo = "http://10.0.2.2:8082/Home/UserApi/getUserInfo";

        try {
            dataForGetUserInfo = "token=" + URLEncoder.encode(User.uToken, "UTF-8");
            Log.i("mes",dataForGetUserInfo);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetUserInfo, dataForGetUserInfo);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetUserInfo.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForGetUserInfo = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();
                Log.i("23", val);
                if (json.optString("success") != "") {
                    Log.i("22", json.optString("success"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    //设置到本地并且缓存到本地数据库
                    User.setUserInfo(json.optInt("id"), json.optString("name"), json.optString("email"), json.optString("password"), json.optString("regtime"), json.optString("imgsrc"), json.optString("city"), json.optString("birth"), json.optString("studentid"),json.optInt("age"),json.optInt("sex"),json.optInt("point"));

                    name = (EditText)findViewById(R.id.name);
                    sex=(TextView)findViewById(R.id.sex);
                    age=(TextView)findViewById(R.id.age);
                    email=(TextView)findViewById(R.id.email);
                    studentid=(TextView)findViewById(R.id.studentid);

                    name.setText(User.uName);
                    age.setText(String.valueOf(User.uAge));
                    email.setText(User.uEmail);
                    studentid.setText(User.ustudentid);
                    String sexStr = ((User.uSex==0) ? "女" : (User.uSex==2)? "其他" : "男");
                    sex.setText(sexStr);

                } else {
                    Log.i("22", json.optString("error"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    new SweetAlertDialog(PersondataActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it = new Intent(PersondataActivity.this, LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22", val);
        }
    };

}
