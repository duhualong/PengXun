package cn.peng.pxun.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.ConversationBean;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;

/**
 * Created by msi on 2017/9/27.
 */

public class MessageAdapter extends SuperBaseApapter {

    public MessageAdapter(List dataSets) {
        super(dataSets);
    }

    @Override
    public BaseHolder setHolder() {
        return new MessageHolder();
    }

    public class MessageHolder extends BaseHolder<ConversationBean> {
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
            if ("系统消息".equals(mData.userName)){
                mIvMessageIcon.setImageResource(R.drawable.icon_sys_message);
            }else if ("智能小白".equals(mData.userName)){
                mIvMessageIcon.setImageResource(R.drawable.head6);
            }else{
                mIvMessageIcon.setImageResource(AppConfig.icons[new Random().nextInt(AppConfig.icons.length)]);
            }
            mTvMessageName.setText(mData.userName);
            mTvMessageSignature.setText(mData.lastMsg);
        }
    }
}