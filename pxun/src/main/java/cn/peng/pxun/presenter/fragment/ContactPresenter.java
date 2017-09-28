package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.ContactFragment;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * Created by msi on 2016/12/26.
 */
public class ContactPresenter extends BasePresenter {
    private ContactFragment mFragment;

    public ContactPresenter(ContactFragment fragment) {
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
                    usernames.add("智能小白");
                    usernames.addAll(EMClient.getInstance().contactManager().getAllContactsFromServer());
                    mFragment.refreshContact(usernames);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
