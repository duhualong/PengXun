package cn.peng.pxun.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bean.Group;
import cn.peng.pxun.modle.bean.User;
import cn.peng.pxun.ui.activity.SearchActivity;
import cn.peng.pxun.ui.adapter.holder.SearchHolder;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;

import static cn.peng.pxun.ui.activity.SearchActivity.SEARCH_GROUP;
import static cn.peng.pxun.ui.activity.SearchActivity.SEARCH_USER;

/**
 * Created by msi on 2017/9/23.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchHolder>{

    private SearchActivity activity;
    private List<User> userData;
    private List<Group> groupData;
    private int searchType;

    public SearchAdapter(SearchActivity activity){
        this.activity = activity;

    }

    public void setUserData(List<User> data){
        searchType = SEARCH_USER;
        this.userData = data;
        notifyDataSetChanged();
    }

    public void setGroupData(List<Group> data){
        searchType = SEARCH_GROUP;
        this.groupData = data;
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_search,null);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        if(searchType == SEARCH_USER){
            final User user = userData.get(position);
            holder.mTvMessageName.setText(user.getUsername());
            holder.mTvMessageSignature.setText(user.getSignaTure());
            holder.mIvAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user.getMobilePhoneNumber().equals(MyApplication.sp.getString("phone",""))){
                                ToastUtil.showToast(activity, "无法添加自己为好友");
                                return;
                            }
                            try {
                                EMClient.getInstance().contactManager().addContact(user.getMobilePhoneNumber(), "希望加你为好友,请同意!");
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(activity, "请求发送成功");
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(activity, "没有找到改用户");
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }else if (searchType == SEARCH_GROUP){
            final Group group = groupData.get(position);
            holder.mTvMessageName.setText(group.getGroupName());
            holder.mTvMessageSignature.setText(group.getGroupDesc());
            holder.mIvAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("20000".equals(group.getGroupType())){
                        ToastUtil.showToast(activity, "该群为私有群,不允许加入!");
                        return;
                    }
                    try {
                        EMClient.getInstance().groupManager().joinGroup(group.getGroupNum());
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(activity, "请求发送成功");
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(activity, "请求发送失败");
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (searchType == SEARCH_GROUP){
            if (groupData == null){
                return 0;
            }
            return groupData.size();
        }else {
            if (userData == null){
                return 0;
            }
            return userData.size();
        }
    }
}
