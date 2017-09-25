package cn.peng.pxun.presenter.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.fragment.BaseFragment;

import static cn.peng.pxun.utils.UIUtils.getPackageName;

/**
 * Created by msi on 2017/1/3.
 */
public class MinePresenter extends BasePresenter{

    public MinePresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 获取当前应用的版本号
     */
    public String getVersionName() throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = MyApplication.context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String versionName = packInfo.versionName;
        return versionName;
    }
}
