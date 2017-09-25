package cn.peng.pxun.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.MainActivity;
import cn.peng.pxun.ui.adapter.MainAdapter;

/**
 * Created by msi on 2016/12/21.
 */
public class MainFragment extends BaseFragment {
    @BindView(R.id.tab_fragment_main)
    TabLayout mTabFragmentMain;
    @BindView(R.id.vp_fragment_main)
    ViewPager mVpFragmentMain;

    private List<BaseFragment> fragmentList;
    private MainActivity mMainActivity;


    @Override
    public View initView() {
        mMainActivity = (MainActivity) getActivity();
        View view = View.inflate(activity, R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        initFragmentList();
        return view;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData() {
        MainAdapter mAdapter = new MainAdapter(getChildFragmentManager(),fragmentList);
        mVpFragmentMain.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabFragmentMain.setupWithViewPager(mVpFragmentMain);//将TabLayout和ViewPager关联起来。
        mTabFragmentMain.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

    @Override
    public void initListener() {
        mVpFragmentMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mMainActivity.mTvToolbarTitle.setText("消息");
                        break;
                    case 1:
                        mMainActivity.mTvToolbarTitle.setText("好友");
                        break;
                    case 2:
                        mMainActivity.mTvToolbarTitle.setText("群组");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * 初始化Fragment
     */
    private void initFragmentList() {
        MessageFragment mf = new MessageFragment();
        ContactFragment cf = new ContactFragment();
        GroupFragment gf = new GroupFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(mf);
        fragmentList.add(cf);
        fragmentList.add(gf);
    }
}
