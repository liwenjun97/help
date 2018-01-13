package com.example.lijun.help.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijun.help.R;
import com.example.lijun.help.bean.CardInfo;
import com.example.lijun.help.bean.Service;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by zzx on 2017/12/24.
 */

public class ServiceActivity extends AppCompatActivity {
    private Button DingGou;
    private Button service_back;
    private Service service;
    public String url;
    public String data;
    public TextView Username;
    public TextView servicePublishTime;
    public TextView serviceTitle;
    public TextView servicePiont;
    public TextView serviceContent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);
        service = new Service();
        int serviceid = this.getIntent().getExtras().getInt("serviceid");

    //    Toast.makeText(this, String.valueOf(serviceid + 1), Toast.LENGTH_SHORT).show();
        getServiceinfo(serviceid);//从服务器获取服务详细信息


        //
        //   Log.i("servicetitle",service.getServiceTitle());


        DingGou = (Button) findViewById(R.id.DingGou);
        DingGou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent it=new Intent(ServiceActivity.this,Service_orderActivity.class);
//                startActivity(it);
//                finish();
                DingGouService();
            }
        });

        service_back = (Button) findViewById(R.id.service_back);
        service_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }
    public void DingGouService(){
        url = "http://10.0.2.2:8082/Home/ServiceApi/chooseService";
        try {
            data = "token="+ URLEncoder.encode(User.uToken, "UTF-8")+
                    "&serviceId="+URLEncoder.encode(String.valueOf(service.getServiceid()), "UTF-8");
            Log.i("mes1",data);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForDingGouService.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForDingGouService =new Handler(){
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
                    Log.i("12",json.optString("success"));
                    TastyToast.makeText(getApplicationContext(), "订购成功", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent it=new Intent(ServiceActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();
                }else {
                    Log.i("12",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
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
                        getOtherinfo(j.optInt("uId"));

                        //



                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("service22", val);
        }
    };

    public void initview() {
        String time = service.getServicePublishTime().substring(0, 10);
        servicePublishTime = (TextView) findViewById(R.id.servicePublishTime);
        servicePublishTime.setText(time);

        serviceContent = (TextView) findViewById(R.id.serviceContent);
        serviceContent.setText(service.getServiceContent());

        serviceTitle = (TextView) findViewById(R.id.serviceTitle);
        serviceTitle.setText("服务名称: "+service.getServiceTitle());

        servicePiont = (TextView) findViewById(R.id.serviceScore);
        servicePiont.setText("需要积分："+String.valueOf(service.getServicePrice()));

        Username=(TextView)findViewById(R.id.Username);
        Username.setText(service.getOthername());
    }

    public void getOtherinfo(int id) {
        String uid = String.valueOf(id);
        url = "http://10.0.2.2:8082/Home/UserApi/getOthersInfo";
        try {
            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&id=" + URLEncoder.encode(uid, "UTF-8");
            Log.i("otherinfodata", data);
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetotherInfo.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetotherInfo = new Handler() {
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
                if(json.optString("success")!="")
                {
                    String othername=json.optString("name");
                    service.setOthername(othername);
                    initview();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("other", val);

        }

    };
}
