package cn.peng.pxun.ui.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bean.Contacts;
import cn.peng.pxun.utils.UIUtils;

/**
 * MessageFragment的ViewHolder
 */
public class MessageHolder extends BaseHolder<Contacts> {
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
        mIvMessageIcon.setImageDrawable(mData.userIcon);
        mTvMessageName.setText(mData.userName);
        mTvMessageSignature.setText(mData.signature);
    }
}
