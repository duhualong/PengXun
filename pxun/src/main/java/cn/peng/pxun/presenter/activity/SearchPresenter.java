package cn.peng.pxun.presenter.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.peng.pxun.modle.bean.Group;
import cn.peng.pxun.modle.bean.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.SearchActivity;
import cn.peng.pxun.utils.ToastUtil;

/**
 * Created by msi on 2017/9/23.
 */
public class SearchPresenter extends BasePresenter {

    private SearchActivity activity;

    public SearchPresenter(SearchActivity activity) {
        super(activity);
        this.activity = activity;
    }

    public void search(int searchType, String content) {
        switch (searchType){
            case SearchActivity.SEARCH_USER:
                searchUser(content);
                break;
            case SearchActivity.SEARCH_GROUP:
                searchGroup(content);
                break;
        }

    }

    private void searchUser(String content) {
        BmobQuery<User> bmobQuery = new BmobQuery();
        List<BmobQuery<User>> params = new ArrayList<>();
        params.add(new BmobQuery<User>().addWhereEqualTo("username", content));
        params.add(new BmobQuery<User>().addWhereEqualTo("mobilePhoneNumber", content));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    activity.onUserSearch(list);
                } else {
                    ToastUtil.showToast(context,"查询失败：" + e.toString());
                    activity.onUserSearch(list);
                }
            }
        });
    }

    private void searchGroup(String content) {
        BmobQuery<Group> bmobQuery = new BmobQuery();
        List<BmobQuery<Group>> params = new ArrayList<>();
        params.add(new BmobQuery<Group>().addWhereEqualTo("groupName", content));
        params.add(new BmobQuery<Group>().addWhereEqualTo("groupNum", content));
        bmobQuery.or(params);
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Group>(){

            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null) {
                    activity.onGroupSearch(list);
                } else {
                    ToastUtil.showToast(context,"查询失败：" + e.toString());
                    activity.onGroupSearch(list);
                }
            }
        });
    }
}
