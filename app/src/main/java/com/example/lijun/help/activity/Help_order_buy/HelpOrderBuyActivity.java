package com.example.lijun.help.activity.Help_order_buy;


import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.example.lijun.help.R;
import com.example.lijun.help.activity.Service_order_buy.Cancel_Order_1;
import com.example.lijun.help.activity.Service_order_buy.Finished_Order_1;
import com.example.lijun.help.activity.Service_order_buy.Unfinished_Order_1;
import com.example.lijun.help.adapter.MyFragmentPageAdapter;
import com.example.lijun.help.adapter.Myadapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2018/1/10.
 */

public class HelpOrderBuyActivity extends AppCompatActivity {



    //************************************************//

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private List<String> mTitle = new ArrayList<String>();
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private Myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_order_buy);

        mViewPager = (ViewPager) findViewById(R.id.help_order_buy_vp_view);
        mTabLayout = (TabLayout)findViewById(R.id.help_order_buy_tabs);


        mTitle.add("未完成");
        mTitle.add("已完成");
        mTitle.add("已取消");

        list = new ArrayList<Fragment>();
        list.add(new Unfinished_Order_3());
        list.add(new Finished_Order_3());
        list.add(new Cancel_Order_3());

        adapter = new Myadapter(getSupportFragmentManager(),mTitle, list);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        //为TabLayout设置ViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        //使用ViewPager的适配器
        mTabLayout.setTabsFromPagerAdapter(adapter);


    }
}
