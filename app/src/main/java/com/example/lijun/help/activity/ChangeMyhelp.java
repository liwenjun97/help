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
import android.widget.TextView;

import com.example.lijun.help.R;
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

public class ChangeMyhelp  extends AppCompatActivity {
    private Button back;
    private  Button up_change;

    private TextView uername;
    private TextView helpname;
    private TextView helpcontent;
    private TextView helppublishtime;
    private TextView helpprice;
    private TextView helppeople;
    private Help help;

    private  String url;
    private  String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhelp_change);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        help=(Help)bundle.getSerializable("helpinfo");

        back=(Button)findViewById(R.id.myhelp_change_back);
        up_change=(Button)findViewById(R.id.change_up_help);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(help.getHelpContent()!=null)
        {

            //不可更改的项目只显示
            uername=(TextView) findViewById(R.id.myhelp_username_change);
            uername.setText(User.uName);
            String time=help.getHelpPublishTime().substring(0, 10);
            helppublishtime=(TextView)findViewById(R.id.myhelp_publishtime_change);
            helppublishtime.setText(time);

            //可更改的项目

            helpprice=(EditText)findViewById(R.id.myhelp_change_price);
            helpprice.setText(String.valueOf(help.getHelpPrice()));
            helpname=(EditText)findViewById(R.id.myhelp_change_name);
            helpname.setText(help.getHelpTitle());
            helpcontent=(EditText)findViewById(R.id.myhelp_change_content);
            helpcontent.setText(help.getHelpContent());
        }
        up_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ChangeMyhelp.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定修改并提交吗?")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                /*确定更改*/
                                changeHelp();
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }
    public void  changeHelp(){
        String id=String.valueOf(help.getHelpid());
        String content=helpcontent.getText().toString();
        String title=helpname.getText().toString();
        String price=helpprice.getText().toString();


        url = "http://10.0.2.2:8082/Home/SeekHelpApi/changeHelp";
        try {
            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&id="+URLEncoder.encode(id, "UTF-8")+
                    "&content=" + URLEncoder.encode(content, "UTF-8")+
                    "&title=" + URLEncoder.encode(title, "UTF-8")+
                    "&price=" + URLEncoder.encode(price, "UTF-8");
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
                    handlerForChangeHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForChangeHelp = new Handler() {
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
                    Intent intent=new Intent(ChangeMyhelp.this,MyhelpActivity.class);
                    startActivity(intent);
                    finish();

                } else if(json.optString("error") != ""){
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("changeHelp22", val);
        }
    };



}
