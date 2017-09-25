package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.activity.LoginPresenter;
import cn.peng.pxun.presenter.fragment.MinePresenter;
import cn.peng.pxun.ui.activity.AboutAuthorActivity;
import cn.peng.pxun.ui.activity.DetailedActivity;
import cn.peng.pxun.ui.activity.MainActivity;
import cn.peng.pxun.utils.ToastUtil;

/**
 * Created by msi on 2016/12/21.
 */
public class MineFragment extends BaseFragment<MinePresenter> {
    @BindView(R.id.rl_mine_info)
    RelativeLayout mRlMineInfo;
    @BindView(R.id.rl_mine_collect)
    RelativeLayout mRlMineCollect;
    @BindView(R.id.rl_mine_purse)
    RelativeLayout mRlMinePurse;
    @BindView(R.id.rl_about_author)
    RelativeLayout mRlAboutAuthor;
    @BindView(R.id.rl_version_info)
    RelativeLayout mRlVersionInfo;
    @BindView(R.id.tv_version_name)
    TextView mTvMineVersionname;
    private MainActivity mainActivity;

    @Override
    public void init() {
        super.init();
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected MinePresenter initPresenter() {
        return new MinePresenter(this);
    }

    @Override
    public void initListener() {
        mRlMineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity,DetailedActivity.class);
                intent.putExtra("isMe",true);
                intent.putExtra("username", LoginPresenter.getKeepUserPhone());
                startActivity(intent);

            }
        });
        mRlMineCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(activity,"即将开启,敬请期待!");
            }
        });
        mRlMinePurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(activity,"即将开启,敬请期待!");
            }
        });
        mRlAboutAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity,AboutAuthorActivity.class);
                startActivity(intent);
            }
        });
        mRlVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(activity,"已经是最新版本,无需升级!");
            }
        });
    }

    @Override
    public void initData() {
        try {
            String versionName = presenter.getVersionName();
            mTvMineVersionname.setText("V"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mTvMineVersionname.setText("版本号解析错误");
        }

    }
}