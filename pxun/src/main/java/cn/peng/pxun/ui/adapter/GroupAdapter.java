package cn.peng.pxun.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;

import java.util.List;

import cn.peng.pxun.MyApplication;

/**
 * Created by msi on 2016/12/26.
 */
public class GroupAdapter extends BaseAdapter {
    private List<EMGroup> list;

    public GroupAdapter(List<EMGroup> grouplist) {
        list = grouplist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv_group = new TextView(MyApplication.context);
        tv_group.setText(list.get(position).getGroupName());
        tv_group.setTextSize(24);
        tv_group.setTextColor(Color.parseColor("#000000"));
        return tv_group;
    }
}
