package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.activity.LoginPresenter;
import cn.peng.pxun.utils.MD5Util;
import cn.peng.pxun.utils.ToastUtil;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;

public class LoginActivity extends BaseActivity<LoginPresenter> {
    @BindView(R.id.et_login_phone)
    EditText mEtPhone;
    @BindView(R.id.et_login_password)
    EditText mEtPassword;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @BindView(R.id.tv_toregist)
    TextView mTvToRegist;
    @BindView(R.id.cb_login)
    CheckBox mCbLogin;
    @BindView(R.id.tv_nologin)
    TextView mTvNologin;
    @BindView(R.id.login_toolbar)
    Toolbar mLoginToolbar;
    @BindView(R.id.bt_login_qq)
    Button mBtLoginQq;
    @BindView(R.id.bt_login_weibo)
    Button mBtLoginWeibo;

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            ToastUtil.showToast(mActivity, platform.name());
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            loadingDialog.cancel();

            if(data != null && data.size() > 0){
                User user = new User();
                user.setThirdPartyID(data.get("uid"));
                user.setUsername(data.get("screen_name"));
                user.setPassword(MD5Util.encode(data.get("uid")));
                user.setHeadIcon(data.get("profile_image_url"));
                user.setSex(data.get("gender"));

                if (platform == SHARE_MEDIA.QQ){
                    user.setLoginType("QQ");
                    user.setAddress(data.get("province")+"省-"+data.get("city")+"市");
                }else if (platform == SHARE_MEDIA.SINA){
                    user.setLoginType("SINA");
                    user.setAddress(data.get("location")+"省");
                    user.setInfoBackGround(data.get("cover_image_phone"));
                }

                showLoadingDialog("正在登陆...");
                presenter.thirdPartyUserRegiest(user);
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            loadingDialog.cancel();
            ToastUtil.showToast(mActivity, "授权失败：" + t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            loadingDialog.cancel();
            ToastUtil.showToast(mActivity, "授权被取消");
        }
    };

    @Override
    protected void init() {
        super.init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        }
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();

        String phone = MyApplication.sp.getString("phone", "");
        mEtPhone.setText(phone);
        String password = MyApplication.sp.getString("password", "");
        mEtPassword.setText(password);
        boolean isRememberPassword = MyApplication.sp.getBoolean("isRemember", false);
        mCbLogin.setChecked(isRememberPassword);
    }

    @Override
    protected void initListener() {
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mEtPhone.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(LoginActivity.this, "帐号不能为空");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(LoginActivity.this, "密码不能为空");
                    return;
                }

                login(phone, password);
            }
        });
        mTvToRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivityForResult(intent, AppConfig.LOGINTOREGIEST);
            }
        });
        mTvNologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mBtLoginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在授权，请稍后...");
                MyApplication.umengApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
            }
        });
        mBtLoginWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在授权，请稍后...");
                MyApplication.umengApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.umengApi.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data != null){
                switch (requestCode){
                    case AppConfig.LOGINTOREGIEST:
                        String userInfo = data.getStringExtra("userInfo");
                        String[] info = userInfo.split(":");
                        mEtPhone.setText(info[0]);
                        mEtPassword.setText(info[1]);
                        mCbLogin.setChecked(true);
                        login(info[0], info[1]);
                        break;
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.cancel();
        }
    }

    /**
     * 显示登陆结果
     * @param code
     */
    public void onLoginFinish(int code,int huanXinCode) {
        switch (code) {
            case AppConfig.SUCCESS:
                loadingDialog.cancel();
                boolean isRemember = mCbLogin.isChecked();
                presenter.keepUserLoginInfo(isRemember);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case AppConfig.NET_ERROR:
                loadingDialog.setTitleText("登录失败")
                        .setContentText("网络异常,请先检查您的网络!")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            case AppConfig.SERVER_ERROR:
                loadingDialog.setTitleText("登录失败")
                        .setContentText("服务器异常,请稍后重试!")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            case AppConfig.ERROR:
                loadingDialog.setTitleText("登录失败");
                if(huanXinCode == 202){
                    loadingDialog.setContentText("您输入的密码有误");
                }else if(huanXinCode == 204){
                    loadingDialog.setContentText("该用户不存在");
                }else{
                    loadingDialog.setContentText("与服务器连接较慢，请稍后重试");
                }
                loadingDialog.setConfirmText("确定");
                loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
        }
    }

    private void login(String phone, String password) {
        showLoadingDialog("正在登陆...");
        presenter.login(phone, password);
    }

    /**
     * 检查权限,动态申请权限
     */
    private void initPermission() {
//        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
//        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取存储卡", R.drawable.permission_ic_storage ));
//        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入存储卡", R.drawable.permission_ic_storage ));
//        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone));

//        if(Build.VERSION.SDK_INT>=23){
//            String[] mPermissionList = new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);
//        }
        HiPermission.create(mActivity)
            .checkMutiPermission(new PermissionCallback() {
                @Override
                public void onClose() {
                ToastUtil.showToast(mActivity, "您取消了授权");
                }

                @Override
                public void onFinish() {
                //ToastUtil.showToast(mActivity, "授权成功");
                }

                @Override
                public void onDeny(String permission, int position) {
                }

                @Override
                public void onGuarantee(String permission, int position) {
            }
        });
    }
}
