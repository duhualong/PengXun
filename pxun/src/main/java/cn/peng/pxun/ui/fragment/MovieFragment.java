package cn.peng.pxun.ui.fragment;

import android.view.View;

import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;

/**
 * Created by tofirst on 2017/10/27.
 */

public class MovieFragment extends BaseFragment{
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_movie, null);
        return view;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
