package cn.peng.pxun.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
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
import cn.bmob.v3.BmobUser;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.Constant;
import cn.peng.pxun.presenter.activity.LoginPresenter;
import cn.peng.pxun.utils.ToastUtil;

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
    @BindView(R.id.bt_login_weixin)
    Button mBtLoginWeixin;
    @BindView(R.id.bt_login_weibo)
    Button mBtLoginWeibo;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void init() {
        super.init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
    }

    @Override
    protected void initView() {
        super.initView();

        Intent intent = getIntent();
        BmobUser user = (BmobUser) intent.getSerializableExtra("user");
        if (user != null) {
            mEtPhone.setText(user.getMobilePhoneNumber());
            mCbLogin.setChecked(false);
        } else {
            String phone = MyApplication.sp.getString("phone", "");
            mEtPhone.setText(phone);
            String password = MyApplication.sp.getString("password", "");
            mEtPassword.setText(password);
            boolean isRememberPassword = MyApplication.sp.getBoolean("isRemember", false);
            mCbLogin.setChecked(isRememberPassword);
        }
    }

    @Override
    protected void initListener() {
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mEtPhone.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(phone)){
                    ToastUtil.showToast(LoginActivity.this,"帐号不能为空");
                    return;
                }else if(TextUtils.isEmpty(password)){
                    ToastUtil.showToast(LoginActivity.this,"密码不能为空");
                    return;
                }

                presenter.login(phone, password);
            }
        });
        mTvToRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                finish();
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
                MyApplication.umengApi.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
            }
        });
        mBtLoginWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.umengApi.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
            }
        });
        mBtLoginWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.umengApi.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
            }
        });
    }

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
            ToastUtil.showToast(mActivity, "成功了");
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtil.showToast(mActivity, "失败：" + t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtil.showToast(mActivity, "取消了");
        }
    };

    public void onLogin(int code) {
        switch (code) {
            case Constant.LOGIN_SUCCESS:
                boolean isRemember = mCbLogin.isChecked();
                presenter.keepUserLoginInfo(isRemember);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case Constant.LOGIN_ERROR:
                ToastUtil.showToast(LoginActivity.this, "登录失败");
                break;
            case Constant.NET_ERROR:
                ToastUtil.showToast(LoginActivity.this, "网络异常,请先检查您的网络!");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 10000){
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                //被授权了
            } else {
                //没有给予该应用权限，不让你用了
            }
        }
    }

    /**
     * 检查权限,动态申请权限
     */
    private void checkPermission() {
        String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this,mPermissionList,10000);
    }
}
