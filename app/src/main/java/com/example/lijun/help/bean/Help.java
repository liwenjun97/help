package com.example.lijun.help.bean;

import java.io.Serializable;

/**
 * Created by lijun on 2018/1/11.
 */

public class Help implements Serializable {
    public   int  helpid;
    public   String  othername;
    public   int  uFinish;
    public   String helpContent;
    public   String helpPlace;
    public   String helpTitle;
    public   int helpPrice;
    public   String helpPublishTime;
    public   String helpActiveTime;
    public   String helpType;
    public   int  helpPeople;
    public   int  helpScore;//打分没用到
    public   static int  lasthelpid;//用于recylerview上拉加载更多

    public int getHelpid() {
        return helpid;
    }

    public void setHelpid(int helpid) {
        this.helpid = helpid;
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

    public String getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }

    public String getHelpPlace() {
        return helpPlace;
    }

    public void setHelpPlace(String helpPlace) {
        this.helpPlace = helpPlace;
    }

    public String getHelpTitle() {
        return helpTitle;
    }

    public void setHelpTitle(String helpTitle) {
        this.helpTitle = helpTitle;
    }

    public int getHelpPrice() {
        return helpPrice;
    }

    public void setHelpPrice(int helpPrice) {
        this.helpPrice = helpPrice;
    }

    public String getHelpPublishTime() {
        return helpPublishTime;
    }

    public void setHelpPublishTime(String helpPublishTime) {
        this.helpPublishTime = helpPublishTime;
    }

    public String getHelpActiveTime() {
        return helpActiveTime;
    }

    public void setHelpActiveTime(String helpActiveTime) {
        this.helpActiveTime = helpActiveTime;
    }

    public String getHelpType() {
        return helpType;
    }

    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }

    public int getHelpPeople() {
        return helpPeople;
    }

    public void setHelpPeople(int helpPeople) {
        this.helpPeople = helpPeople;
    }

    public int getHelpScore() {
        return helpScore;
    }

    public void setHelpScore(int helpScore) {
        this.helpScore = helpScore;
    }
}
