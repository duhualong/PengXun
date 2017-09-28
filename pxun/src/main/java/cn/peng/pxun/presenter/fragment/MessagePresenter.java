package cn.peng.pxun.presenter.fragment;

import android.os.SystemClock;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.MessageFragment;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * Created by msi on 2017/9/27.
 */

public class MessagePresenter extends BasePresenter{
    private MessageFragment mFragment;

    public MessagePresenter(MessageFragment fragment) {
        super(fragment);
        mFragment = fragment;
    }

    /**
     * 获取会话列表
     */
    public void getMessageList() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
                ArrayList<ConversationBean> conversations = new ArrayList<>();
                conversations.add(new ConversationBean("10000","系统消息"));
                if (conversationMap != null && conversationMap.size() > 0) {
                    ArrayList<EMConversation> emConversations = new ArrayList<EMConversation>();
                    emConversations.addAll(conversationMap.values());
                    if (emConversations.size() > 0){
                        Collections.sort(emConversations, new Comparator<EMConversation>() {
                            @Override
                            public int compare(EMConversation o1, EMConversation o2) {
                                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                            }
                        });
                        for (EMConversation emConversation : emConversations){
                            ConversationBean conversation = new ConversationBean();
                            conversation.userId = emConversation.getUserName();
                            conversation.userName = emConversation.getUserName();
                            conversation.lastMsg = splitEmMessage(emConversation.getLastMessage());
                            conversation.lastChatTime = emConversation.getLastMessage().getMsgTime();
                            conversation.isGroup = emConversation.isGroup();
                            conversations.add(conversation);
                        }
                    }
                }
                mFragment.refreshMessage(conversations);
            }
        });
    }
}
