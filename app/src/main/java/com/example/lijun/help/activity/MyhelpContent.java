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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lijun on 2018/1/12.
 */

public class MyhelpContent extends AppCompatActivity {
    private Button back;
    private  Button closehelp;
    private  Button changehelp;
    private TextView uername;
    private TextView helpname;
    private TextView helpcontent;
    private TextView helppublishtime;
    private TextView helpprice;

    public Help help;
    private  String url;
    private  String data;
    private int helpid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhelp_content);
        help = new Help();
        helpid = this.getIntent().getExtras().getInt("helpid");

        back=(Button)findViewById(R.id.myhelp_back);
        closehelp=(Button)findViewById(R.id.close_help);
        changehelp=(Button)findViewById(R.id.change_help);

        uername=(TextView) findViewById(R.id.myhelp_username);
        helpname=(TextView)findViewById(R.id.myhelp_name);
        helpcontent=(TextView)findViewById(R.id.myhelp_content);
        helppublishtime=(TextView)findViewById(R.id.myhelp_publishtime);
        helpprice=(TextView)findViewById(R.id.myhelp_price);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyhelpContent.this,MyhelpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        closehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MyhelpContent.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("您确定要关闭此求助吗?")
                        .setContentText("关闭求助后不能找回!")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                /*确定取消*/
                                deleteHelp(helpid);
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();

            }
        });
        changehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putSerializable("helpinfo",help);
                Intent intent=new Intent(MyhelpContent.this,ChangeMyhelp.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        getHelpinfo(helpid);//从服务器获取服务详细信息

    }
    public void deleteHelp(int helpid) {
        String id = String.valueOf(helpid);
        url = "http://10.0.2.2:8082/Home/SeekHelpApi/deleteHelp";
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
                    handlerForDeleteHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private Handler handlerForDeleteHelp = new Handler() {
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
                        Intent intent=new Intent(MyhelpContent.this,MyhelpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    TastyToast.makeText(getApplicationContext(),json.optString("error"),TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("help23", val);
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
                        intview();
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("help22", val);
        }
    };

    public void intview(){


        String time=help.getHelpPublishTime().substring(0, 10);
        uername.setText(User.uName);
        helpname.setText("求助名称:  "+help.getHelpTitle());
        helpprice.setText("奖励积分:  "+help.getHelpPrice());
        helpcontent.setText(help.getHelpContent());
        helppublishtime.setText("发布时间:  "+time);

    } 
}
