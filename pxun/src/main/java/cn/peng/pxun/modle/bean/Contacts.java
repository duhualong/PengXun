package cn.peng.pxun.modle.bean;

import android.graphics.drawable.Drawable;

/**
 * 联系人对象的实体类
 */
public class Contacts {
    public String userName;
    public String signature;
    public Drawable userIcon;

    public String url;
    public String text;
    public int code;

    public Contacts(String userName, String signature, Drawable userIcon) {
        this.userName = userName;
        this.signature = signature;
        this.userIcon = userIcon;
    }

    public Contacts() {
    }

}
