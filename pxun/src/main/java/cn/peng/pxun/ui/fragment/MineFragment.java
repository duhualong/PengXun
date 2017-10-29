package cn.peng.pxun.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.fragment.MinePresenter;
import cn.peng.pxun.ui.activity.AboutAuthorActivity;
import cn.peng.pxun.ui.activity.ContactActivity;
import cn.peng.pxun.ui.activity.SettingActivity;
import cn.peng.pxun.ui.activity.UserInfoActivity;
import cn.peng.pxun.ui.view.HandIconView;

/**
 * Created by msi on 2016/12/21.
 */
public class MineFragment extends BaseFragment<MinePresenter> {

    @BindView(R.id.iv_mine_icon)
    HandIconView mIvMineIcon;
    @BindView(R.id.tv_mine_name)
    TextView mTvMineName;
    @BindView(R.id.iv_mine_sex)
    ImageView mIvMineSex;
    @BindView(R.id.tv_mine_info)
    TextView mTvMineInfo;
    @BindView(R.id.tv_mine_friend)
    TextView mTvMineFriend;
    @BindView(R.id.tv_mine_group)
    TextView mTvMineGroup;
    @BindView(R.id.ll_mine_about)
    LinearLayout mLlMineAbout;
    @BindView(R.id.ll_mine_setting)
    LinearLayout mLlMineSetting;


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_mine, null);
        return view;
    }

    @Override
    protected MinePresenter initPresenter() {
        return new MinePresenter(this);
    }

    @SuppressLint("ResourceType")
    @Override
    public void initData() {
        if (AppConfig.appUser != null) {
            mTvMineName.setText(AppConfig.appUser.getUsername());

            if (!TextUtils.isEmpty(AppConfig.appUser.getSex())) {
                if ("男".equals(AppConfig.appUser.getSex())) {
                    mTvMineName.setTextColor(Color.parseColor("#33B5E5"));
                    mIvMineSex.setImageResource(R.drawable.sex_boy);
                    mIvMineIcon.setImageResource(R.drawable.icon_nan);
                } else if ("女".equals(AppConfig.appUser.getSex())){
                    mTvMineName.setTextColor(Color.parseColor("#FF4081"));
                    mIvMineSex.setImageResource(R.drawable.sex_girl);
                    mIvMineIcon.setImageResource(R.drawable.icon_nv);
                }
            } else {
                mIvMineSex.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(AppConfig.appUser.getHeadIcon())) {
                Picasso.with(mActivity).load(AppConfig.appUser.getHeadIcon()).into(mIvMineIcon);
            }
        }
    }

    @Override
    public void initListener() {
        mIvMineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        mTvMineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        mTvMineFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ContactActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);

            }
        });
        mTvMineGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ContactActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);

            }
        });
        mLlMineAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AboutAuthorActivity.class);
                startActivity(intent);
            }
        });
        mLlMineSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
