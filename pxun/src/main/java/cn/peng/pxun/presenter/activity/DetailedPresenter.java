package cn.peng.pxun.presenter.activity;

import java.util.List;

import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BaseUserPresenter;
import cn.peng.pxun.ui.activity.DetailedActivity;

/**
 * Created by msi on 2017/10/20.
 */

public class DetailedPresenter extends BaseUserPresenter{
    private DetailedActivity activity;

    public DetailedPresenter(DetailedActivity activity) {
        super(activity);
        this.activity = activity;
        getFriendListFromHuanXin();

        addFriendListListener(new FriendListListener() {
            @Override
            public void onGetFriendList(List<String> userIds) {
                DetailedPresenter.this.activity.startInit();
            }
        });
        addUserInfoListener(new UserInfoListener() {
            @Override
            public void onGetUser(User user) {
                DetailedPresenter.this.activity.setUserInfo(user);
            }
        });
    }

    /**
     * 获取用户信息
     * @param accountNumber
     */
    public void getUserInfo(String accountNumber){
        if ("tuling".equals(accountNumber)){
            User user = new User();
            user.setUsername("图灵小白");
            user.setSex("女");
            user.setAddress("北京市朝阳区");
            user.setBirthday("2016年11月11日");
            activity.setUserInfo(user);
        }else{
            getUser(accountNumber);
        }
    }
}
