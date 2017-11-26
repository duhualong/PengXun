package cn.peng.pxun.presenter.activity;

import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.activity.UserInfoActivity;

/**
 * Created by tofirst on 2017/11/6.
 */

public class UserInfoPresenter extends BaseUserPresenter {
    private UserInfoActivity activity;

    public UserInfoPresenter(UserInfoActivity activity) {
        super(activity);
        this.activity = activity;

        setListener();
    }

    private void setListener() {
        setUpLoadFileListener(new UpLoadFileListener() {
            @Override
            public void onUpLoadFinish(String path) {
                activity.onIconUploadFinish(path);
            }

            @Override
            public void onUpLoadProgress(int value) {
                activity.onIconUploadProgress(value);
            }
        });
        addUpdataUserListener(new UpdataUserListener() {
            @Override
            public void onResult(int result) {
                activity.updataResult(result);
            }
        });
    }
}
