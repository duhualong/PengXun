package cn.peng.pxun.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.R;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;
import cn.peng.pxun.utils.UIUtils;

/**
 * 联系人数据适配器
 */
public class ContactAdapter extends SuperBaseApapter<String> {

    public ContactAdapter(List<String> dataSets) {
        super(dataSets);
    }

    @Override
    public BaseHolder setHolder() {
        return new ContactHolder();
    }

    class ContactHolder extends BaseHolder<String> {
        @BindView(R.id.iv_message_icon)
        ImageView mIvMessageIcon;
        @BindView(R.id.tv_message_name)
        TextView mTvMessageName;
        @BindView(R.id.tv_message_signature)
        TextView mTvMessageSignature;

        @Override
        public View initHolderView() {
            View view = View.inflate(UIUtils.getContext(), R.layout.item_message, null);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void bindView() {
            mIvMessageIcon.setImageResource(R.drawable.headicon2);
            mTvMessageName.setText(mData);
            mTvMessageSignature.setText("");
        }
    }
}
