package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.fragment.GroupPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.GroupAdapter;
import cn.peng.pxun.ui.view.SuperListView;
import cn.peng.pxun.utils.ThreadUtils;

/**
 * 群组页面
 * Created by msi on 2016/12/21.
 */
public class GroupFragment extends BaseFragment<GroupPresenter> {
    @BindView(R.id.lv_group)
    SuperListView mLvGroup;

    private List<EMGroup> groupList;
    private GroupAdapter mAdapter;

    @Override
    public void init() {
        super.init();
        //EventBus.getDefault().register(this);
        groupList = new ArrayList<>();
        presenter.getGroupList();
    }

    @Override
    protected GroupPresenter initPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_group, null);
        return view;
    }

    @Override
    public void initData() {
        mAdapter = new GroupAdapter(groupList);
        mLvGroup.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mLvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMGroup group = groupList.get(position-1);
                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("isGroup", true);
                intent.putExtra("userId", group.getGroupId());
                intent.putExtra("username", group.getGroupName());
                startActivity(intent);
            }
        });
        mLvGroup.setOnLoadDataListener(new SuperListView.OnLoadDataListener() {
            @Override
            public void onRefresh() {
                presenter.getGroupList();
            }
        });
    }


    public void refreshGroup(final List<EMGroup> grouplist) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mLvGroup != null &&mAdapter != null ) {
                    groupList = grouplist;
                    mAdapter.setDataSets(groupList);
                    if (mLvGroup.isRefresh()) {
                        mLvGroup.onRefreshFinish();
                    }
                }
            }
        });
    }

}
