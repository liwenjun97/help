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
 * Created by zzx on 2018/1/11.
 */

public class CreatHelp extends AppCompatActivity{
    private Button creat_help;
    private Button creat_help_back;
    private EditText help_content;
    private EditText help_title;
    private EditText help_price;
    private EditText help_type;
    private String url;
    private String data;
    private int word=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_help);

        help_content = (EditText) findViewById(R.id.help_content);
        help_title = (EditText) findViewById(R.id.help_title);
        help_price = (EditText) findViewById(R.id.help_price);
        help_type = (EditText) findViewById(R.id.help_type);

        creat_help_back = (Button) findViewById(R.id.creat_help_back);
        creat_help_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        creat_help = (Button)findViewById(R.id.creat_help);
        creat_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentText = help_content.getText().toString();
                String titleText = help_title.getText().toString();
                String priceText = help_price.getText().toString();
                String typeText = help_type.getText().toString();
                if (contentText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "求助内容不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if (titleText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "求助名称不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }

                if (priceText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "求助积分不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if (typeText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "求助类型不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if(word == 1){
                    creatHelp(contentText,titleText,priceText,typeText);
                    Intent t2 = new Intent(CreatHelp.this, MainActivity.class);
                    startActivity(t2);
                    finish();
                }
                else{
                    TastyToast.makeText(getApplicationContext(), "服务信息未填写完整", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
            }
        });
    }

    public void creatHelp(String contentText,String titleText,String priceText,String typeText){
        url = "http://10.0.2.2:8082/Home/SeekHelpApi/sendHelp";
        try {
            data = "token="+ URLEncoder.encode(User.uToken, "UTF-8")+
                    "&content="+ URLEncoder.encode(contentText, "UTF-8")+
                    "&title="+ URLEncoder.encode(titleText, "UTF-8")+
                    "&pay="+ URLEncoder.encode(priceText, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCreatHelp.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCreatHelp =new Handler(){
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
                    Log.i("41",json.optString("success"));
                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent it=new Intent(CreatHelp.this,MainActivity.class);
                    startActivity(it);
                    finish();
                    ///////////////
                }else {
                    Log.i("41",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("11",val);
        }
    };
}
