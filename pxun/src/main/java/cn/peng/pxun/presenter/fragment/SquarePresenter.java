package cn.peng.pxun.presenter.fragment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.fragment.SquareFragment;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by msi on 2017/11/25.
 */

public class SquarePresenter extends BasePresenter{

    private SquareFragment fragment;

    public SquarePresenter(SquareFragment fragment) {
        super(fragment);
        this.fragment = fragment;
    }

    public void getPostsList() {
        BmobQuery<Posts> bmobQuery = new BmobQuery();
        bmobQuery.addWhereNotEqualTo("postsType", 101);

        bmobQuery.findObjects(new FindListener<Posts>(){
            @Override
            public void done(List<Posts> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    fragment.setData(list);
                } else {
                    if (e != null){
                        LogUtil.e(e.toString());
                    }
                    fragment.setEmptyPage();
                }
            }
        });
    }
}
