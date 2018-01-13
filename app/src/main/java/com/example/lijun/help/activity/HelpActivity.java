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
import com.example.lijun.help.bean.Help;
import com.example.lijun.help.bean.Help;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lijun on 2018/1/11.
 */

public class HelpActivity extends AppCompatActivity {
    private Button DingGou;
    private Button help_back;
    private Help help;
    public String url;
    public String data;
    public TextView Username;
    public TextView helpPublishTime;
    public TextView helpTitle;
    public TextView helpPay;
    public TextView helpContent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        help=new Help();
        int helpid = this.getIntent().getExtras().getInt("helpid");

     //   Toast.makeText(this, String.valueOf(helpid), Toast.LENGTH_SHORT).show();
        getHelpinfo(helpid);//从服务器获取服务详细信息

        DingGou = (Button) findViewById(R.id.DingGou_help);
        DingGou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DingGouHelp();
            }
        });

        help_back = (Button) findViewById(R.id.help_back);
        help_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    public void getHelpinfo(int helpid)
    {
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
                    handlerForGetHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetHelp = new Handler() {
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

                    if (json.optString("help") != null) {
                        JSONObject j = new JSONObject(json.optString("help"));

                        Log.i("25", j.optString("seekContent"));
                        help.setHelpid(j.optInt("id"));
                        help.setHelpContent(j.optString("seekContent"));
                        Log.i("25", j.optString("seekContent"));
                        help.setHelpTitle(j.optString("seekTitle"));
                        help.setHelpPrice(j.optInt("seekPay"));
                        help.setHelpPublishTime(j.optString("seekPublishTime"));


                        Log.i("uid",String.valueOf(j.optInt("uId")) );
                        getOtherinfo(j.optInt("uId"));

                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("help22", val);
        }
    };

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

    public void initview() {
        String time = help.getHelpPublishTime().substring(0, 10);
        helpPublishTime = (TextView) findViewById(R.id.helpPublishTime);
        helpPublishTime.setText(time);

        helpContent = (TextView) findViewById(R.id.helpContent);
        helpContent.setText(help.getHelpContent());

        helpTitle = (TextView) findViewById(R.id.helpTitle);
        helpTitle.setText("求助名称: "+help.getHelpTitle());

        helpPay = (TextView) findViewById(R.id.helpPay);
        helpPay.setText("奖励积分："+String.valueOf(help.getHelpPrice()));

        Username=(TextView)findViewById(R.id.Username_help);
        Username.setText(help.getOthername());
    }

    public void DingGouHelp()
    {
        url = "http://10.0.2.2:8082/Home/SeekHelpApi/chooseHelp";
        try {
            data = "token="+ URLEncoder.encode(User.uToken, "UTF-8")+
                    "&helpId="+URLEncoder.encode(String.valueOf(help.getHelpid()), "UTF-8");
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
                    handlerForDingGouHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private Handler handlerForDingGouHelp =new Handler(){
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
                    TastyToast.makeText(getApplicationContext(), "选择成功，帮助他人后您将获得积分奖励", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    Intent it=new Intent(HelpActivity.this,MainActivity.class);
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
}
