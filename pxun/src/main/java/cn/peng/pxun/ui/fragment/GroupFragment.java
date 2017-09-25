package cn.peng.pxun.ui.fragment;

import android.view.View;

import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.fragment.GroupPresenter;
import cn.peng.pxun.ui.adapter.GroupAdapter;
import cn.peng.pxun.ui.view.SuperListView;

/**
 * 群组页面
 * Created by msi on 2016/12/21.
 */
public class GroupFragment extends BaseFragment<GroupPresenter> {
    @BindView(R.id.lv_group)
    SuperListView mLvGroup;

    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.fragment_group, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected GroupPresenter initPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    public void initData() {
        presenter.getGroupList();
    }

    @Override
    public void initListener() {
        mLvGroup.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                presenter.getGroupList();
            }
        });
    }

    public void bindView(List<EMGroup> grouplist) {
        mLvGroup.setAdapter(new GroupAdapter(grouplist));
        mLvGroup.onRefreshFinish();
    }


}
