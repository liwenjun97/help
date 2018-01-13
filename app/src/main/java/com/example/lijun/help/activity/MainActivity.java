package com.example.lijun.help.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijun.help.R;
import com.example.lijun.help.adapter.MyFragmentPageAdapter;
import com.example.lijun.help.bean.User;
import com.example.lijun.help.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private RadioGroup bottom_tab_bar;
    private RadioButton bottom_today;
    private RadioButton bottom_yesterday;
    private RadioButton bottom_tomorrow;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;

    private List<Fragment> list;
    private ViewPager mainContent;
    private MyFragmentPageAdapter adapter;

    private ImageView account;
    private TextView id_username;
    private TextView id_dream;

    public static final int YESTERDAY = 0;
    public static final int TODAY = 1;
    public static final int TOMORROW = 2;

    private String urlForGetUserInfo;
    private String dataForGetUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
        initData();
        getUserInfo(User.uToken);
        mainContent = (ViewPager) findViewById(R.id.mainContent);
        adapter = new MyFragmentPageAdapter(getSupportFragmentManager(), list);
        mainContent.setAdapter(adapter);
        mainContent.setCurrentItem(1);
        bottom_today.setChecked(true);
        mainContent.addOnPageChangeListener(this);
    }


    //界面初始化
    public void initViews() {
        bottom_tab_bar = (RadioGroup) findViewById(R.id.bottom_tab_bar);
        bottom_today = (RadioButton) findViewById(R.id.today);
        bottom_yesterday = (RadioButton) findViewById(R.id.yesterday);
        bottom_tomorrow = (RadioButton) findViewById(R.id.tomorroy);
        bottom_tab_bar.setOnCheckedChangeListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.enter_creatservice) {
//                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    Intent it=new Intent(MainActivity.this, CreatService.class);
                    startActivity(it);

                } else if (menuItemId == R.id.enter_creathelp) {
//                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    Intent t2 = new Intent(MainActivity.this, CreatHelp.class);
                    startActivity(t2);


                }
                return true;

            }
        });


        list = new ArrayList<Fragment>();
        list.add(new FirstpageActivity());
        list.add(new SecondepageActivity());
        list.add(new ThirdpageActivity());

        mNavigation = (NavigationView) findViewById(R.id.navigation_view);
        setNavigationView(mNavigation);//设置菜单点击事件
        mNavigation.setItemIconTintList(null);

        //获取头布局文件
        View drawerView = mNavigation.inflateHeaderView(R.layout.header_just_username);

        id_username = (TextView) drawerView.findViewById(R.id.id_username);
        id_dream = (TextView) drawerView.findViewById(R.id.id_dream);



    }

    public void setNavigationView(NavigationView navigationView)   //设置菜单点击事件
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId2 = item.getItemId();
                if (menuItemId2 == R.id.nav_info) { /*个人信息*/
                    Intent intent = new Intent(MainActivity.this, PersondataActivity.class);
                    startActivity(intent);

                } else if (menuItemId2 == R.id.nav_aboutus) {
                    TastyToast.makeText(getApplicationContext(),"40101小组出品",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    /*关于我们*/

                }else if(menuItemId2==R.id.nav_myservice){
                    /*发布的服务*/
                    Intent intent=new Intent(MainActivity.this,MyserviceAcitivity.class);
                    startActivity(intent);

                }else if(menuItemId2==R.id.nav_myhelp){
                    /*发布的求助*/
                    Intent intent=new Intent(MainActivity.this,MyhelpActivity.class);
                    startActivity(intent);
                }else if(menuItemId2==R.id.nav_version){
                    TastyToast.makeText(getApplicationContext(),"当前版本 V1.0",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    /*当前版本*/
                }else if(menuItemId2==R.id.cancellation) {
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    /*注销*/
                }else if (menuItemId2==R.id.exit){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("确定要退出吗?")
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                /*确定取消*/
                                    System.exit(0);
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .show();

                    /*退出*/
                }


                return true;
            }

        });
    }

    private void initData() {

        // 参数：开启抽屉的activity、DrawerLayout的对象、toolbar按钮打开关闭的对象、描述open drawer、描述close drawer
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        // 添加抽屉按钮，通过点击按钮实现打开和关闭功能; 如果不想要抽屉按钮，只允许在侧边边界拉出侧边栏，可以不写此行代码
        mDrawerToggle.syncState();
        // 设置按钮的动画效果; 如果不想要打开关闭抽屉时的箭头动画效果，可以不写此行代码
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int CheckId) {
        switch (CheckId) {
            case R.id.yesterday:
                mainContent.setCurrentItem(YESTERDAY);
                TextView textView = (TextView) findViewById(R.id.tv_title);
                textView.setText("服务");
                break;
            case R.id.today:
                mainContent.setCurrentItem(TODAY);
                TextView textView2 = (TextView) findViewById(R.id.tv_title);
                textView2.setText("求助");
                break;
            case R.id.tomorroy:
                mainContent.setCurrentItem(TOMORROW);
                TextView textView3 = (TextView) findViewById(R.id.tv_title);
                textView3.setText("订单");
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (mainContent.getCurrentItem()) {
                case YESTERDAY:
                    bottom_yesterday.setChecked(true);
                    break;
                case TODAY:
                    bottom_today.setChecked(true);
                    break;
                case TOMORROW:
                    bottom_tomorrow.setChecked(true);
                    break;
            }

        }
    }

    public void getUserInfo(String token) {
        urlForGetUserInfo = "http://10.0.2.2:8082/Home/UserApi/getUserInfo";

        try {
            dataForGetUserInfo = "token=" + URLEncoder.encode(User.uToken, "UTF-8");
            Log.i("mes",dataForGetUserInfo);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetUserInfo, dataForGetUserInfo);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetUserInfo.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForGetUserInfo = new Handler() {
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
                Log.i("23", val);
                if (json.optString("success") != "") {
                    Log.i("22", json.optString("success"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    //设置到本地并且缓存到本地数据库
                    User.setUserInfo(json.optInt("id"), json.optString("name"), json.optString("email"), json.optString("password"), json.optString("regtime"), json.optString("imgsrc"), json.optString("city"), json.optString("birth"), json.optString("studentid"),json.optInt("age"),json.optInt("sex"),json.optInt("point"));
                    //显示用户名和积分

                    id_username.setText(User.uName);
                    id_dream.setText("积分："+User.upoint);

                } else {
                    Log.i("22", json.optString("error"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("user", val);
        }
    };
}
