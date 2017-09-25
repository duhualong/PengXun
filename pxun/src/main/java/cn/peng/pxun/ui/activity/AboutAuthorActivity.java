package cn.peng.pxun.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.utils.ToastUtil;

public class AboutAuthorActivity extends BaseActivity {
    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.chat_toolbar)
    Toolbar mChatToolbar;
    @BindView(R.id.iv_auther_wx)
    ImageView mIvAutherWx;
    @BindView(R.id.iv_auther_zfb)
    ImageView mIvAutherZfb;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_about_author;
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
        mTvChatTitle.setText("关于作者");
    }

    @Override
    protected void initListener() {
        mIvChatGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvAutherWx.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showToast(AboutAuthorActivity.this,"微信");
                return true;
            }
        });
        mIvAutherZfb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showToast(AboutAuthorActivity.this,"支付宝");
                return true;
            }
        });
    }
}
