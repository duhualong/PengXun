package cn.peng.pxun.presenter.activity;

import android.content.SharedPreferences;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.LoginActivity;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * LoginActivity的业务类
 */
public class LoginPresenter extends BasePresenter{
    private LoginActivity activity;
    private String phone;
    private String password;

    public LoginPresenter(BaseActivity activity) {
        super(activity);
        this.activity = (LoginActivity)activity;
    }

    /**
     * 登录帐号
     * @param phone
     * @param password
     * @return
     */
    public void login(String phone, final String password) {
        if (!isNetUsable(activity)){
            activity.onLogin(AppConfig.NET_ERROR);
            return;
        }

        this.phone = phone;
        this.password = password;

        //登录环信服务器
        EMClient.getInstance().login(phone,password,new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                setResult(AppConfig.SUCCESS);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                setResult(AppConfig.ERROR);
            }
        });
    }

    private void setResult(final int code) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                activity.onLogin(code);
            }
        });
    }

    /**
     * 保存用户的登录信息
     * @param isRememberPassword
     */
    public void keepUserLoginInfo(boolean isRememberPassword) {
        SharedPreferences.Editor editor = MyApplication.sp.edit();
        editor.putString("phone",phone);
        editor.putBoolean("isRemember",isRememberPassword);
        editor.putBoolean("isLogin",true);
        if (isRememberPassword){
            editor.putString("password",password);
        }else {
            editor.putString("password","");
        }
        editor.commit();

    }

    /**
     * 获取已登录用户的账号
     * @return
     */
    public static String getKeepUserPhone(){
        return MyApplication.sp.getString("phone","");
    }
}
