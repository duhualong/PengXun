package cn.peng.pxun.modle.bmob;

import cn.bmob.v3.BmobUser;

/**
 * 用户实体
 * Created by msi on 2016/12/24.
 */

public class User extends BmobUser{
    //头像
    private String headIcon;
    //个性签名
    private String signaTure;

    //真实姓名
    private String name;
    //性别
    private String sex;
    //年龄
    private String age;
    //生日
    private String birthday;
    //所在地
    private String address;
    //QQ号
    private String qqNum;
    //微信号
    private String wechatNum;


    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getSignaTure() {
        return signaTure;
    }

    public void setSignaTure(String signaTure) {
        this.signaTure = signaTure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
