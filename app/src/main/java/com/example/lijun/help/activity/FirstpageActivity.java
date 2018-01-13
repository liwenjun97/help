package com.example.lijun.help.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2017/12/21.
 */

public class FirstpageActivity extends Fragment {
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshWidget;
    private PullMoreRecyclerAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    public  String url;
    public  String data;
    List<CardInfo> list = new ArrayList<CardInfo>();
    private Toolbar toolbar;
    private int lastServiceid;//最后一个服务id
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.firstpage,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rv = (RecyclerView) getActivity().findViewById(R.id.rv);
        swipeRefreshWidget = (SwipeRefreshLayout)getActivity(). findViewById(R.id.swipe_refresh_widget);
        swipeRefreshWidget.setColorSchemeResources(R.color.colorAccent, R.color.add_bg_color, R
                .color.colorPrimary, R.color.colorPrimaryDark, R.color.add_selected_color);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(mLayoutManager);
        //首次进入自动获取数据
         getDatasfirst();
        //创建并设置Adapter
        adapter = new PullMoreRecyclerAdapter(list);
        rv.setAdapter(adapter);


        //下拉刷新
        swipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshWidget.setEnabled(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        getDatas2();//下拉刷新
                      //  adapter.notifyDataSetChanged();
                        swipeRefreshWidget.setEnabled(true);
                        swipeRefreshWidget.setRefreshing(false);
                    }
                }, 3000);
            }
        });




        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;


            //上拉加载更多
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!swipeRefreshWidget.isRefreshing()) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==
                            adapter.getItemCount()) {
                        swipeRefreshWidget.setEnabled(false);
                        adapter.setMoreStatus(PullMoreRecyclerAdapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getdatas();
                                swipeRefreshWidget.setEnabled(true);
                                adapter.setMoreStatus(PullMoreRecyclerAdapter.PULLUP_LOAD_MORE);
                            //    adapter.notifyDataSetChanged();
                            }
                        }, 3000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                }



        });

        //目录监听事件
        adapter.setOnItemClickListener(new PullMoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos,int userid) {

                Bundle bundle=new Bundle();
                bundle.putInt("serviceid",userid);
                Intent intent=new Intent();
                intent.setClass(getActivity(),ServiceActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(getActivity(),String.valueOf(userid),Toast.LENGTH_SHORT).show();

            }
        });
    }

    //首次进入自动刷新
    private void getDatasfirst() {
        getDatas2();
    }

    //下拉刷新
    private void getDatas2() {

        url = "http://10.0.2.2:8082/Home/ServiceApi/displayServiceAll";
        try {
            //count 1:上拉加载  0:下滑刷新
            //type  0:时间排序  2:人数排序
            //last  0:最后一个id
            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&count=" + URLEncoder.encode("0", "UTF-8") +
                    "&type=" + URLEncoder.encode("0", "UTF-8")+
                    "&last=" + URLEncoder.encode("0", "UTF-8");
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
                    handlerForServiceall.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private Handler handlerForServiceall = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
        //    TastyToast.makeText(getActivity(),"刷新成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("error")==""){
                    Log.i("22", json.optString("allService"));
                    if(json.optString("allService")!=null){
                        JSONArray jsonArray =new JSONArray(json.optString("allService"));
                        JSONObject last=(JSONObject) jsonArray.get(jsonArray.length()-1);
                        Log.i("lastServiceid", last.toString());
                        lastServiceid=last.optInt("id");

                        Log.i("lastServiceid", String.valueOf(String.valueOf(lastServiceid)));
                        Service.lastserviceid=lastServiceid;
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject j=(JSONObject)jsonArray.get(i);
                            CardInfo cardInfo=new CardInfo();
                            //   Log.i("23", String.valueOf(jsonArray.length()));
                            //     cardInfo.setUserid(j.optString(""));
                            if(j.optInt("uFinish")==0) {            //只显示有效的服务
                                cardInfo.setUserid(j.optInt("id"));
                                cardInfo.setTitle(j.optString("serviceTitle"));
                                cardInfo.setContent(j.optString("serviceContent"));
                                list.add(cardInfo);
                            }

                        }

                        adapter.notifyDataSetChanged();//数据发生改变
                    }
                }else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };

    //上拉加载更多
    public void getdatas() {

        String lastid=String.valueOf(Service.lastserviceid);
        url = "http://10.0.2.2:8082/Home/ServiceApi/displayServiceAll";
        try {
            //count 1:上拉加载更多  0:下滑刷新
            //type  0:时间排序  2:人数排序
            //last  最后一个id

            data = "token=" + URLEncoder.encode(User.uToken, "UTF-8") +
                    "&count=" + URLEncoder.encode("1", "UTF-8") +
                    "&type=" + URLEncoder.encode("0", "UTF-8")+
                    "&last=" + URLEncoder.encode(lastid, "UTF-8");
            Log.i("lastmes", data);
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForServiceall2.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForServiceall2 = new Handler() {
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
                if(json.optString("error")==""){
                    if(json.optString("allService")!=null){
                        JSONArray jsonArray =new JSONArray(json.optString("allService"));
                        JSONObject last=(JSONObject) jsonArray.get(jsonArray.length()-1);
                        Log.i("lastServiceid", last.toString());
                        lastServiceid=last.optInt("id");
                        Log.i("lastServiceid", String.valueOf(String.valueOf(lastServiceid)));

                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject j=(JSONObject)jsonArray.get(i);
                            CardInfo cardInfo=new CardInfo();
                            //   Log.i("23", String.valueOf(jsonArray.length()));
                            //     cardInfo.setUserid(j.optString(""));
                            cardInfo.setUserid(j.optInt("id"));
                            cardInfo.setTitle(j.optString("serviceTitle"));
                            cardInfo.setContent(j.optString("serviceContent"));
                            list.add(cardInfo);
                        }

                        Service.lastserviceid=lastServiceid;//记录最后一项的serviceid
                        adapter.notifyDataSetChanged();//数据发生改变
                    }
                }else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("10",val);
        }
    };
}
