package cn.peng.pxun.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;

public class ContactActivity extends BaseActivity {


    @BindView(R.id.iv_title_goback)
    ImageView mIvTitleGoback;
    @BindView(R.id.tab_title)
    TabLayout mTabTitle;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.vp_contact)
    ViewPager mVpContact;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_contact;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mIvTitleGoback.setVisibility(View.VISIBLE);
        mTabTitle.setVisibility(View.VISIBLE);
        mTvTitleText.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mIvTitleGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
