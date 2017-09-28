package cn.peng.pxun.modle.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 系统消息实体
 * Created by msi on 2017/9/27.
 */

public class SysMessage extends BmobObject {
    private String message;
    private String fromUser;
    private String toUser;
    private String msgType;
    private String msgState;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }
}
