package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.utils.ToastUtil;

public class AboutAuthorActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_goback)
    ImageView mIvGoback;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_auther_wx)
    ImageView mIvAutherWx;
    @BindView(R.id.iv_auther_zfb)
    ImageView mIvAutherZfb;
    @BindView(R.id.tv_author_blog)
    TextView mTvAutherBlog;

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvTitle.setText("关于作者");
    }

    @Override
    protected void initListener() {
        mIvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvAutherBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutAuthorActivity.this, WebActivity.class);
                intent.putExtra("url", mTvAutherBlog.getText().toString());
                startActivity(intent);
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
