package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.presenter.fragment.SquarePresenter;
import cn.peng.pxun.ui.activity.BigPicActivity;
import cn.peng.pxun.ui.adapter.recycleview.SquareAdapter;

/**
 * Created by tofirst on 2017/10/27.
 * 推荐,广场页面
 */

public class SquareFragment extends BaseFragment<SquarePresenter> {

    @BindView(R.id.app_recyclerview)
    RecyclerView mAppRecyclerview;
    @BindView(R.id.app_refresh_layout)
    SwipeRefreshLayout mAppRefreshLayout;

    private SquareAdapter adapter;
    private List<Posts> mPostsList;

    @Override
    protected void init() {
        super.init();
        mPostsList = new ArrayList<>();
    }

    @Override
    public View initLayout() {
        View view = View.inflate(mActivity, R.layout.fragment_square, null);
        return view;
    }

    @Override
    public SquarePresenter initPresenter() {
        return new SquarePresenter(this);
    }

    @Override
    protected void initData() {
        adapter = new SquareAdapter(R.layout.item_square, mPostsList);
        adapter.bindToRecyclerView(mAppRecyclerview);
        mAppRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        mAppRecyclerview.setAdapter(adapter);

        mAppRefreshLayout.setRefreshing(true);
        presenter.getPostsList();
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {

            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Posts posts = (Posts) adapter.getItem(position);
                Intent intent = new Intent();
                switch (view.getId()){
                    case R.id.tv_posts_username:
                    case R.id.iv_posts_usericon:
//                        intent.setClass(mActivity, DetailedActivity.class);
//                        startActivity(intent);
                        break;
                    case R.id.iv_posts_pic:
//                        int location[] = new int[2];
//                        view.getLocationOnScreen(location);

                        intent.setClass(mActivity, BigPicActivity.class);
//                        intent.putExtra("left", location[0]);
//                        intent.putExtra("top", location[1]);
//                        intent.putExtra("height", view.getHeight());
//                        intent.putExtra("width", view.getWidth());
                        intent.putExtra("url", posts.getPicPath());
                        startActivity(intent);
                        break;
                }
            }
        });
        mAppRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getPostsList();
            }
        });
    }

    public void setData(List<Posts> list) {
        if (mAppRefreshLayout!= null && mAppRefreshLayout.isRefreshing()){
            mPostsList.clear();
            mAppRefreshLayout.setRefreshing(false);
        }

        mPostsList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void setEmptyPage() {
        if (adapter != null){
            // 设置空页面
            adapter.setEmptyView(R.layout.app_page_empty);
        }
    }
}
