package cn.peng.pxun.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.peng.pxun.presenter.BasePresenter;

/**
 * Fragment的基类
 * @author Peng
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    /** Fragment所依赖的Activity */
    protected Activity activity;
    /** 此Fragment所对应的业务操作类 */
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        return initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initListener();
        initData();
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 执行初始化操作,
     * @des 子类可选择复写,用来进行初始化操作
     */
    public void init(){
        this.presenter = initPresenter();
    }

    /**
     * 初始化界面数据,
     * @des 子类可选择复写,用来初始化界面数据
     */
    public void initData() {
    }

    /**
     * 初始化界面内的监听,
     * @des 子类可选择复写,初始化界面内的监听
     */
    public void initListener() {
    }

    /**
     * 初始化Fragment界面视图
     * @des 子类必须复写
     * @return
     */
    public abstract View initView();

    /**
     * 初始化和此Fragment绑定的业务类
     * @return
     */
    protected abstract P initPresenter();
}
