package cn.peng.pxun.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.adapter.viewpager.HomeAdapter;

/**
 * Created by tofirst on 2017/10/27.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tab_title)
    TabLayout mTabTitle;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.vp_fragment_home)
    ViewPager mVpFragmentHome;

    private List<BaseFragment> fragmentList;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_home, null);
        initFragmentList();
        return view;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData() {
        mTabTitle.setVisibility(View.VISIBLE);
        mTvTitleText.setVisibility(View.GONE);
        HomeAdapter mAdapter = new HomeAdapter(getChildFragmentManager(), fragmentList);
        mVpFragmentHome.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabTitle.setupWithViewPager(mVpFragmentHome);//将TabLayout和ViewPager关联起来。
        mVpFragmentHome.setCurrentItem(2);
    }

    /**
     * 初始化Fragment
     */
    private void initFragmentList() {
        UserFragment userFragment = new UserFragment();
        FirstFragment firstFragment = new FirstFragment();
        MovieFragment movieFragment = new MovieFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(userFragment);
        fragmentList.add(firstFragment);
        fragmentList.add(movieFragment);
    }
}
