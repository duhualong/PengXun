package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.activity.DetailedPresenter;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 用户详情页面
 */
public class DetailedActivity extends BaseActivity<DetailedPresenter> {
    @BindView(R.id.iv_userinfo_bg)
    ImageView mIvUserinfoBg;
    @BindView(R.id.iv_userinfo_icon)
    ImageView mIvUserinfoIcon;
    @BindView(R.id.tv_userinfo_id)
    TextView mTvUserinfoId;
    @BindView(R.id.tv_userinfo_sex)
    TextView mTvUserinfoSex;
    @BindView(R.id.tv_userinfo_birthday)
    TextView mTvUserinfoBirthday;
    @BindView(R.id.tv_userinfo_address)
    TextView mTvUserinfoAddress;
    @BindView(R.id.bt_userinfo)
    Button mBtUserinfo;

    private boolean isFriend;
    private boolean isMe;
    private String accountNumber;
    private User mUser;

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        isMe = intent.getBooleanExtra("isMe",false);
        accountNumber = intent.getStringExtra("accountNumber");
        mUser = (User) intent.getSerializableExtra("user");
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_detailed;
    }

    @Override
    public DetailedPresenter initPresenter() {
        return new DetailedPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        showLoadingDialog("加载中");
    }

    @Override
    protected void initListener() {
        mIvUserinfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, BigPicActivity.class);
                if (!TextUtils.isEmpty(mUser.getHeadIcon())){
                    intent.putExtra("url", mUser.getHeadIcon());
                }else{
                    if ("tuling".equals(accountNumber)){
                        intent.putExtra("isTuling", true);
                    }else{
                        intent.putExtra("isTuling", false);
                    }
                }
                startActivity(intent);
            }
        });
        mBtUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMe){
                    Intent intent = new Intent(mActivity, UserInfoActivity.class);
                    startActivity(intent);
                }else{
                    if (isFriend){
                        Intent intent = new Intent(mActivity, ChatActivity.class);
                        intent.putExtra("isGroup", false);
                        intent.putExtra("userId", AppConfig.getUserId(mUser));
                        intent.putExtra("username", mUser.getUsername());
                        startActivity(intent);
                    }else {
                        presenter.showToast("添加好友");
                    }
                }
            }
        });
    }

    /**
     * 开始初始化
     */
    public void startInit() {
        isFriend = presenter.isMyFriend(AppConfig.getUserId(mUser));
        if (isMe){
            if (AppConfig.appUser != null){
                setUserInfo(AppConfig.appUser);
            } else {
                accountNumber = MyApplication.sp.getString("userId","");
                presenter.getUserInfo(accountNumber);
            }
            mBtUserinfo.setText("修改资料卡");
        }else{
            if (mUser == null){
                presenter.getUserInfo(accountNumber);
            }else {
                setUserInfo(mUser);
            }
            if (isFriend){
                mBtUserinfo.setText("发送消息");
            } else {
                mBtUserinfo.setText("添加好友");
            }
        }
    }

    /**
     * 设置用户信息到界面
     * @param user
     */
    public void setUserInfo(User user) {
        dismissLoadingDialog();
        if (user != null){
            if (isMe && AppConfig.appUser == null){
                AppConfig.appUser = user;
            }
            this.mUser = user;
            if (!TextUtils.isEmpty(user.getInfoBackGround())){
                Picasso.with(this).load(user.getInfoBackGround()).into(mIvUserinfoBg);
            }
            if (!TextUtils.isEmpty(user.getHeadIcon())){
                Picasso.with(this).load(user.getHeadIcon()).into(mIvUserinfoIcon);
            } else if ("tuling".equals(accountNumber)){
                mIvUserinfoIcon.setImageResource(R.drawable.icon_tuling);
            }
            mTvUserinfoId.setText(user.getUsername());
            mTvUserinfoSex.setText(user.getSex());
            mTvUserinfoBirthday.setText(user.getBirthday());
            mTvUserinfoAddress.setText(user.getAddress());
        }else{
            ToastUtil.showToast(this,"用户信息加载失败");
        }
    }

}
