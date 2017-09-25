package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.ui.fragment.GroupFragment;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * Created by msi on 2016/12/26.
 */
public class GroupPresenter extends BasePresenter{
    private GroupFragment mFragment;
    private List<EMGroup> mGrouplist;

    public GroupPresenter(BaseFragment fragment) {
        super(fragment);
        mFragment = (GroupFragment) fragment;
    }

    /**
     * 从服务器获取自己加入的和创建的群组列表
     * @return
     */
    public void getGroupList() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mGrouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mFragment.bindView(mGrouplist);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
