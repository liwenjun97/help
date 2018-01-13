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
 * Created by zzx on 2017/12/30.
 */

public class CreatService extends AppCompatActivity {
    private Button creat;
    private Button creat_service_back;
    private String[] begin = new String[]{"07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22"};
    private String[] end = new String[]{"07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22"};
    private EditText content;
    private EditText title;
    private EditText place;
    private EditText price;
    private EditText type;
    private String url;
    private String data;
    private int word=1;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_service);

        content = (EditText)findViewById(R.id.content);
        title = (EditText)findViewById(R.id.title);
        place = (EditText)findViewById(R.id.place);
        price = (EditText)findViewById(R.id.price);
        type = (EditText)findViewById(R.id.type);

        creat_service_back = (Button)findViewById(R.id.creat_service_back);
        creat_service_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent it=new Intent(CreatService.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        creat = (Button)findViewById(R.id.creat_S);
        creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentText = content.getText().toString();
                String titleText = title.getText().toString();
                String placeText = place.getText().toString();
                String priceText = price.getText().toString();
                String typeText = type.getText().toString();
                if (contentText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "服务内容不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if (titleText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "服务名称不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if (placeText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "服务地点不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if (priceText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "服务积分不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if (typeText.equals("")) {
                    word=0;
                    TastyToast.makeText(getApplicationContext(), "服务类型不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
                if(word == 1){
                    creatService(contentText,titleText,placeText,priceText,typeText);
                }
                else{
                    TastyToast.makeText(getApplicationContext(), "服务信息未填写完整", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
            }
        });
    }

    public void creatService(String contentText,String titleText,String placeText,String priceText,String typeText){
        url = "http://10.0.2.2:8082/Home/ServiceApi/sendService";
        try {
            data = "token="+ URLEncoder.encode(User.uToken, "UTF-8")+
                    "&content="+ URLEncoder.encode(contentText, "UTF-8")+
                    "&place="+ URLEncoder.encode(placeText, "UTF-8")+
                    "&title="+ URLEncoder.encode(titleText, "UTF-8")+
                    "&price="+ URLEncoder.encode(priceText, "UTF-8")+
                    "&type="+ URLEncoder.encode(typeText, "UTF-8")+
                    "&time="+ URLEncoder.encode("0000-00-00 00:00:00", "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCreatService.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCreatService =new Handler(){
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
                    Log.i("11",json.optString("success"));
                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent it=new Intent(CreatService.this,MainActivity.class);
                    startActivity(it);
                    finish();
                    ///////////////
                }else {
                    Log.i("11",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("11",val);
        }
    };
}
