package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.GroupFragment;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by msi on 2016/12/26.
 */
public class GroupPresenter extends BasePresenter{
    private GroupFragment mFragment;

    public GroupPresenter(GroupFragment fragment) {
        super(fragment);
        mFragment = fragment;
    }

    /**
     * 从服务器获取自己加入的和创建的群组列表
     * @return
     */
    public void getGroupList() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    ThreadUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mFragment.refreshGroup(grouplist);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
