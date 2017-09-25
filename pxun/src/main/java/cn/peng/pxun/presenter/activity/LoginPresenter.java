package cn.peng.pxun.presenter.activity;

import android.content.SharedPreferences;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.Constant;
import cn.peng.pxun.modle.bean.User;
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
    public void login(String phone,String password) {
        this.phone = phone;
        this.password = password;

        if (!isNetUsable(activity)){
            activity.onLogin(Constant.NET_ERROR);
            return;
        }

        //登录环信服务器
        EMClient.getInstance().login(phone,password,new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.onLogin(Constant.LOGIN_SUCCESS);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.onLogin(Constant.LOGIN_ERROR);
                    }
                });
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

    public void getUser() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("mobilePhoneNumber", phone);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list,BmobException e) {

            }
        });
    }

    public static String getKeepUserPhone(){
        return MyApplication.sp.getString("phone","");
    }
}
