package cn.peng.pxun.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cn.peng.pxun.presenter.BasePresenter;

/**
 * Activity的基类
 * @author Peng
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P presenter;
    protected BaseActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setContentView(setLayoutRes());

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化一些设置,此方法会在setLayoutRes()之前执行
     */
    protected void init(){
        mActivity = this;
        this.presenter = initPresenter();
    }

    /**
     * 初始化视图
     */
    protected void initView(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化页面数据
     */
    protected void initData(){
    }

    /**
     * 初始化页面监听
     */
    protected  void initListener(){
    }

    /**
     * 设置布局文件资源
     * @return
     */
    public abstract int setLayoutRes();

    /**
     * 初始化和此Activity绑定的业务类
     * @return
     */
    public abstract P initPresenter();


}
