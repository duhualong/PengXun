package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.MessageFragment;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by msi on 2017/9/27.
 */

public class MessagePresenter extends BaseUserPresenter{
    private MessageFragment mFragment;

    private int index = 0;
    private boolean loadMessageState = false;
    private ArrayList<EMConversation> emConversations;

    public MessagePresenter(MessageFragment fragment) {
        super(fragment);
        mFragment = fragment;

        addUserInfoListener(new UserInfoListener() {
            @Override
            public void onGetUser(User user) {
                ConversationBean conversation = getConversation(user);

                index += 1;
                if (index < emConversations.size()){
                    getUser(emConversations.get(index).getUserName());
                }else {
                    loadMessageState = false;
                }

                mFragment.refreshMessage(conversation);
            }
        });
    }

    /**
     * 开始加载用户信息
     */
    private void startLoadUser() {
        index = 0;
        loadMessageState = true;
        getUser(emConversations.get(index).getUserName());
    }

    /**
     * 获取会话实体
     * @param user
     * @return
     */
    private ConversationBean getConversation(User user) {
        EMConversation emConversation = emConversations.get(index);
        ConversationBean conversation = new ConversationBean();
        conversation.user = user;
        conversation.lastMsg = splitEmMessage(emConversation.getLastMessage());
        conversation.lastChatTime = emConversation.getLastMessage().getMsgTime();
        conversation.unreadCount = emConversation.getUnreadMsgCount();
        conversation.isGroup = emConversation.isGroup();
        return conversation;
    }

    /**
     * 获取会话列表
     */
    public void getMessageList() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
                if (conversationMap != null && conversationMap.size() > 0) {
                    emConversations = new ArrayList();
                    emConversations.addAll(conversationMap.values());
                    if (emConversations.size() > 0){
                        Collections.sort(emConversations, new Comparator<EMConversation>() {
                            @Override
                            public int compare(EMConversation o1, EMConversation o2) {
                                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                            }
                        });

                        startLoadUser();
                    }else {
                        mFragment.refreshMessage(null);
                    }
                }

            }
        });
    }

    /**
     * 获取现在是否正在加载好会话消息
     * @return
     */
    public boolean isLoadingMessage(){
        return loadMessageState;
    }

}
