package com.example.lijun.help.bean;

/**
 * Created by lijun on 2017/12/29.
 */

public class User {
    public static int uId;
    public static int upoint;
    public static String uToken = "";           //默认为空
    public static String uName;
    public static String uEmail;
    public static String uPas;   //password;
    public static String uRegTime;
    public static String uImgSrc;

    public static int uAge;
    public static String uBirth;
    public static int uSex;

    public static String ustudentid;
    public static String uCity;




    public static void setUserInfo(int id,String name,String email,String pwd,String regTime,String imgSrc,String city,String birth,String studentid,int age,int sex,int point){
        uId = id;
        uName = name;
        uEmail = email;
        uPas = pwd;
        uRegTime = regTime;
        uImgSrc = imgSrc;
        uCity = city;
        uBirth = birth;
        uAge = age;
        uSex = sex;
        ustudentid =studentid ;
        upoint=point;

    //    dreamClickNum = dreamclicknum;
  //      dreamCommentNum = dreamcommentnum;
//        UserSql.addToSql();               //保存到本地数据库
    }

    //Get||Set
    public static int getuId() {
        return uId;
    }

    public static String getuName() {
        return uName;
    }

    public static void setuId(int id) {
        uId = id;
    }
}
