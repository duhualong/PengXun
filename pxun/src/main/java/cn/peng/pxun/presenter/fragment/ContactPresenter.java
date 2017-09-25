package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.ui.fragment.ContactFragment;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * Created by msi on 2016/12/26.
 */
public class ContactPresenter extends BasePresenter {
    private ContactFragment mFragment;
    public List<String> mUsernames;

    public ContactPresenter(BaseFragment fragment) {
        super(fragment);
        mFragment = (ContactFragment) fragment;
    }

    public void getContactList() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mUsernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mFragment.bindView(mUsernames);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
