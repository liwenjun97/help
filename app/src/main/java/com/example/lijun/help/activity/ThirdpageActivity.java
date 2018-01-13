package com.example.lijun.help.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lijun.help.R;
import com.example.lijun.help.activity.Help_order_buy.HelpOrderBuyActivity;
import com.example.lijun.help.activity.Help_order_my.HelpOrderMyActivity;
import com.example.lijun.help.activity.Service_order_buy.ServiceOrderBuyAcitivity;
import com.example.lijun.help.activity.Service_order_my.ServiceOrderMyActivity;

/**
 * Created by lijun on 2017/12/21.
 */

public class ThirdpageActivity extends Fragment {
    private Button buyservice;
    private Button myservice;
    private Button buyhelp;
    private Button myhelp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.thirdpage,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buyservice=(Button)getActivity().findViewById(R.id.buyservice);
        myservice=(Button)getActivity().findViewById(R.id.myservice);
        buyhelp=(Button)getActivity().findViewById(R.id.buyhelp);
        myhelp=(Button)getActivity().findViewById(R.id.myhelp);

        buyservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ServiceOrderBuyAcitivity.class);
                startActivity(intent);

            }
        });

        myservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ServiceOrderMyActivity.class);
                startActivity(intent);
            }
        });

        buyhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),HelpOrderBuyActivity.class);
                startActivity(intent);

            }
        });

        myhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),HelpOrderMyActivity.class);
                startActivity(intent);

            }
        });

    }
}
