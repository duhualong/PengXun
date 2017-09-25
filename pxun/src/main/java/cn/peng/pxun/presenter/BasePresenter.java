package cn.peng.pxun.presenter;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.NetworkUtil;

/**
 * presenter的基类
 * @author Peng
 */
public abstract class BasePresenter {
    protected BaseActivity context;
    protected BaseFragment fragment;

    public BasePresenter(BaseActivity activity){
        this.context = activity;
    }

    public BasePresenter(BaseFragment fragment){
        this.fragment = fragment;
    }

    /**
     * 检测当的网络（WIFI、3G-/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetUsable(Context context) {
        return NetworkUtil.isNetworkAvailable(context);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isPhoneNumber(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
