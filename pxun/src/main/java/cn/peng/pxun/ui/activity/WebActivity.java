package cn.peng.pxun.ui.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;

public class WebActivity extends BaseActivity {
    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.webview)
    WebView mWebview;

    private String url;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_web;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void initView() {
        super.initView();

        mTvChatTitle.setText("PengBlog");
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebview.loadUrl(url);
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
