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
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.fragment.FriendPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.FriendAdapter;
import cn.peng.pxun.ui.view.SuperListView;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 联系人界面的Fragment
 */
public class FriendFragment extends BaseFragment<FriendPresenter> {

    @BindView(R.id.lv_contact)
    SuperListView mLvContact;

    private List<User> friendList;
    private FriendAdapter mAdapter;


    @Override
    public void init() {
        super.init();
        //EventBus.getDefault().register(this);
        friendList = new ArrayList<>();
        presenter.getFriendList();
    }

    @Override
    protected FriendPresenter initPresenter() {
        return new FriendPresenter(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_contact, null);
        return view;
    }

    @Override
    public void initData() {
        mAdapter = new FriendAdapter(friendList);
        mLvContact.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = friendList.get(position-1);

                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("isGroup", false);
                intent.putExtra("userId", AppConfig.getUserId(user));
                intent.putExtra("username", user.getUsername());
                startActivity(intent);
            }
        });
        mLvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final User user = friendList.get(position-1);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("系统消息");
                builder.setMessage("您确定要删除好友" + user.getUsername() + "吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThreadUtils.runOnSubThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().contactManager().deleteContact(AppConfig.getUserId(user));
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
                friendList.clear();
                presenter.getFriendList();
            }
        });
    }

    public void refreshFriend(User user) {
        if (mLvContact != null &&mAdapter != null ) {
            friendList.add(user);
            mAdapter.setDataSets(friendList);
            if (mLvContact.isRefresh()) {
                mLvContact.onRefreshFinish();
            }
        }
    }
}
