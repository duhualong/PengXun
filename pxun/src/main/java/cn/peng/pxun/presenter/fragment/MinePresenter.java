package cn.peng.pxun.presenter.fragment;

import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.MineFragment;


/**
 * Created by msi on 2017/1/3.
 */
public class MinePresenter extends BaseUserPresenter {
    private MineFragment fragment;

    public MinePresenter(MineFragment fragment) {
        super(fragment);
        this.fragment = fragment;

        setListener();
    }

    private void setListener() {
        setUpLoadFileListener(new UpLoadFileListener() {
            @Override
            public void onUpLoadFinish(String path) {
                fragment.onBgUpLoadFinish(path);
            }

            @Override
            public void onUpLoadProgress(int value) {
                fragment.onBgUpLoadProgress(value);
            }
        });
        addUpdataUserListener(new UpdataUserListener() {
            @Override
            public void onResult(int result) {
                fragment.updataResult(result);
            }
        });
    }

}
