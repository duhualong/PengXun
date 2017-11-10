package cn.peng.pxun.presenter.fragment;

import cn.peng.pxun.presenter.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.MineFragment;


/**
 * Created by msi on 2017/1/3.
 */
public class MinePresenter extends BaseUserPresenter {
    private MineFragment mFragment;

    public MinePresenter(MineFragment fragment) {
        super(fragment);
        this.mFragment = fragment;

        setListener();
    }

    private void setListener() {
        setUpLoadFileListener(new UpLoadFileListener() {
            @Override
            public void onUpLoadFinish(String path) {
                mFragment.onBgUpLoadFinish(path);
            }

            @Override
            public void onUpLoadProgress(int value) {
                mFragment.onBgUpLoadProgress(value);
            }
        });
        addUpdataUserListener(new UpdataUserListener() {
            @Override
            public void onResult(int result) {
                mFragment.updataResult(result);
            }
        });
    }

}
