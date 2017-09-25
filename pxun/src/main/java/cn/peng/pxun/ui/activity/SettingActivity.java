package cn.peng.pxun.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.chat_toolbar)
    Toolbar mChatToolbar;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvChatTitle.setText("设置");
    }

    @Override
    protected void initListener() {
        mIvChatGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
