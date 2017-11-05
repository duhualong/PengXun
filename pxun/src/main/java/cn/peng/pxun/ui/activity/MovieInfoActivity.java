package cn.peng.pxun.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;

public class MovieInfoActivity extends BaseActivity {

    @BindView(R.id.iv_title_goback)
    ImageView mIvTitleGoback;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_movie_info;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mIvTitleGoback.setVisibility(View.VISIBLE);
        mTvTitleText.setText("电影详情");
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
