package cn.peng.pxun.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.presenter.activity.SysMessagePresenter;
import cn.peng.pxun.ui.adapter.SysMessageAdapter;

public class SysMessageActivity extends BaseActivity<SysMessagePresenter> {

    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.chat_toolbar)
    Toolbar mChatToolbar;
    @BindView(R.id.rv_sys_message)
    RecyclerView mRvSysMessage;

    private List<SysMessage> mList;
    private SysMessageAdapter mAdapter;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_sys_message;
    }

    @Override
    public SysMessagePresenter initPresenter() {
        return new SysMessagePresenter(this);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvChatTitle.setText("系统消息");
        mRvSysMessage.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SysMessageAdapter(this);
        mRvSysMessage.setAdapter(mAdapter);
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
