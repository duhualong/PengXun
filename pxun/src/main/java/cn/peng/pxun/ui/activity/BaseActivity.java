package cn.peng.pxun.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.peng.pxun.presenter.BasePresenter;
import de.greenrobot.event.EventBus;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Activity的基类
 * @author Peng
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P presenter;
    // Activity中的上下文
    protected BaseActivity mActivity;
    // 加载中的dialog
    protected SweetAlertDialog loadingDialog;

    // 管理运行的所有的activity
    public final static List<AppCompatActivity> mActivities = new LinkedList();
    // ButterKnife的解绑器
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setContentView(setLayoutRes());

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑ButterKnife
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        synchronized (mActivities) {
            mActivities.remove(this);
        }
    }

    /**
     * 初始化一些设置,此方法会在setLayoutRes()之前执行
     */
    protected void init(){
        synchronized (mActivities) {
            mActivities.add(this);
        }
        this.presenter = initPresenter();
    }

    /**
     * 初始化视图
     */
    protected void initView(){
        //绑定ButterKnife
        mUnbinder = ButterKnife.bind(this);
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

    /**
     * 显示加载中提示框
     */
    protected void showLoadingDialog(String title) {
        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setCancelable(false);
        loadingDialog.setTitleText(title);
        loadingDialog.show();
    }

    /**
     * 请求多个权限
     * @param permissionItems
     */
    protected void requestPermission(List<PermissionItem> permissionItems, PermissionCallback callback){
        HiPermission.create(this)
                .permissions(permissionItems)
                .checkMutiPermission(callback);
    }
}
