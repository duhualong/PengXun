package cn.peng.pxun.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by tofirst on 2017/11/3.
 */

public class BaseUserPresenter extends BasePresenter{
    private UserInfoListener userInfoListener;

    public BaseUserPresenter(BaseActivity activity) {
        super(activity);
    }

    public BaseUserPresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 根据用户ID获取用户
     * @param userId
     */
    public void getUser(String userId){
        if (isNetUsable(context)){
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
    }

    /**
     * 从环信获取我的好友集合
     * @return
     */
    public List<String> getFriendListFromHuanXin(){
        if (isNetUsable(context)) {
            try {
                return EMClient.getInstance().contactManager().getAllContactsFromServer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断该用户是否为好友
     * @param userId
     * @return
     */
    public boolean isMyFriend(String userId){
        if (isNetUsable(context)) {
            List<String> friendIds = getFriendListFromHuanXin();
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

    interface UserInfoListener{
        void onGetUser(User user);
    }
}
