package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

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
    public void getContactList() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> usernames = new ArrayList();
                    usernames.addAll(EMClient.getInstance().contactManager().getAllContactsFromServer());
                    mFragment.refreshContact(usernames);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
