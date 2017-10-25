package cn.peng.pxun.presenter.activity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.SearchActivity;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;

/**
 * Created by msi on 2017/9/23.
 */
public class SearchPresenter extends BasePresenter {

    private SearchActivity activity;

    public SearchPresenter(SearchActivity activity) {
        super(activity);
        this.activity = activity;
    }

    /**
     * 开始搜索
     * @param searchType
     * @param content
     */
    public void search(int searchType, String content) {
        switch (searchType){
            case SearchActivity.SEARCH_USER:
                searchUser(content);
                break;
            case SearchActivity.SEARCH_GROUP:
                searchGroup(content);
                break;
        }
    }

    /**
     * 搜索用户
     * @param content
     */
    private void searchUser(String content) {
        BmobQuery<User> bmobQuery = new BmobQuery();
        List<BmobQuery<User>> params = new ArrayList<>();
        params.add(new BmobQuery<User>().addWhereEqualTo("username", content));
        params.add(new BmobQuery<User>().addWhereEqualTo("mobilePhoneNumber", content));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    activity.onUserSearch(list);
                } else {
                    ToastUtil.showToast(context,"查询失败：" + e.toString());
                    activity.onUserSearch(list);
                }
            }
        });
    }

    /**
     * 搜索群组
     * @param content
     */
    private void searchGroup(String content) {
        BmobQuery<Group> bmobQuery = new BmobQuery();
        List<BmobQuery<Group>> params = new ArrayList<>();
        params.add(new BmobQuery<Group>().addWhereEqualTo("groupName", content));
        params.add(new BmobQuery<Group>().addWhereEqualTo("groupNum", content));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Group>(){

            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null) {
                    activity.onGroupSearch(list);
                } else {
                    ToastUtil.showToast(context,"查询失败：" + e.toString());
                    activity.onGroupSearch(list);
                }
            }
        });
    }

    /**
     * 发送添加联系人请求
     * @param user
     */
    public void addContact(final User user) {
        final String appUserID = AppConfig.getUserId(AppConfig.appUser);
        final String userID = AppConfig.getUserId(user);
        if (userID.equals(appUserID)){
            ToastUtil.showToast(activity, "无法添加自己为好友");
            return;
        }

        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = AppConfig.appUser.getUsername()+"请求添加您为好友,请同意!";
                    // 发送环信好友申请
                    EMClient.getInstance().contactManager().addContact(userID, message);

                    SysMessage sysMsg = new SysMessage();
                    sysMsg.setMessage(message);
                    sysMsg.setFromUser(appUserID);
                    sysMsg.setToUser(userID);
                    sysMsg.setMsgType("100");
                    sysMsg.setMsgState("0");
                    sysMsg.save(new SaveListener<String>(){

                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                showToast(activity, "好友申请发送成功");
                            }else{
                                showToast(activity, "服务器连接较慢，请稍后重试");
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    showToast(activity, "消息发送失败");
                }
            }
        });
    }

}
