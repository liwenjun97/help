package com.example.lijun.help.activity.Help_order_my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lijun.help.R;
import com.example.lijun.help.activity.Service_order_buy.Order_service_buy;
import com.example.lijun.help.activity.Service_order_buy.ServiceOrderBuyAcitivity;
import com.example.lijun.help.bean.Help;
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
 * Created by zzx on 2018/1/11.
 */

public class Order_help_my extends AppCompatActivity {
    private Help help;
    private int orderid;
    public int helpid;
    public String url;
    public String data;
    public TextView orderTime;
    public TextView helpTitle;
    public TextView helpPiont;
    public TextView helpContent;
    public TextView Username;
    public int uid;
    public Button order_help_back;
    public Button quxiao_help;
    public Button queren_help;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_help_my);
        help = new Help();
        orderid = this.getIntent().getExtras().getInt("orderid");


        getDatas();


        order_help_back=(Button)findViewById(R.id.orderhelp_my_back);
        order_help_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        quxiao_help=(Button)findViewById(R.id.quxiao_help);
        quxiao_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeHelp();
            }
        });

        queren_help=(Button)findViewById(R.id.queren_help);
        queren_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishHelp();
            }
        });
    }
    private void closeHelp(){
        url = "http://10.0.2.2:8082/Home/SeekHelpApi/closeHelp";
        try {
            data = "token="+ URLEncoder.encode(User.uToken, "UTF-8")+
                    "&helpId="+URLEncoder.encode(String.valueOf(helpid), "UTF-8");
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
                    handlerForCloseHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForCloseHelp = new Handler() {
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

                if(json.optString("success")!=""){
                    TastyToast.makeText(getApplicationContext(), "取消成功", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent intent=new Intent(Order_help_my.this,HelpOrderMyActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("service225", val);
        }
    };

    public void finishHelp(){
        url = "http://10.0.2.2:8082/Home/SeekHelpApi/finishHelp";
        try {
            data = "token="+ URLEncoder.encode(User.uToken, "UTF-8")+
                    "&helpId="+URLEncoder.encode(String.valueOf(helpid), "UTF-8")+
                    "&score="+URLEncoder.encode("0", "UTF-8");//分数没用到
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
                    handlerForFinishHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForFinishHelp = new Handler() {
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

                if(json.optString("success")!=""){
                    TastyToast.makeText(getApplicationContext(), "确认成功", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent intent=new Intent(Order_help_my.this,HelpOrderMyActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("service225", val);
        }
    };

    private void getDatas() {

        url = "http://10.0.2.2:8082/Home/SeekHelpApi/displayHelpUserOwn";
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
                    Log.i("52", json.optString("help"));
                    if(json.optString("help")!=null)
                    {
                        JSONArray jsonArray =new JSONArray(json.optString("help"));


                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.i("31", String.valueOf(i));
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            if(j.optInt("id")==orderid)
                            {
                                helpid = j.optInt("helpId");
                                orderTime = (TextView) findViewById(R.id.orderhelp_my_time);
                                orderTime.setText(j.optString("uPublishTime"));
                                uid = j.optInt("uId");
                                getHelpinfo(helpid);//从服务器获取求助详细信息
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

    public void getHelpinfo(int helpid) {
        String id = String.valueOf(helpid);
        url = "http://10.0.2.2:8082/Home/SeekHelpApi/displayHelp";
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
                    handlerForCheckGetHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckGetHelp = new Handler() {
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

                    if (json.optString("help") != null)
                    {
                        JSONObject j = new JSONObject(json.optString("help"));

                        Log.i("25", j.optString("helpContent"));
                        help.setHelpid(j.optInt("id"));
                        help.setHelpContent(j.optString("seekContent"));
                        help.setHelpTitle(j.optString("seekTitle"));
                        help.setHelpPrice(j.optInt("seekPay"));

                        //service.setuId();

                        getOtherinfo(uid);
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


        helpContent = (TextView) findViewById(R.id.orderhelp_my_content);
        helpContent.setText(help.getHelpContent());

        helpTitle = (TextView) findViewById(R.id.orderhelp_my_title);
        helpTitle.setText(help.getHelpTitle());

        helpPiont = (TextView) findViewById(R.id.orderhelp_my_piont);
        helpPiont.setText("积分："+String.valueOf(help.getHelpPrice()));

        Username=(TextView)findViewById(R.id.orderhelp_my_name);
        Username.setText(help.getOthername());
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
                    help.setOthername(othername);
                    initview();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("other", val);

        }

    };
}
