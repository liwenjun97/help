package com.example.lijun.help.activity.Service_order_my;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Finished_Order_2 extends Fragment {
    List<CardInfo> list = new ArrayList<CardInfo>();
    private RecyclerView ordershow;
    public String url;
    public String data;
    private PullMoreRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshWidget;
    private LinearLayoutManager mLayoutManager;
    private Service service;
    private  int serviceID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.finished_order_my,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ordershow= (RecyclerView) getActivity().findViewById(R.id.re_finished_order_my);

        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        ordershow.setLayoutManager(mLayoutManager);

        list.clear();
        getDatas();

        TastyToast.makeText(getActivity(), "已完成", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

        adapter = new PullMoreRecyclerAdapter(list);
        ordershow.setAdapter(adapter);

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
                Log.i("33","qqq");
                if(json.optString("error")==""){
                    Log.i("32", json.optString("order"));
                    if(json.optString("order")!=null){
                        JSONArray jsonArray =new JSONArray(json.optString("order"));


                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.i("31", String.valueOf(i));
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            int ufinish = j.optInt("uFinish");
                            if(ufinish == 1&&j.optInt("idOwn")==User.uId) {
                                CardInfo cardInfo = new CardInfo();

                                cardInfo.setUserid(j.optInt("uid"));
                                cardInfo.setTitle(j.optString("servicename"));
                                cardInfo.setContent(j.optString("serviceContent"));

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
            Log.i("unfished",val);
        }
        //   Log.i("10", val);

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
                        Log.i("33", json.optString("service"));
                        if(j.optInt("uFinish")==1)
                        {
                            CardInfo cardInfo = new CardInfo();

                            cardInfo.setUserid(j.optInt("uid"));
                            cardInfo.setTitle(j.optString("serviceTitle"));
                            cardInfo.setContent(j.optString("serviceContent"));
                            list.add(cardInfo);
                        }

                    }
                    adapter.notifyDataSetChanged();//数据发生改变
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("service22", val);
        }
    };
}
