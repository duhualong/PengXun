package cn.peng.pxun.ui.adapter.listview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;

/**
 * Created by msi on 2016/12/26.
 */
public class GroupAdapter  extends SuperBaseApapter<EMGroup> {

    public GroupAdapter(List<EMGroup> dataSets) {
        super(dataSets);
    }

    @Override
    public GroupHolder setHolder() {
        return new GroupHolder();
    }

    class GroupHolder extends BaseHolder<EMGroup> {
        @BindView(R.id.iv_message_icon)
        ImageView mIvMessageIcon;
        @BindView(R.id.tv_message_name)
        TextView mTvMessageName;
        @BindView(R.id.tv_message_signature)
        TextView mTvMessageSignature;

        @Override
        public View initHolderView() {
            View view = View.inflate(MyApplication.context, R.layout.item_message, null);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void bindView() {
            mIvMessageIcon.setImageResource(R.drawable.icon_group);
            mTvMessageName.setText(mData.getGroupName());
            mTvMessageSignature.setText(mData.getDescription());
        }
    }
}
