package com.example.lijun.help.activity.Service_order_my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.lijun.help.R;
import com.example.lijun.help.adapter.Myadapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2018/1/10.
 */

public class ServiceOrderMyActivity extends AppCompatActivity {private List<Fragment> list;


    //************************************************//

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private Myadapter adapter;
    private List<String> mTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_order_my);
        mViewPager = (ViewPager) findViewById(R.id.service_order_my_vp_view);
        mTabLayout = (TabLayout)findViewById(R.id.service_order_my_tabs);

        mTitle.add("未完成");
        mTitle.add("已完成");
        mTitle.add("已取消");

        list = new ArrayList<Fragment>();
        list.add(new Unfinished_Order_2());
        list.add(new Finished_Order_2());
        list.add(new Cancel_Order_2());

        adapter = new Myadapter(getSupportFragmentManager(),mTitle, list);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        //为TabLayout设置ViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        //使用ViewPager的适配器
        mTabLayout.setTabsFromPagerAdapter(adapter);


    }

}
