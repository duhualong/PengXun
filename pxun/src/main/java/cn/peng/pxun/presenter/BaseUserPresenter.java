package cn.peng.pxun.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.LogUtil;
import cn.peng.pxun.utils.ThreadUtil;

/**
 * Created by tofirst on 2017/11/3.
 */

public class BaseUserPresenter extends BasePhotoPresenter{
    private UserInfoListener userInfoListener;
    private UpdataUserListener updataUserListener;
    private FriendListListener friendListListener;

    protected List<String> friendIds;

    public BaseUserPresenter(BaseActivity activity) {
        super(activity);
    }

    public BaseUserPresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 更新用户信息
     * @param user
     */
    public void upDataUser(User user){
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
               if (updataUserListener != null){
                   if (e == null){
                       updataUserListener.onResult(AppConfig.SUCCESS);
                   }else {
                       updataUserListener.onResult(AppConfig.SERVER_ERROR);
                   }
               }
            }
        });
    }

    /**
     * 根据用户ID获取用户
     * @param userId
     */
    public void getUser(String userId){
        if (!isNetUsable(context)) {
            return;
        }
        BmobQuery<User> bmobQuery = new BmobQuery();
        if (isPhoneNumber(userId)){
            bmobQuery.addWhereEqualTo("mobilePhoneNumber", userId);
        }else{
            userId = userId.toUpperCase();
            bmobQuery.addWhereEqualTo("thirdPartyID", userId);
        }
        bmobQuery.findObjects(new FindListener<User>(){
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && userInfoListener != null) {
                    User user = list.get(0);
                    userInfoListener.onGetUser(user);
                } else {
                    LogUtil.e(e.toString());
                }
            }
        });
    }

    /**
     * 从环信获取我的好友集合
     * @return
     */
    public void getFriendListFromHuanXin(){
        if (!isNetUsable(context)) {
            return;
        }
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    friendIds = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    ThreadUtil.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (friendListListener != null){
                                friendListListener.onGetFriendList(friendIds);
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断该用户是否为好友
     * @param userId
     * @return
     */
    public boolean isMyFriend(String userId){
        if (friendIds != null && friendIds.size() > 0){
            for (String friendId : friendIds){
                if (userId.equals(friendId)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 添加获取用户的监听
     * @param listener
     */
    public void addUserInfoListener(UserInfoListener listener){
        this.userInfoListener = listener;
    }

    public interface UserInfoListener{
        void onGetUser(User user);
    }

    /**
     * 添加获取用户的监听
     * @param listener
     */
    public void addUpdataUserListener(UpdataUserListener listener){
        this.updataUserListener = listener;
    }

    public interface UpdataUserListener{
        void onResult(int result);
    }

    /**
     * 添加获取好友列表的监听
     * @param listener
     */
    public void addFriendListListener(FriendListListener listener){
        this.friendListListener = listener;
    }

    public interface FriendListListener{
        void onGetFriendList(List<String> userIds);
    }
}
