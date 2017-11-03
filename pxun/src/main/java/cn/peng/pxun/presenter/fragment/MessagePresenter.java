package cn.peng.pxun.presenter.fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.MessageFragment;
import cn.peng.pxun.utils.ThreadUtil;

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
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
                ArrayList<ConversationBean> conversations = new ArrayList<>();
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
                            getUserFromBmob(emConversation);
                        }
                    }
                }

            }
        });
    }

    /**
     * 从Bmob服务器获取用户信息
     * @param emConversation
     * @return
     */
    private void getUserFromBmob(final EMConversation emConversation) {
        String userId = emConversation.getUserName().toUpperCase();
        BmobQuery<User> bmobQuery = new BmobQuery();
        List<BmobQuery<User>> params = new ArrayList<>();
        params.add(new BmobQuery<User>().addWhereEqualTo("thirdPartyID", userId));
        params.add(new BmobQuery<User>().addWhereEqualTo("mobilePhoneNumber", userId));
        bmobQuery.or(params);
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    ConversationBean conversation = new ConversationBean();
                    conversation.user = list.get(0);
                    conversation.lastMsg = splitEmMessage(emConversation.getLastMessage());
                    conversation.lastChatTime = emConversation.getLastMessage().getMsgTime();
                    conversation.unreadCount = emConversation.getUnreadMsgCount();
                    conversation.isGroup = emConversation.isGroup();
                    mFragment.refreshMessage(conversation);
                }
            }
        });
    }
}
