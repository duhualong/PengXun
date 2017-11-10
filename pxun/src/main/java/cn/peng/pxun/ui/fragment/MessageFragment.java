package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.presenter.fragment.MessagePresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.activity.ContactActivity;
import cn.peng.pxun.ui.activity.SysMessageActivity;
import cn.peng.pxun.ui.adapter.listview.MessageAdapter;
import cn.peng.pxun.ui.view.SuperListView;

/**
 * 消息界面的Fragment
 */
public class MessageFragment extends BaseFragment<MessagePresenter> {

    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.iv_title_friend)
    ImageView mIvTitleFriend;
    @BindView(R.id.lv_message)
    SuperListView mLvMessage;

    private List<ConversationBean> messageList;
    private MessageAdapter mAdapter;

    @Override
    public void init() {
        super.init();
        EventBus.getDefault().register(this);
        messageList = new ArrayList<>();
        messageList.add(new ConversationBean(new User("admin", "系统消息")));
        messageList.add(new ConversationBean(new User("tuling", "智能小白")));
        presenter.getMessageList();
    }

    @Override
    protected MessagePresenter initPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_message, null);
        return view;
    }

    @Override
    public void initData() {
        mTvTitleText.setText("消息");
        mIvTitleFriend.setVisibility(View.VISIBLE);
        mAdapter = new MessageAdapter(messageList);
        mLvMessage.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mIvTitleFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ContactActivity.class);
                startActivity(intent);
            }
        });
        mLvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, final int i, long l) {
                ConversationBean conversation = messageList.get(i - 1);

                Intent intent = new Intent();
                if ("系统消息".equals(conversation.user.getUsername())) {
                    intent.setClass(mActivity, SysMessageActivity.class);
                } else {
                    intent.setClass(mActivity, ChatActivity.class);
                    intent.putExtra("isGroup", conversation.isGroup);
                    intent.putExtra("userId", AppConfig.getUserId(conversation.user));
                    intent.putExtra("username", conversation.user.getUsername());
                }
                startActivity(intent);
            }
        });
        mLvMessage.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                if (!presenter.isLoadingMessage()){
                    messageList.clear();
                    messageList.add(new ConversationBean(new User("admin", "系统消息")));
                    messageList.add(new ConversationBean(new User("tuling", "智能小白")));
                    presenter.getMessageList();
                }else {
                    mLvMessage.onRefreshFinish();
                }
            }
        });
    }

    /**
     * 刷新消息界面
     * @param conversation
     */
    public void refreshMessage(ConversationBean conversation) {
        if (mLvMessage != null && mAdapter != null) {
            if (mLvMessage.isRefresh() && !presenter.isLoadingMessage()) {
                mLvMessage.onRefreshFinish();
            }

            if (conversation != null){
                messageList.add(conversation);
                mAdapter.setDataSets(messageList);
            }
        }
    }

    /**
     * 收到新的消息
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(Message msg) {
        for (ConversationBean conversation : messageList){

        }
    }
}
