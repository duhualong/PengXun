package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.Constant;
import cn.peng.pxun.presenter.activity.RegistPresenter;
import cn.peng.pxun.utils.ToastUtil;

public class RegistActivity extends BaseActivity<RegistPresenter> {
    @BindView(R.id.et_regist_phone)
    EditText mEtRegistPhone;
    @BindView(R.id.et_regist_password)
    EditText mEtRegistPassword;
    @BindView(R.id.et_regist_again)
    EditText mEtRegistAgain;
    @BindView(R.id.bt_regist)
    Button mBtRegist;
    @BindView(R.id.iv_regist_goback)
    ImageView mIvRegistGoback;
    @BindView(R.id.regist_toolbar)
    Toolbar mRegistToolbar;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.et_regist_username)
    EditText mEtRegistUsername;
    @BindView(R.id.tv_regist_sex)
    TextView mTvRegistSex;
    @BindView(R.id.ll_regist_sex)
    LinearLayout mLlRegistSex;
    @BindView(R.id.tv_regist_birthday)
    TextView mTvRegistBirthday;
    @BindView(R.id.ll_regist_birthday)
    LinearLayout mLlRegistBirthday;
    @BindView(R.id.tv_regist_address)
    TextView mTvRegistAddress;
    @BindView(R.id.ll_regist_address)
    LinearLayout mLlRegistAddress;
    @BindView(R.id.tv_regist_service)
    TextView mTvRegistService;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_regist;
    }

    @Override
    public RegistPresenter initPresenter() {
        return new RegistPresenter(this);
    }

    @Override
    protected void initListener() {
        mIvRegistGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mBtRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mEtRegistPhone.getText().toString().trim();
                String username = mEtRegistUsername.getText().toString().trim();
                String password = mEtRegistPassword.getText().toString().trim();
                String pwdagain = mEtRegistAgain.getText().toString().trim();

                if (TextUtils.isEmpty(phone)){
                    ToastUtil.showToast(RegistActivity.this,"帐号不能为空");
                    return;
                }else if (TextUtils.isEmpty(username)){
                    ToastUtil.showToast(RegistActivity.this,"昵称不能为空");
                    return;
                }else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(pwdagain)){
                    ToastUtil.showToast(RegistActivity.this,"密码不能为空");
                    return;
                }else if (!password.equals(pwdagain)){
                    ToastUtil.showToast(RegistActivity.this, "您俩次输入的密码不一致");
                    return;
                }

                String sex = mTvRegistSex.getText().toString();
                String birthday = mTvRegistBirthday.getText().toString();
                String address = mTvRegistAddress.getText().toString();

                presenter.regist(phone, username, password ,sex ,birthday ,address);
            }
        });
        mLlRegistSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(RegistActivity.this,"性别");
            }
        });
        mLlRegistBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(RegistActivity.this,"生日");
            }
        });
        mLlRegistAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(RegistActivity.this,"居住地");
            }
        });
        mTvRegistService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(RegistActivity.this,"注册协议");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    public void onRegist(int code, BmobUser user) {
        switch (code) {
            case Constant.NET_ERROR:
                ToastUtil.showToast(RegistActivity.this, "网络异常,请先检查您的网络!");
                break;
            case Constant.REGIST_SUCCESS:
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                ToastUtil.showToast(RegistActivity.this, "注册成功");
                break;
            case Constant.REGIST_ERROR:
                ToastUtil.showToast(RegistActivity.this, "注册失败");
                break;
            case Constant.NUMBER_ERROR:
                ToastUtil.showToast(RegistActivity.this, "您输入的帐号格式有误");
                break;
        }
    }

}
