package cn.peng.pxun.presenter.fragment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.fragment.FindFragment;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by tofirst on 2017/10/30.
 */

public class FindPresenter extends BasePresenter{
    private FindFragment mFragment;

    public FindPresenter(FindFragment fragment) {
        super(fragment);
        this.mFragment = fragment;
    }

    public void getUserList() {
        if (isNetUsable(mContext)){
            String mUserId = AppConfig.getUserId(AppConfig.appUser);
            BmobQuery<User> bmobQuery = new BmobQuery();
            if (isPhoneNumber(mUserId)){
                bmobQuery.addWhereNotEqualTo("mobilePhoneNumber", mUserId);
            }else{
                mUserId = mUserId.toUpperCase();
                bmobQuery.addWhereNotEqualTo("thirdPartyID", mUserId);
            }
            //分页加载
            //bmobQuery.setLimit(20);
            //bmobQuery.setSkip(20);
            bmobQuery.findObjects(new FindListener<User>(){
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        mFragment.setData(list);
                    } else {
                        LogUtil.e(e.toString());
                        mFragment.setEmptyPage();
                    }
                }
            });
        }
    }

}
