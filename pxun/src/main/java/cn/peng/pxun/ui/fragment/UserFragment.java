package cn.peng.pxun.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.fragment.UserPresenter;

/**
 * Created by tofirst on 2017/10/27.
 */

public class UserFragment extends BaseFragment<UserPresenter> {

    @BindView(R.id.app_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.app_recyclerview)
    RecyclerView mRecycleview;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_user, null);
        return view;
    }

    @Override
    protected UserPresenter initPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public void initData() {

    }

}
