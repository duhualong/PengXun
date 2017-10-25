package cn.peng.pxun.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 意见反馈界面
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_goback)
    ImageView mIvGoback;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_feedback)
    EditText mEtFeedback;
    @BindView(R.id.bt_feedback_submit)
    Button mBtFeedbackSubmit;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_feedback;
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
        mTvTitle.setText("意见反馈");
    }

    @Override
    protected void initListener() {
        mIvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtFeedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String suggestion = mEtFeedback.getText().toString().trim();
                if (TextUtils.isEmpty(suggestion)){
                    ToastUtil.showToast(FeedbackActivity.this,"您输入了空的意见!");
                    return;
                }

                ThreadUtils.runOnSubThread(new Runnable() {
                    @Override
                    public void run() {
                        EMMessage message = EMMessage.createTxtSendMessage(suggestion, "18888888888");
                        EMClient.getInstance().chatManager().sendMessage(message);
                    }
                });
                ToastUtil.showToast(FeedbackActivity.this,"您的意见提交成功");
                mEtFeedback.setText("");
            }
        });
    }

}
