package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.utils.ToastUtil;

public class DetailedActivity extends BaseActivity {
    @BindView(R.id.iv_userinfo_icon)
    ImageView mIvUserinfoIcon;
    @BindView(R.id.tv_userinfo_id)
    TextView mTvUserinfoId;
    @BindView(R.id.tv_userinfo_sex)
    TextView mTvUserinfoSex;
    @BindView(R.id.tv_userinfo_birthday)
    TextView mTvUserinfoBirthday;
    @BindView(R.id.tv_userinfo_address)
    TextView mTvUserinfoAddress;
    @BindView(R.id.bt_userinfo)
    Button mBtUserinfo;
    private boolean isMe;
    private String username;

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        isMe = intent.getBooleanExtra("isMe",false);
        username = intent.getStringExtra("username");
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_detailed;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvUserinfoId.setText(username);
        mTvUserinfoSex.setText("男");
        mTvUserinfoBirthday.setText("1999年9月9日");
        mTvUserinfoAddress.setText("山西省忻州市原平市");
        if (isMe){
            mBtUserinfo.setText("修改资料卡");
        }else{
            mBtUserinfo.setText("发送消息");
        }
    }

    @Override
    protected void initListener() {
        mIvUserinfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMe){

                }else{
                    Intent intent = new Intent(DetailedActivity.this, BigPicActivity.class);
                    intent.putExtra("url", "111");
                    startActivity(intent);
                }
            }
        });
        mBtUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMe){
                    ToastUtil.showToast(DetailedActivity.this,"修改资料");
                }else{
                    finish();
                }
            }
        });
    }
}
