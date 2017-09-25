package cn.peng.pxun.ui.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.peng.pxun.presenter.BasePresenter;

/**
 * Created by msi on 2016/12/21.
 */
public class FindFragment extends BaseFragment{
    @Override
    public View initView() {
        TextView tv =  new TextView(activity);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(24);
        tv.setTextColor(Color.BLACK);
        tv.setText("发现");
        return tv;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
