package cn.peng.pxun.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.presenter.activity.PublishPostsPresenter;
import cn.peng.pxun.utils.ToastUtil;

public class PublishPostsActivity extends BaseActivity<PublishPostsPresenter> {

    @BindView(R.id.iv_posts_goback)
    ImageView mIvPostsGoback;
    @BindView(R.id.tv_posts_publish)
    TextView mTvPostsPublish;
    @BindView(R.id.et_posts_input)
    EditText mEtPostsInput;

    private Posts mPosts;

    @Override
    protected void init() {
        super.init();
        initPosts();
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_publish_posts;
    }

    @Override
    public PublishPostsPresenter initPresenter() {
        return new PublishPostsPresenter(this);
    }

    @Override
    protected void initListener() {
        mIvPostsGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvPostsPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postsContent = mEtPostsInput.getText().toString();
                if (TextUtils.isEmpty(postsContent)){
                    ToastUtil.showToast(mActivity, "先写点什么吧");
                    return;
                }

                showLoadingDialog("请稍候...");
                mPosts.setContent(postsContent);
                presenter.publishPosts(mPosts);
            }
        });
    }

    private void initPosts() {
        mPosts = new Posts();
        mPosts.setPublishUserId(AppConfig.getUserId(AppConfig.appUser));
        mPosts.setPublishUserName(AppConfig.appUser.getUsername());
        mPosts.setPublishUserIcon(AppConfig.appUser.getHeadIcon());
        mPosts.setPostsType(Posts.POSTS_TYPE_JOKE);
        mPosts.setContentType(Posts.CONTENT_TYPE_TEXT);
    }

    public void publishSuccess() {
        dismissLoadingDialog();
        ToastUtil.showToast(this, "发表成功");
        finish();
    }
}
