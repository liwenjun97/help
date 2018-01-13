package com.example.lijun.help.bean;

import java.io.Serializable;

/**
 * Created by lijun on 2017/12/29.
 */

public class Service implements Serializable {

    public   int  serviceid;
    public   String  othername;
    public   int  uFinish;
    public   String serviceContent;
    public   String servicePlace;
    public   String serviceTitle;
    public   int servicePrice;
    public   String servicePublishTime;
    public   String serviceActiveTime;
    public   String serviceType;
    public   int  servicePeople;
    public   int  serviceScore;
    public   static int  lastserviceid;//用于recylerview上拉加载更多
    public int getLastserviceid() {
        return lastserviceid;
    }

    public void setLastserviceid(int lastserviceid) {
        this.lastserviceid = lastserviceid;
    }



    public int getServiceid() {
        return serviceid;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public int getuFinish() {
        return uFinish;
    }

    public void setuFinish(int uFinish) {
        this.uFinish = uFinish;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getServicePlace() {
        return servicePlace;
    }

    public void setServicePlace(String servicePlace) {
        this.servicePlace = servicePlace;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public  int getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServicePublishTime() {
        return servicePublishTime;
    }

    public void setServicePublishTime(String servicePublishTime) {
        this.servicePublishTime = servicePublishTime;
    }

    public String getServiceActiveTime() {
        return serviceActiveTime;
    }

    public void setServiceActiveTime(String serviceActiveTime) {
        this.serviceActiveTime = serviceActiveTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getServicePeople() {
        return servicePeople;
    }

    public void setServicePeople(int servicePeople) {
        this.servicePeople = servicePeople;
    }

    public int getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(int serviceScore) {
        this.serviceScore = serviceScore;
    }
}
