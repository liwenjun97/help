package com.example.lijun.help.activity.Service_order_my;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lijun.help.R;
import com.example.lijun.help.adapter.PullMoreRecyclerAdapter;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2018/1/10.
 */

public class Order_service_my extends AppCompatActivity{
    private Service service;
    private int orderid;
    public int serviceid;
    public int uId;
    public String url;
    public String data;
    public TextView orderTime;
    public TextView orderID;
    public TextView serviceTitle;
    public TextView servicePiont;
    public TextView serviceContent;
    public TextView buyname;
    public Button order_service_back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_service_my);
        service = new Service();
        orderid = this.getIntent().getExtras().getInt("orderid");


        getDatas();


        order_service_back=(Button)findViewById(R.id.order_service_back);
        order_service_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDatas() {

        url = "http://10.0.2.2:8082/Home/ServiceApi/displayOrderUserOwn";
        try {

            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8");
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
                    Log.i("43","qqq");
                    handlerFordisplayOrderIng.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private Handler handlerFordisplayOrderIng = new Handler() {
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
                if(json.optString("error")=="")
                {
                    Log.i("42", json.optString("order"));
                    if(json.optString("order")!=null)
                    {
                        JSONArray jsonArray =new JSONArray(json.optString("order"));


                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.i("31", String.valueOf(i));
                            JSONObject j = (JSONObject) jsonArray.get(i);

                            if(j.optInt("id")==orderid)
                            {
                                serviceid = j.optInt("serviceId");
                                orderTime = (TextView) findViewById(R.id.order_service_time);
                                orderID = (TextView) findViewById(R.id.orderID);
                                TastyToast.makeText(getApplicationContext(), String.valueOf(j.optInt("serviceId")), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                                orderTime.setText(j.optString("uPublishTime"));
                                orderID.setText(j.optString("id"));
                                uId = j.optInt("uId");
                                getServiceinfo(serviceid);//从服务器获取服务详细信息
                            }

                        }



                    }
                }else {

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
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

                if (json.optString("error") == "")
                {

                    if (json.optString("service") != null)
                    {
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
                        getOtherinfo(uId);
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


        serviceContent = (TextView) findViewById(R.id.service_order_content);
        serviceContent.setText(service.getServiceContent());

        serviceTitle = (TextView) findViewById(R.id.service_order_title);
        serviceTitle.setText(service.getServiceTitle());

        servicePiont = (TextView) findViewById(R.id.order_service_piont);
        servicePiont.setText("积分："+String.valueOf(service.getServicePrice()));

        buyname=(TextView)findViewById(R.id.order_service_buyname);
        buyname.setText(service.getOthername());
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
