package cn.peng.pxun.presenter.activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.RegistActivity;
import cn.peng.pxun.utils.MD5Util;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * RegistActivity的业务类
 */
public class RegistPresenter extends BasePresenter{

    private RegistActivity activity;
    //当前注册的用户
    private User user;

    public RegistPresenter(BaseActivity activity) {
        super(activity);
        this.activity = (RegistActivity) activity;
    }

    /**
     * 注册
     * @param phone 用户登录账号，手机号
     * @param username 用户名，昵称
     * @param password 密码
     * @param sex 用户性别
     * @param birthday 用户生日
     * @param address 用户所在地
     */
    public void regist(String phone, String username, final String password, String sex, String birthday, String address) {
        if (!isNetUsable(activity)){
            activity.onRegistFinish(AppConfig.NET_ERROR, 100, "");
            return;
        }
        if (!isPhoneNumber(phone)){
            activity.onRegistFinish(AppConfig.NUMBER_ERROR, 101, "");
            return;
        }
        if("未选择".equals(sex)){
            sex = "";
        }
        if("未选择".equals(birthday)){
            birthday = "";
        }
        if("未选择".equals(address)){
            address = "";
        }

        //注册到自己的Bmob服务器
        user = new User();
        user.setMobilePhoneNumber(phone);
        user.setUsername(username);
        user.setPassword(MD5Util.encode(password));
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setAddress(address);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if (e == null){
                    registHuanXin(password);
                }else {
                    setResult(AppConfig.SERVER_ERROR, 500, "");
                }
            }
        });
    }

    /**
     * 注册环信服务器
     * @param password
     */
    private void registHuanXin(final String password) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userNum = user.getMobilePhoneNumber();
                    EMClient.getInstance().createAccount(userNum, MD5Util.encode(password));
                    //注册成功
                    setResult(AppConfig.SUCCESS, 200, userNum + ":" + password);
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                    //将Bmob上注册的user给删除掉
                    user.delete();
                    //注册失败
                    setResult(AppConfig.ERROR, e1.getErrorCode(), "");
                }
            }
        });
    }

    /**
     * 更新activity的注册结果
     * @param code
     * @param userInfo
     */
    private void setResult (final int code, final int huanXinCode, final String userInfo) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                activity.onRegistFinish(code, huanXinCode, userInfo);
            }
        });
    }
}
