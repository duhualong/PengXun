package cn.peng.pxun.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bean.Contacts;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;
import cn.peng.pxun.ui.adapter.holder.MessageHolder;
import cn.peng.pxun.ui.view.SuperListView;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 消息界面的Fragment
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.lv_message)
    SuperListView mLvMessage;

    private List<Contacts> mList;
    private MessageAdapter mAdapter;

    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);
        mList = new ArrayList<>();
        getList();
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_message, null);
        return view;
    }

    @Override
    public void initData() {
        mAdapter = new MessageAdapter(mList);
        mLvMessage.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mLvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, final int i, long l) {
                final String username = mList.get(i - 1).userName;
                if (username.equals("系统消息")) {
                    final String messageUser = mList.get(i - 1).text;
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("系统消息");
                    builder.setMessage(mList.get(i - 1).signature);
                    if (mList.get(i - 1).signature.contains("请求添加")) {
                        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            EMClient.getInstance().contactManager().acceptInvitation(messageUser);
                                            ThreadUtils.runOnMainThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mList.remove(i - 1);
                                                    mAdapter.setDataSets(mList);
                                                    mAdapter.notifyDataSetChanged();
                                                    ToastUtil.showToast(mActivity, "已同意" + messageUser + "的好友申请");
                                                }
                                            });
                                        } catch (HyphenateException e) {
                                            e.printStackTrace();
                                            ThreadUtils.runOnMainThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ToastUtil.showToast(mActivity, "消息处理失败");
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            EMClient.getInstance().contactManager().declineInvitation(messageUser);
                                            ThreadUtils.runOnMainThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mList.remove(i - 1);
                                                    mAdapter.setDataSets(mList);
                                                    mAdapter.notifyDataSetChanged();
                                                    ToastUtil.showToast(mActivity, "已拒绝" + messageUser + "的好友申请");
                                                }
                                            });
                                        } catch (HyphenateException e) {
                                            e.printStackTrace();
                                            ThreadUtils.runOnMainThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ToastUtil.showToast(mActivity, "消息处理失败");
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mList.remove(i - 1);
                                mAdapter.setDataSets(mList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    builder.create().show();
                } else {
                    Intent intent = new Intent(mActivity, ChatActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });
        mLvMessage.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                ThreadUtils.runOnSubThread(new Runnable() {
                    @Override
                    public void run() {
                        final List<Contacts> list = getList();
                        SystemClock.sleep(1000);
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setDataSets(list);
                                mAdapter.notifyDataSetChanged();
                                mLvMessage.onRefreshFinish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void hasNewMessage(Contacts contacts) {
        mList.add(contacts);
        mAdapter.setDataSets(mList);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getMessage(Message message) {
        mAdapter.setDataSets(getList());
        mAdapter.notifyDataSetChanged();
    }

    public List<Contacts> getList() {
        mList.clear();
        mList.add(new Contacts("智能小白", "最聪慧的小白,为您服务!", getResources().getDrawable(R.drawable.headicon1)));

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        if (conversations != null && conversations.size() > 0) {
            ArrayList<EMConversation> contacts = new ArrayList<>();
            contacts.addAll(conversations.values());
            Collections.sort(contacts, new Comparator<EMConversation>() {
                @Override
                public int compare(EMConversation o1, EMConversation o2) {
                    return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                }
            });
            for (EMConversation emConversation : contacts) {
                mList.add(new Contacts(emConversation.getUserName(), emConversation.getLastMessage().getBody().toString().split(":")[1].replaceAll("\"", " "), getResources().getDrawable(R.drawable.headicon1)));
            }
        }
        return mList;
    }

    private class MessageAdapter extends SuperBaseApapter {
        private MessageHolder mHolder;

        public MessageAdapter(List dataSets) {
            super(dataSets);
        }

        @Override
        public BaseHolder setHolder() {
            return new MessageHolder();
        }
    }
}
