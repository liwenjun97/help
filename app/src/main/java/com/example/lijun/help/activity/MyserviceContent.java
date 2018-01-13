package com.example.lijun.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MyserviceContent extends AppCompatActivity {
    private  Button back;
    private  Button closeservice;
    private  Button changeservice;
    private TextView uername;
    private TextView servicename;
    private TextView servicecontent;
    private TextView servicepublishtime;
    private TextView serviceprice;
    private TextView servicepeople;
    public Service service;
    private  String url;
    private  String data;
    private int serviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myservice_content);
        service = new Service();
        serviceid = this.getIntent().getExtras().getInt("serviceid");

        back=(Button)findViewById(R.id.myservice_back);
        closeservice=(Button)findViewById(R.id.close_service);
        changeservice=(Button)findViewById(R.id.change_service);

        uername=(TextView) findViewById(R.id.myservice_username);
        servicename=(TextView)findViewById(R.id.myservice_name);
        servicecontent=(TextView)findViewById(R.id.myservice_content);
        servicepublishtime=(TextView)findViewById(R.id.myservice_publishtime);
        serviceprice=(TextView)findViewById(R.id.myservice_price);
        servicepeople=(TextView)findViewById(R.id.myservice_people);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyserviceContent.this,MyserviceAcitivity.class);
                startActivity(intent);
                finish();
            }
        });

        closeservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MyserviceContent.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("您确定要关闭此服务吗?")
                        .setContentText("关闭服务后不能找回!")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                /*确定取消*/
                                deleteService(serviceid);
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();

            }
        });
        changeservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle=new Bundle();
                bundle.putSerializable("serviceinfo",service);
                Intent intent=new Intent(MyserviceContent.this,ChangeMyservice.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        getServiceinfo(serviceid);//从服务器获取服务详细信息

    }
    public void deleteService(int serviceid) {
        String id = String.valueOf(serviceid);
        url = "http://10.0.2.2:8082/Home/ServiceApi/deleteService";
        try {
            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&id=" + URLEncoder.encode(id, "UTF-8");
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
                    handlerForDeleteService.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private Handler handlerForDeleteService = new Handler() {
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

                if (json.optString("error") == "") {

                    if (json.optString("success") != null) {

                        TastyToast.makeText(getApplicationContext(),"取消成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        Intent intent=new Intent(MyserviceContent.this,MyserviceAcitivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    TastyToast.makeText(getApplicationContext(),json.optString("error"),TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("service23", val);
        }
    };
    public void getServiceinfo(int serviceid) {
        String id = String.valueOf(serviceid);
        url = "http://10.0.2.2:8082/Home/ServiceApi/displayService";
        try {
            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&id=" + URLEncoder.encode(id, "UTF-8");
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
                    handlerForCheckGetService.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    private Handler handlerForCheckGetService = new Handler() {
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

                if (json.optString("error") == "") {

                    if (json.optString("service") != null) {
                        JSONObject j = new JSONObject(json.optString("service"));

                        Log.i("25", j.optString("serviceContent"));
                        service.setServiceid(j.optInt("id"));
                        service.setServiceContent(j.optString("serviceContent"));
                        Log.i("25", j.optString("serviceContent"));
                        service.setServicePlace(j.optString("servicePlace"));
                        service.setServiceTitle(j.optString("serviceTitle"));
                        service.setServicePrice(j.optInt("servicePrice"));
                        service.setServicePublishTime(j.optString("servicePublishTime"));
                        service.setServiceActiveTime(j.optString("serviceActiveTime"));
                        service.setServiceType(j.optString("serviceType"));
                        service.setServiceScore(j.optInt("serviceScore"));
                        service.setServicePeople(j.optInt("servicePeople"));
                        Log.i("service1", service.getServiceTitle());
                        //service.setuId();
                        Log.i("uid",String.valueOf(j.optInt("uId")) );
                        intview();//显示服务信息

                        //



                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("service23", val);
        }
    };

    public void intview(){


        String time=service.getServicePublishTime().substring(0, 10);
        uername.setText(User.uName);
        servicename.setText("服务名称:  "+service.getServiceTitle());
        serviceprice.setText("积分价格:  "+service.getServicePrice());
        servicecontent.setText(service.getServiceContent());
        servicepublishtime.setText("发布时间:  "+time);
        servicepeople.setText("订购人数:  "+service.getServicePeople());
    }
}
