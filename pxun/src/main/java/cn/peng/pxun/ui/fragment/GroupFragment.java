package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.fragment.GroupPresenter;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.ui.adapter.GroupAdapter;
import cn.peng.pxun.ui.view.SuperListView;

/**
 * 群组页面
 * Created by msi on 2016/12/21.
 */
public class GroupFragment extends BaseFragment<GroupPresenter> {
    @BindView(R.id.lv_group)
    SuperListView mLvGroup;

    private List<EMGroup> groupList;
    private GroupAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_group, null);
        return view;
    }

    @Override
    protected GroupPresenter initPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    public void initData() {
        presenter.getGroupList();
        adapter = new GroupAdapter(groupList);
        mLvGroup.setAdapter(new GroupAdapter(groupList));
    }

    @Override
    public void initListener() {
        mLvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ChatActivity.class);
                intent.putExtra("isGroup", true);
                intent.putExtra("userId", groupList.get(position).getGroupId());
                intent.putExtra("username", groupList.get(position).getGroupName());
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

    public void bindView(List<EMGroup> grouplist) {
        groupList = grouplist;
        adapter.notifyDataSetChanged();
        if (mLvGroup.isRefresh()){
            mLvGroup.onRefreshFinish();
        }
    }

}
