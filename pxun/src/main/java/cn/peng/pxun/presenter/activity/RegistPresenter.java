package cn.peng.pxun.presenter.activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.Constant;
import cn.peng.pxun.modle.bean.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.RegistActivity;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * RegistActivity的业务类
 */
public class RegistPresenter extends BasePresenter{
    private RegistActivity activity;

    public RegistPresenter(BaseActivity activity) {
        super(activity);
        this.activity = (RegistActivity) activity;
    }

    /**
     * 注册
     * @param phone
     * @param username
     * @param password
     * @param sex
     * @param birthday
     * @param address
     */
    public void regist(final String phone, String username, final String password, String sex, String birthday, String address) {
        if (!isNetUsable(activity)){
            activity.onRegist(Constant.NET_ERROR,null);
            return;
        }
        if (!isPhoneNumber(phone)){
            activity.onRegist(Constant.NUMBER_ERROR,null);
            return;
        }

        //注册到自己的Bmob服务器
        User user = new User();
        user.setUsername(username);
        user.setMobilePhoneNumber(phone);
        user.setPassword(password);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setAddress(address);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if (e == null){
                    //注册环信
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(phone, password);
                                //环信注册成功
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.onRegist(Constant.REGIST_SUCCESS,user);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //将Bmob上注册的user给删除掉
                                user.delete();
                                //环信注册失败了
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.onRegist(Constant.REGIST_ERROR,null);
                                    }
                                });
                            }
                        }
                    });
                }else {
                    activity.onRegist(Constant.REGIST_ERROR,null);
                }
            }
        });
    }
}
