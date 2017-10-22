package cn.peng.pxun.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.peng.pxun.R;
import cn.peng.pxun.ui.activity.SysMessageActivity;

/**
 * Created by tofirst on 2017/9/28.
 */

public class SysMessageAdapter extends RecyclerView.Adapter{
    private SysMessageActivity activity;

    public SysMessageAdapter(SysMessageActivity activity){
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_sys_message, null);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
