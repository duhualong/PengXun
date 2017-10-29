package cn.peng.pxun.presenter.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.DetailedActivity;

/**
 * Created by msi on 2017/10/20.
 */

public class DetailedPresenter extends BasePresenter{
    private DetailedActivity activity;

    public DetailedPresenter(DetailedActivity activity) {
        super(activity);
        this.activity = activity;
    }

    /**
     * 获取用户信息
     * @param accountNumber
     */
    public void getUserInfo(String accountNumber){
        if ("tuling".equals(accountNumber)){
            User user = new User();
            user.setUsername("图灵小白");
            user.setSex("女");
            user.setAddress("北京市朝阳区");
            user.setBirthday("2016年11月11日");
            activity.setUserInfo(user);
        }else{
            accountNumber = accountNumber.toUpperCase();
            BmobQuery<User> bmobQuery = new BmobQuery();
            List<BmobQuery<User>> params = new ArrayList<>();
            params.add(new BmobQuery<User>().addWhereEqualTo("thirdPartyID", accountNumber));
            params.add(new BmobQuery<User>().addWhereEqualTo("mobilePhoneNumber", accountNumber));
            bmobQuery.or(params);
            bmobQuery.setLimit(50);
            bmobQuery.findObjects(new FindListener<User>(){

                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        activity.setUserInfo(list.get(0));
                    } else {
                        activity.setUserInfo(null);
                    }
                }
            });
        }
    }
}
