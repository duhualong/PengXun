package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.picker.City;
import cn.peng.pxun.modle.picker.County;
import cn.peng.pxun.modle.picker.Province;
import cn.peng.pxun.presenter.activity.RegistPresenter;
import cn.peng.pxun.ui.view.picker.AddressPickTask;
import cn.peng.pxun.ui.view.picker.DatePicker;
import cn.peng.pxun.ui.view.picker.OptionPicker;
import cn.peng.pxun.utils.ConvertUtils;
import cn.peng.pxun.utils.MD5Util;

import static cn.peng.pxun.utils.ToastUtil.showToast;

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

    private OptionPicker sexPicker;
    private DatePicker datePicker;
    private AddressPickTask addressPicker;

    @Override
    protected void init() {
        super.init();
        initSexPicker();
        initBirthdayPicker();
        initAddressPicker();
    }

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
                    showToast(RegistActivity.this,"帐号不能为空");
                    return;
                }else if (TextUtils.isEmpty(username)){
                    showToast(RegistActivity.this,"昵称不能为空");
                    return;
                }else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(pwdagain)){
                    showToast(RegistActivity.this,"密码不能为空");
                    return;
                }else if (!password.equals(pwdagain)){
                    showToast(RegistActivity.this, "您俩次输入的密码不一致");
                    return;
                }

                String sex = mTvRegistSex.getText().toString();
                String birthday = mTvRegistBirthday.getText().toString();
                String address = mTvRegistAddress.getText().toString();

                presenter.regist(phone, username, MD5Util.encode(password) ,sex ,birthday ,address);
            }
        });
        mLlRegistSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPicker.show();
            }
        });
        mLlRegistBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
        mLlRegistAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressPicker.show();
            }
        });
        mTvRegistService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(RegistActivity.this,"注册协议");
            }
        });
    }

    public void onRegist(int code, String userInfo) {
        switch (code) {
            case AppConfig.NET_ERROR:
                showToast(RegistActivity.this, "网络异常,请先检查您的网络!");
                break;
            case AppConfig.SUCCESS:
                Intent intent = new Intent();
                intent.putExtra("userInfo", userInfo);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case AppConfig.ERROR:
                showToast(RegistActivity.this, "用户注册失败，请稍后重试");
                break;
            case AppConfig.SERVER_ERROR:
                showToast(RegistActivity.this, "当前服务器连接较慢，请稍后重试");
                break;
            case AppConfig.NUMBER_ERROR:
                showToast(RegistActivity.this, "您输入的帐号格式有误");
                break;
        }
    }

    /**
     * 初始化性别选择器
     */
    private void initSexPicker() {
        sexPicker = new OptionPicker(this, AppConfig.USER_SEX);
        sexPicker.setCycleDisable(true);
        sexPicker.setTitleText( "请选择" );
        sexPicker.setTitleTextSize(14);
        sexPicker.setCancelTextSize(12);
        sexPicker.setSubmitTextSize(12);
        sexPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mTvRegistSex.setText(item);
            }
        });
    }

    /**
     * 初始化日期选择器
     */
    private void initBirthdayPicker() {
        datePicker = new DatePicker(this);
        datePicker.setTopPadding(ConvertUtils.toPx(this, 12));
        Calendar now = Calendar.getInstance();
        datePicker.setRangeStart(1970, 1, 1);
        datePicker.setRangeEnd(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1, now.get(Calendar.DAY_OF_MONTH));
        datePicker.setSelectedItem(1990, 8, 1);
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String birthday = year + "年" + month + "月" + day + "日";
                mTvRegistBirthday.setText(birthday);
            }
        });
        datePicker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                datePicker.setTitleText(year + "-" + datePicker.getSelectedMonth() + "-" + datePicker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                datePicker.setTitleText(datePicker.getSelectedYear() + "-" + month + "-" + datePicker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                datePicker.setTitleText(datePicker.getSelectedYear() + "-" + datePicker.getSelectedMonth() + "-" + day);
            }
        });
    }

    /**
     * 初始化地址选择器
     */
    private void initAddressPicker() {
        addressPicker = new AddressPickTask(this);
        addressPicker.setHideProvince(false);
        addressPicker.setHideCounty(false);
        addressPicker.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                showToast(mActivity,"数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String address;
                if (county == null) {
                    if(province.getAreaName().equals(city.getAreaName())){
                        address = city.getAreaName();
                    }else{
                        address = province.getAreaName() + city.getAreaName();
                    }
                } else {
                    if(province.getAreaName().equals(city.getAreaName())){
                        address = province.getAreaName() + city.getAreaName();
                    }else{
                        address = province.getAreaName() + city.getAreaName() + county.getAreaName();
                    }
                }
                mTvRegistAddress.setText(address);
            }
        });
        addressPicker.execute("河北省", "石家庄市", "长安区");
    }
}
