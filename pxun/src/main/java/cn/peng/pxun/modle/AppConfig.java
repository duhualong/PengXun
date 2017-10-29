package cn.peng.pxun.modle;

import cn.peng.pxun.modle.bmob.User;

/**
 * 保存常量字符串的类
 */
public class AppConfig {
    /** 图林机器人是否初始化成功 */
    public static boolean isInitTuring;
    /** 当前登录用户 */
    public static User appUser;

    /** 是否是调试模式，用于显示log */
    public static final boolean DEBUG_ENABLE = true;
    /** log日志的tag */
    public static final String DEBUG_TAG = "pxun";
    /** SharedPreferences文件名称 */
    public static final String SharedPreferencesName = "config";

    /** 网络异常 */
    public static final int NET_ERROR = -10000;
    /** 成功 */
    public static final int SUCCESS = 10000;
    /** 错误 */
    public static final int ERROR = 10001;
    /** Bmob服务器错误*/
    public static final int SERVER_ERROR = 10002;
    /** 帐号格式错误,帐号不存在 */
    public static final int NUMBER_ERROR = 10010;

    /** 请求码：登录到注册*/
    public static final int LOGINTOREGIEST = 1000;


    /** BMOB后端云appkey */
    public static final String BMOB_APP_KEY = "aace143390901d901c3ce3a8c1d9d009";
    /** 图灵机器人appkey */
    public static final String TURING_APP_KEY = "93ae478490f141f6ad870b4d3b191068";
    /** 科大讯飞appid */
    public static final String IFLYTEK_APPID = "57fce417";

    public static final String[] MENU_ITEMS = {
        "添加好友",
        "创建聊天群",
        "加入聊天群",
        "反馈意见",
        "设置",
    };

    public static final String[] USER_SEX = {
            "男","女","保密"
    };

    public static final String[] CONSTELLATION = {
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
    };

    public static final String[] BELLE_PIC = {
        "http://p0.so.qhimg.com/bdr/1080__/t019f317413b32eb4ba.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t01702d70f973322346.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t0164aabb64a8236121.jpg",
        "http://p0.so.qhimg.com/bdr/1080__/t01ad3641a09574b2f5.jpg",
        "http://p0.so.qhimg.com/bdr/1080__/t01c3585927dc7b419a.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t019c338075a61bd03b.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t019c338075a61bd03b.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t0173961b44fea171a6.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t0173961b44fea171a6.jpg",
        "http://p2.so.qhimg.com/bdr/1080__/t014fca575548dc61e7.jpg",
        "http://p2.so.qhimg.com/bdr/1080__/t010cd2d424a97e08d2.jpg",
        "http://p2.so.qhimg.com/bdr/1080__/t015fefa32e2bf46597.jpg",
        "http://p3.so.qhimg.com/bdr/1080__/t01b5ba1d3e18b45ae3.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t01a7ce4d164d5c15fb.jpg",
        "http://p4.so.qhimg.com/bdr/_960_/t01941a8203d49382d9.jpg",
        "http://p3.so.qhimg.com/bdr/1080__/t016bbbb42b42830428.jpg",
        "http://p1.so.qhimg.com/bdr/_960_/t010b6379438bf233f6.jpg",
        "http://p2.so.qhimg.com/bdr/1080__/t015fefa32e2bf46597.jpg",
        "http://p1.so.qhimg.com/bdr/1080__/t01e93105df8d93c2eb.jpg",
        "http://p0.so.qhimg.com/bdr/1080__/t0121c248a899ad2530.jpg",
        "http://p2.so.qhimg.com/bdr/1080__/t01fb3e43c8cd9ee917.jpg",
    };

    /**
     * 获取当前登录用户的注册Id
     * @return
     */
    public static String getUserId(User user){
        if (user != null){
            if ("QQ".equals(user.getLoginType()) || "SINA".equals(user.getLoginType())){
                return user.getThirdPartyID();
            } else {
                return user.getMobilePhoneNumber();
            }
        }else{
            return "pxun";
        }
    }
}
