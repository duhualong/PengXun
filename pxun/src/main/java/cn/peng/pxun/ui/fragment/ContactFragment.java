package cn.peng.pxun.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.fragment.ContactPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.ContactAdapter;
import cn.peng.pxun.ui.view.SuperListView;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 联系人界面的Fragment
 */
public class ContactFragment extends BaseFragment<ContactPresenter> {

    @BindView(R.id.lv_contact)
    SuperListView mLvContact;

    private List<String> contactList;
    private ContactAdapter mAdapter;


    @Override
    public void init() {
        super.init();
        //EventBus.getDefault().register(this);
        contactList = new ArrayList<>();
        presenter.getContactList();
    }

    @Override
    protected ContactPresenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_contact, null);
        return view;
    }

    @Override
    public void initData() {
        contactList = new ArrayList<>();
        contactList.add("智能小白");
        mAdapter = new ContactAdapter(contactList);
        mLvContact.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = contactList.get(position-1);

                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("isGroup", false);
                intent.putExtra("userId", username);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        mLvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String username = (String) parent.getAdapter().getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("系统消息");
                builder.setMessage("您确定要删除好友" + username + "吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThreadUtils.runOnSubThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().contactManager().deleteContact(username);
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showToast(mActivity, "删除好友成功");
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showToast(mActivity, "删除好友失败");
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

                return true;
            }
        });
        mLvContact.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                presenter.getContactList();
            }
        });
    }

    public void refreshContact(final List<String> usernames) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mLvContact != null &&mAdapter != null ) {
                    contactList = usernames;
                    mAdapter.setDataSets(contactList);
                    if (mLvContact.isRefresh()) {
                        mLvContact.onRefreshFinish();
                    }
                }
            }
        });
    }
}
