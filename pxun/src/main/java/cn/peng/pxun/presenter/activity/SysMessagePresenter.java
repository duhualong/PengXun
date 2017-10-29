package cn.peng.pxun.presenter.activity;


import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.SysMessageActivity;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * Created by tofirst on 2017/9/28.
 */

public class SysMessagePresenter extends BasePresenter{
    private SysMessageActivity activity;

    public SysMessagePresenter(SysMessageActivity activity) {
        super(activity);
        this.activity = activity;
    }

    /**
     * 获取系统消息列表
     */
    public void getSysMessageList(){
        BmobQuery<SysMessage> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("toUser", AppConfig.getUserId(AppConfig.appUser));
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<SysMessage>(){

            @Override
            public void done(List<SysMessage> list, BmobException e) {
                if (e == null) {
                    activity.onloadFinish(list);
                } else {
                    showToast(activity, "数据加载失败");
                }
            }
        });
    }

    /**
     * 同意好友申请
     * @param sysMsg
     */
    public void agreeAddContact(final SysMessage sysMsg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(sysMsg.getFromUser());
                    sysMsg.setMsgState("1");
                    sysMsg.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                SysMessage newMsg = new SysMessage();
                                newMsg.setMessage(AppConfig.appUser.getUsername()+"同意了你的好友申请。");
                                newMsg.setFromUser(sysMsg.getToUser());
                                newMsg.setToUser(sysMsg.getFromUser());
                                newMsg.setMsgType("110");
                                newMsg.setMsgState("1");
                                addNewSysMessage(newMsg);
                            }else{
                                showToast(activity, "服务器连接较慢，请稍后重试");
                            }
                        }
                    });
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                    showToast(activity, "消息处理失败");
                }
            }
        });
    }

    private void addNewSysMessage(final SysMessage sysMsg) {
        sysMsg.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    showToast(activity, "消息处理成功");
                }else{
                    showToast(activity, "服务器连接较慢，请稍后重试");
                }
            }
        });
    }
}
