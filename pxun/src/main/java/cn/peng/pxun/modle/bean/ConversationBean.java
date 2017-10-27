package cn.peng.pxun.modle.bean;

/**
 * 会话消息的实体
 * Created by tofirst on 2017/9/28.
 */

public class ConversationBean {
    // 用户登录账号
    public String userId;
    // 用户名
    public String userName;
    // 用户头像
    public String headIcon;
    // 最后一条消息
    public String lastMsg;
    // 最后一次聊天时间
    public long lastChatTime;
    // 是否是群组
    public boolean isGroup;

    public ConversationBean(){

    }

    public ConversationBean(String userId, String userName, boolean isGroup) {
        this.userId = userId;
        this.userName = userName;
        this.isGroup = isGroup;
    }
}
