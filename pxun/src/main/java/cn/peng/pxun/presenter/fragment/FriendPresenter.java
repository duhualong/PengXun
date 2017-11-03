package cn.peng.pxun.presenter.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.FriendFragment;

/**
 * Created by msi on 2016/12/26.
 */
public class FriendPresenter extends BaseUserPresenter {
    private FriendFragment mFragment;

    public FriendPresenter(FriendFragment fragment) {
        super(fragment);
        mFragment = fragment;
    }

    /**
     * 从环信获取好友列表
     */
    public void getFriendList() {
        List<String> userIds = getFriendListFromHuanXin();
        for (String userId : userIds){
            getUserFromBmob(userId);
        }
    }

    /**
     * 从Bmob服务器获取用户信息
     * @param userId
     * @return
     */
    private void getUserFromBmob(String userId) {
        userId = userId.toUpperCase();
        BmobQuery<User> bmobQuery = new BmobQuery();
        List<BmobQuery<User>> params = new ArrayList<>();
        params.add(new BmobQuery<User>().addWhereEqualTo("thirdPartyID", userId));
        params.add(new BmobQuery<User>().addWhereEqualTo("mobilePhoneNumber", userId));
        bmobQuery.or(params);
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User user = list.get(0);
                    mFragment.refreshFriend(user);
                }
            }
        });
    }
}
