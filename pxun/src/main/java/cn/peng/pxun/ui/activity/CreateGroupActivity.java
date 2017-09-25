package cn.peng.pxun.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hyphenate.chat.EMGroupManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.Constant;
import cn.peng.pxun.presenter.activity.CreateGroupPresenter;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 创建群组界面
 */
public class CreateGroupActivity extends BaseActivity<CreateGroupPresenter> {

    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.chat_toolbar)
    Toolbar mChatToolbar;
    @BindView(R.id.iv_group_icon)
    ImageView mIvGroupIcon;
    @BindView(R.id.et_group_name)
    EditText mEtGroupName;
    @BindView(R.id.bt_group_create)
    Button mBtGroupCreate;
    @BindView(R.id.sp_group_type)
    Spinner mSpGroupType;

    private EMGroupManager.EMGroupStyle groupType;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_create_group;
    }

    @Override
    public CreateGroupPresenter initPresenter() {
        return new CreateGroupPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvChatTitle.setText("创建聊天群");

        List<String> spData = new ArrayList<>();
        spData.add("公开群");
        spData.add("私有群");
        ArrayAdapter adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spData);
        mSpGroupType.setAdapter(adapter);
        mSpGroupType.setSelection(0);
        groupType = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
    }

    @Override
    protected void initListener() {
        mBtGroupCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = mEtGroupName.getText().toString().trim();
                if (TextUtils.isEmpty(groupName)) {
                    ToastUtil.showToast(mActivity, "群名称不能为空");
                    return;
                }
                presenter.createGroup(groupName, "",groupType, null);
            }
        });
        mSpGroupType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    groupType = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                }else if (position == 1){
                    groupType = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mIvChatGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onCreateGroup(int code, String groupInfo) {
        switch (code) {
            case Constant.NET_ERROR:
                ToastUtil.showToast(mActivity, "网络异常,请先检查您的网络!");
                break;
            case Constant.GROUP_SUCCESS:
                finish();
                ToastUtil.showToast(mActivity, "创建成功,群组ID为:" + groupInfo);
                break;
            case Constant.GROUP_ERROR:
                ToastUtil.showToast(mActivity, "群组创建失败,错误代码:" + groupInfo);
                break;

        }
    }

}
