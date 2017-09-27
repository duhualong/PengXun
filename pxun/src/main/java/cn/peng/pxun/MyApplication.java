package cn.peng.pxun;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.greendao.database.Database;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.Contacts;
import cn.peng.pxun.modle.greendao.DaoMaster;
import cn.peng.pxun.modle.greendao.DaoSession;
import cn.peng.pxun.modle.greendao.Message;
import de.greenrobot.event.EventBus;

public class MyApplication extends Application {
    /** 全局唯一的上下文 */
    public static Context context;
    /** 全局唯一的主线程Handler */
    public static Handler handler;
    /** 全局唯一的SharedPreferences */
    public static SharedPreferences sp;
    /** 获取Dao */
    public static DaoSession session;

    public static UMShareAPI umengApi;

    {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1106448540", "rdvp5djKePx2QPzC");
        PlatformConfig.setSinaWeibo("919366623", "4c2578103b186d7468a9b08fb0fca497", "http://sns.whalecloud.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initBmob();
        initUMeng();
        initHuanXin();
        initHuanXinListener();
        initDatabase();

        context = getApplicationContext();
        handler = new Handler();
        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
    }

    /**
     * 初始化Bmob后端云
     */
    private void initBmob() {
        Bmob.initialize(this, AppConfig.BMOB_APP_KEY);
    }

    /**
     * 初始化友盟
     */
    private void initUMeng() {
        umengApi = UMShareAPI.get(this);
    }

    /**
     * 初始化环信
     */
    private void initHuanXin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    /**
     * 初始化消息和联系人的监听
     */
    private void initHuanXinListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAgreed(final String username) {
                //好友请求被同意
                Contacts admin = new Contacts();
                admin.userName = "系统消息";
                admin.userIcon = getResources().getDrawable(R.drawable.peng);
                admin.signature = username +"同意了你的好友申请.";
                EventBus.getDefault().post(admin);
            }

            @Override
            public void onContactRefused(final String username) {
                //好友请求被拒绝
                Contacts admin = new Contacts();
                admin.userName = "系统消息";
                admin.userIcon = getResources().getDrawable(R.drawable.peng);
                admin.signature = username +"拒绝了你的好友申请.";
                EventBus.getDefault().post(admin);
            }

            @Override
            public void onContactInvited(final String username, String reason) {
                //收到好友邀请
                Contacts admin = new Contacts();
                admin.userName = "系统消息";
                admin.userIcon = getResources().getDrawable(R.drawable.peng);
                admin.signature = username +":请求添加你为好友,是否同意?";
                admin.text = username;
                EventBus.getDefault().post(admin);
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
            }

            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(final List<EMMessage> messages) {
                //收到消息
                if (messages != null && messages.size() > 0) {
                    Message message = new Message();
                    message.fromUserID = messages.get(0).getFrom();
                    message.toUserID = messages.get(0).getTo();
                    message.message = messages.get(0).getBody().toString().split(":")[1].replaceAll("\""," ");
                    message.messageType = Message.TEXT_TYPE;
                    EventBus.getDefault().post(message);
                }
            }

            @Override
            public void onCmdMessageReceived(final List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        });
    }


    /**
     * 初始化数据库(GreenDao)
     */
    public void initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "peng.db", null);
        Database db = helper.getWritableDb();
        session = new DaoMaster(db).newSession();
    }

    /**\
     * 获取应用的名称
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
