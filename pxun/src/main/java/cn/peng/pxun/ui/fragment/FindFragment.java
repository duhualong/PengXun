package cn.peng.pxun.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.SearchActivity;

/**
 * Created by msi on 2016/12/21.
 */
public class FindFragment extends BaseFragment {

    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.iv_title_search)
    ImageView mIvTitleSearch;


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_find, null);

        return view;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initData() {
        mTvTitleText.setText("发现");
        mIvTitleSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        mIvTitleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("searchType", SearchActivity.SEARCH_USER);
                startActivity(intent);
            }
        });

    }
}
