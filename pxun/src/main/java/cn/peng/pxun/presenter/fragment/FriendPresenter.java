package cn.peng.pxun.presenter.fragment;

import java.util.List;

import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.FriendFragment;

/**
 * Created by msi on 2016/12/26.
 */
public class FriendPresenter extends BaseUserPresenter {
    private FriendFragment mFragment;

    private int index = 0;
    private boolean loadFriendState = false;

    public FriendPresenter(final FriendFragment fragment) {
        super(fragment);
        mFragment = fragment;

        addFriendListListener(new FriendListListener() {
            @Override
            public void onGetFriendList(List<String> userIds) {
                if (userIds != null && userIds.size() > 0){
                    startLoadUser();
                }else {
                    mFragment.setEmptyPage();
                }
            }
        });
        addUserInfoListener(new UserInfoListener() {
            @Override
            public void onGetUser(User user) {
                index += 1;
                if (index < friendIds.size()){
                    getUser(friendIds.get(index));
                }else {
                    loadFriendState = false;
                }
                mFragment.refreshFriend(user);
            }
        });
    }

    /**
     * 开始加载用户信息
     */
    private void startLoadUser() {
        index = 0;
        loadFriendState = true;
        getUser(friendIds.get(index));
    }

    /**
     * 从环信获取好友列表
     */
    public void getFriendList() {
        getFriendListFromHuanXin();
    }

    /**
     * 获取现在是否正在加载好友信息
     * @return
     */
    public boolean isLoadingFriend(){
        return loadFriendState;
    }
}
