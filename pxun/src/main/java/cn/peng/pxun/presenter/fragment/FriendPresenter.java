package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.FriendFragment;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * Created by msi on 2016/12/26.
 */
public class FriendPresenter extends BasePresenter {
    private FriendFragment mFragment;

    public FriendPresenter(FriendFragment fragment) {
        super(fragment);
        mFragment = fragment;
    }

    /**
     * 从环信获取好友列表
     */
    public void getFriendList() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> userIds = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (String userId : userIds){
                        getUserFromBmob(userId);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
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
