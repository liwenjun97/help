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
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijun.help.R;
import com.example.lijun.help.bean.Service;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lijun on 2018/1/12.
 */

public class ChangeMyservice extends AppCompatActivity {
    private  Button back;
    private  Button up_change;

    private TextView uername;
    private TextView servicename;
    private TextView servicecontent;
    private TextView servicepublishtime;
    private TextView serviceprice;
    private TextView servicepeople;
    private Service service;

    private  String url;
    private  String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myservice_change);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        service=(Service)bundle.getSerializable("serviceinfo");

        back=(Button)findViewById(R.id.myservice_change_back);
        up_change=(Button)findViewById(R.id.change_up_service);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(service.getServiceContent()!=null)
        {

            //不可更改的项目只显示
            uername=(TextView) findViewById(R.id.myservice_username_change);
            uername.setText(User.uName);
            String time=service.getServicePublishTime().substring(0, 10);
            servicepublishtime=(TextView)findViewById(R.id.myservice_publishtime_change);
            servicepublishtime.setText(time);

            //可更改的项目

            serviceprice=(EditText)findViewById(R.id.myservice_change_price);
            serviceprice.setText(String.valueOf(service.getServicePrice()));
            servicename=(EditText)findViewById(R.id.myservice_change_name);
            servicename.setText(service.getServiceTitle());
            servicecontent=(EditText)findViewById(R.id.myservice_change_content);
            servicecontent.setText(service.getServiceContent());
        }
        up_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ChangeMyservice.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定修改并保存吗？")
                        .setContentText("修改后订购人数和发布时间会重置？")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                /*确定更改*/
                               changeService();
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }
    public void  changeService(){
        String id=String.valueOf(service.getServiceid());
        String content=servicecontent.getText().toString();
        String place="";
        String title=servicename.getText().toString();
        String price=serviceprice.getText().toString();
        String time="";
        String type="";

        url = "http://10.0.2.2:8082/Home/ServiceApi/changeService";
        try {
            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&id="+URLEncoder.encode(id, "UTF-8")+
                    "&content=" + URLEncoder.encode(content, "UTF-8")+
                    "&place=" + URLEncoder.encode(place, "UTF-8")+
                    "&title=" + URLEncoder.encode(title, "UTF-8")+
                    "&price=" + URLEncoder.encode(price, "UTF-8")+
                    "&time=" + URLEncoder.encode(time, "UTF-8")+
                    "&type=" + URLEncoder.encode(type, "UTF-8");
            Log.i("mes", data);
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForChangeService.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForChangeService = new Handler() {
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

                if (json.optString("success") != "") {
                    TastyToast.makeText(getApplicationContext(), "修改成功", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                    Intent intent=new Intent(ChangeMyservice.this,MyserviceAcitivity.class);
                    startActivity(intent);
                    finish();

                } else if(json.optString("error") != ""){
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("changeService22", val);
        }
    };



}
