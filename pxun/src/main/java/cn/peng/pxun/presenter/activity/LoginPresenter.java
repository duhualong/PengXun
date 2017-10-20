package cn.peng.pxun.presenter.activity;

import android.content.SharedPreferences;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.LoginActivity;
import cn.peng.pxun.utils.MD5Util;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * LoginActivity的业务类
 */
public class LoginPresenter extends BasePresenter{
    private LoginActivity activity;
    private String phone;
    private String password;
    private boolean isThirdPartyLogin = false;

    public LoginPresenter(BaseActivity activity) {
        super(activity);
        this.activity = (LoginActivity)activity;
    }

    /**
     * 查询该三方用户是否注册过
     * 未注册用户先注册后登陆
     * @param user
     */
    public void thirdPartyUserRegiest(final User user) {
        isThirdPartyLogin = true;
        BmobQuery<User> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("thirdPartyID", user.getThirdPartyID());
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    if(list != null && list.size() > 0){
                        loginHuanXin(user.getThirdPartyID(), MD5Util.encode("pxun123456"));
                    }else{
                        regiestUser(user);
                    }
                }else{
                    e.printStackTrace();
                    regiestUser(user);
                }
            }
        });
    }

    /**
     * 注册用户
     * @param user
     */
    private void regiestUser(final User user){
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(user.getThirdPartyID(), MD5Util.encode("pxun123456"));
                    //注册成功
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(final User user, BmobException e) {
                            if (e == null){
                                loginHuanXin(user.getThirdPartyID(), MD5Util.encode("pxun123456"));
                            }else {
                                setResult(AppConfig.SERVER_ERROR, 500);
                            }
                        }
                    });
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                    user.delete();
                    setResult(AppConfig.ERROR, e1.getErrorCode());
                }
            }
        });

    }

    /**
     * 登录帐号
     * @param phone
     * @param password
     * @return
     */
    public void login(String phone, final String password) {
        if (!isNetUsable(activity)){
            activity.onLoginFinish(AppConfig.NET_ERROR, 100);
            return;
        }

        this.phone = phone;
        this.password = password;

        //登录环信服务器、
        loginHuanXin(phone, password);
    }

    private void loginHuanXin(String accountNumber, String password) {
        EMClient.getInstance().login(accountNumber, MD5Util.encode(password),new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                setResult(AppConfig.SUCCESS, 200);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                setResult(AppConfig.ERROR, code);
            }
        });
    }

    /**
     * 传递数据给actiivty
     * @param code
     * @param huanXinCode
     */
    private void setResult(final int code, final int huanXinCode) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                activity.onLoginFinish(code, huanXinCode);
            }
        });
    }

    /**
     * 保存用户的登录信息
     * @param isRememberPassword
     */
    public void keepUserLoginInfo(boolean isRememberPassword) {
        if (!isThirdPartyLogin){
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
    }

    /**
     * 获取已登录用户的账号
     * @return
     */
    public static String getKeepUserPhone(){
        return MyApplication.sp.getString("phone","");
    }

}
