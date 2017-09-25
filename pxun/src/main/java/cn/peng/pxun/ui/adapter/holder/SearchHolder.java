package cn.peng.pxun.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.peng.pxun.R;

/**
 * Created by msi on 2017/9/23.
 */

public class SearchHolder extends RecyclerView.ViewHolder {

    public ImageView mIvMessageIcon;
    public TextView mTvMessageName;
    public TextView mTvMessageSignature;
    public ImageView mIvAddContact;

    public SearchHolder(View itemView) {
        super(itemView);
        mIvMessageIcon = (ImageView) itemView.findViewById(R.id.iv_message_icon);
        mTvMessageName = (TextView) itemView.findViewById(R.id.tv_message_name);
        mTvMessageSignature = (TextView) itemView.findViewById(R.id.tv_message_signature);
        mIvAddContact = (ImageView) itemView.findViewById(R.id.iv_add_contact);
    }
}
