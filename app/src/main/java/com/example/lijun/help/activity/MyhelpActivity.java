package com.example.lijun.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lijun.help.R;
import com.example.lijun.help.adapter.PullMoreRecyclerAdapter;
import com.example.lijun.help.bean.CardInfo;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2018/1/11.
 */

public class MyhelpActivity extends AppCompatActivity {
    private Button back;

    List<CardInfo> list = new ArrayList<CardInfo>();
    private RecyclerView ordershow1;
    public String url;
    public String data;
    private PullMoreRecyclerAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhelp);
        back=(Button)findViewById(R.id.back_myhelp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ordershow1 = (RecyclerView)findViewById(R.id.myhelp);

        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        ordershow1.setLayoutManager(mLayoutManager);
        list.clear();
        getDatas();

        adapter = new PullMoreRecyclerAdapter(list);
        ordershow1.setAdapter(adapter);
//目录监听事件
        adapter.setOnItemClickListener(new PullMoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos,int userid) {

                Bundle bundle=new Bundle();
                bundle.putInt("helpid",userid);
                Intent intent=new Intent(MyhelpActivity.this,MyhelpContent.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(),String.valueOf(userid),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDatas() {

        url = "http://10.0.2.2:8082/Home/SeekHelpApi/displayHelpOwn";
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
                if(json.optString("success")!=""){
                    Log.i("22", json.optString("help"));
                    if(json.optString("help")!=null){
                        JSONArray jsonArray =new JSONArray(json.optString("help"));

                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject j=(JSONObject)jsonArray.get(i);
                            CardInfo cardInfo=new CardInfo();
                            //   Log.i("23", String.valueOf(jsonArray.length()));
                            //     cardInfo.setUserid(j.optString(""));
                            if(j.optInt("uFinish")!=2) {    //显示已经生成订单的和刚发布的服务
                                cardInfo.setUserid(j.optInt("id"));
                                cardInfo.setTitle(j.optString("seekTitle"));
                                cardInfo.setContent(j.optString("seekContent"));
                                list.add(cardInfo);
                            }

                        }

                        adapter.notifyDataSetChanged();//数据发生改变

                    }
                }else {

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Log.i("helpmy",val);
        }
        //   Log.i("10", val);

    };
}
