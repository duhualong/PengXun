package cn.peng.pxun.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bean.Contacts;
import cn.peng.pxun.presenter.fragment.ContactPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.ContactAdapter;
import cn.peng.pxun.ui.view.SuperListView;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 发现界面的Fragment
 */
public class ContactFragment extends BaseFragment<ContactPresenter> {

    @BindView(R.id.lv_contact)
    SuperListView mLvContact;

    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.fragment_contact, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    protected ContactPresenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public void initListener() {
        mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = (String) parent.getAdapter().getItem(position);
                Intent intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        mLvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String username = (String) parent.getAdapter().getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                                            ToastUtil.showToast(activity, "删除好友成功");
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showToast(activity, "删除好友失败");
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

    @Override
    public void initData() {
        presenter.getContactList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void refreshContact(Contacts contacts) {
        if (contacts.signature.contains("同意")) {
            presenter.mUsernames.add(contacts.text);
            mLvContact.setAdapter(new ContactAdapter(presenter.mUsernames));
        }
    }

    public void bindView(List<String> usernames) {
        mLvContact.setAdapter(new ContactAdapter(usernames));
        mLvContact.onRefreshFinish();
    }
}
