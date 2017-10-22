package cn.peng.pxun.presenter.activity;

import android.util.Log;

import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;

/**
 * Created by msi on 2017/10/20.
 */

public class MainPresenter extends BasePresenter{

    public MainPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 获取已登录的用户信息
     */
    public void setAppUser() {
        String userId = MyApplication.sp.getString("userId","");
        BmobQuery<User> bmobQuery = new BmobQuery();
        List<BmobQuery<User>> params = new ArrayList<>();
        params.add(new BmobQuery<User>().addWhereEqualTo("thirdPartyID", userId));
        params.add(new BmobQuery<User>().addWhereEqualTo("mobilePhoneNumber", userId));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    AppConfig.appUser = list.get(0);
                }
            }
        });
    }

    /**
     * 初始化图灵机器人
     */
    public void initTuLing() {
        SDKInitBuilder builder = new SDKInitBuilder(context).setSecret("77bd9b637dd3aff6").
                setTuringKey(AppConfig.TURING_APP_KEY).setUniqueId("1136313078");
        SDKInit.init(builder, new InitListener() {
            @Override
            public void onComplete() {
                AppConfig.isInitTuring = true;
                Log.i("ChatActivity","图灵机器人初始化成功");
            }
            @Override
            public void onFail(String s) {
                AppConfig.isInitTuring = false;
                Log.i("ChatActivity","图灵机器人初始化失败");
            }
        });
    }
}
