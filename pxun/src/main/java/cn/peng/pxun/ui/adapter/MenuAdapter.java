package cn.peng.pxun.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.Constant;

/**
 * Created by msi on 2017/1/1.
 */

public class MenuAdapter extends BaseAdapter {
    String[] items;

    public MenuAdapter(){
        this.items = Constant.MENU_ITEMS;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(MyApplication.context);
        tv.setText(items[position]);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setTextSize(24);
        return tv;
    }
}
