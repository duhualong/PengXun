package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.modle.greendao.MessageDao;
import cn.peng.pxun.presenter.activity.ChatPresenter;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;
import cn.peng.pxun.ui.adapter.holder.ChatHolder;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by msi on 2016/12/30.
 */
public class ChatActivity extends BaseActivity<ChatPresenter> {
    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.chat_toolbar)
    Toolbar mChatToolbar;
    @BindView(R.id.lv_chat)
    ListView mLvChat;
    @BindView(R.id.ib_chat_inputtype)
    ImageButton mIbChatInputtype;
    @BindView(R.id.bt_chat_speech)
    Button mBtChatSpeech;
    @BindView(R.id.et_chat_spell)
    EditText mEtChatSpell;
    @BindView(R.id.bt_chat_send)
    Button mBtChatSend;
    @BindView(R.id.ll_chat_spell)
    LinearLayout mLlChatSpell;

    private String mUsername;
    private boolean isSpeech = true;
    private List<Message> list;
    private ChatAdapter mAdapter;

    @Override
    protected void init() {
        super.init();
        //初始化科大讯飞语音识别
        SpeechUtility.createUtility(this, "appid=" + AppConfig.IFLYTEK_APPID);
        //注册EventBus
        EventBus.getDefault().register(this);

        mUsername = getIntent().getStringExtra("username");
        list = new ArrayList<>();
        if ("智能小白".equals(mUsername)){
            presenter.initTuring();
            MessageDao messageDao = MyApplication.session.getMessageDao();
            QueryBuilder queryBuilder = messageDao.queryBuilder();
            list = queryBuilder.where(queryBuilder
                    .or(queryBuilder
                            .and(MessageDao.Properties.FromUserID.eq(MyApplication.sp.getString("phone","")),
                                    MessageDao.Properties.ToUserID.eq("tuling")),queryBuilder
                            .and(MessageDao.Properties.FromUserID.eq("tuling"),
                                    MessageDao.Properties.ToUserID.eq(MyApplication.sp.getString("phone","")))))
                    .list();
        }else {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mUsername);
            if (conversation != null) {
                EMMessage lastMessage = conversation.getLastMessage();
                int count = 19;
                if (list.size() >= 19) {
                    count = list.size() + 1;
                }
                List<EMMessage> messages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
                messages.add(lastMessage);

                if (messages != null && messages.size() > 0){
                    for (EMMessage emMsg : messages) {
                        Message msg = new Message();
                        msg.date = getDate(emMsg.getMsgTime());
                        msg.message = emMsg.getBody().toString().split(":")[1].replaceAll("\"", "");
                        msg.fromUserID = emMsg.getFrom();
                        msg.toUserID = emMsg.getTo();
                        msg.messageType = 1;
                        msg.isTuring = false;
                        list.add(msg);
                    }
                }
            }
        }
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvChatTitle.setText("与"+mUsername+"聊天中");
        if (list != null && list.size() > 0){
            mAdapter = new ChatAdapter(list);
            mLvChat.setAdapter(mAdapter);
            mLvChat.setSelection(list.size()-1);
        }
    }

    @Override
    protected void initListener() {
        mIvChatGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIbChatInputtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSpeech){
                    mIbChatInputtype.setBackgroundResource(R.drawable.spellinput);
                    mLlChatSpell.setVisibility(View.GONE);
                    mBtChatSpeech.setVisibility(View.VISIBLE);
                }else {
                    mIbChatInputtype.setBackgroundResource(R.drawable.speechinput);
                    mLlChatSpell.setVisibility(View.VISIBLE);
                    mBtChatSpeech.setVisibility(View.GONE);
                }
                isSpeech = !isSpeech;
            }
        });
        mBtChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String spellMsg = mEtChatSpell.getText().toString().trim();
                if (TextUtils.isEmpty(spellMsg)){
                    ToastUtil.showToast(ChatActivity.this,"发送消息不能为空");
                    return;
                }
                mEtChatSpell.setText("");

                Message msg = new Message();
                msg.date = getDate(System.currentTimeMillis());
                msg.message = spellMsg;
                msg.fromUserID = MyApplication.sp.getString("phone","");
                msg.messageType = Message.TEXT_TYPE;
                if ("智能小白".equals(mUsername)) {
                    msg.isTuring = true;
                    msg.toUserID = "tuling";
                    presenter.requestTuring(spellMsg);
                }else {
                    msg.isTuring = false;
                    msg.toUserID = mUsername;
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            EMMessage message = EMMessage.createTxtSendMessage(spellMsg, mUsername);
                            EMClient.getInstance().chatManager().sendMessage(message);
                        }
                    });
                }
                addDataAndRefreshUi(msg,false);
            }
        });
        mBtChatSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognizerDialog mDialog = new RecognizerDialog(ChatActivity.this, null);
                mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                final StringBuffer mBuffer = new StringBuffer();
                mDialog.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        String result = recognizerResult.getResultString();
                        String resultString = presenter.processData(result);

                        mBuffer.append(resultString);
                        if (b) {
                            final String speechMsg = mBuffer.toString();

                            Message msg = new Message();
                            msg.date = getDate(System.currentTimeMillis());
                            msg.message = speechMsg;
                            msg.fromUserID = MyApplication.sp.getString("phone","");
                            msg.messageType = Message.TEXT_TYPE;

                            if ("智能小白".equals(mUsername)) {
                                msg.isTuring = true;
                                msg.toUserID = "tuling";
                                presenter.requestTuring(speechMsg);
                            }else {
                                msg.isTuring = false;
                                msg.toUserID = mUsername;
                                ThreadUtils.runOnSubThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMMessage message = EMMessage.createTxtSendMessage(speechMsg, mUsername);
                                        EMClient.getInstance().chatManager().sendMessage(message);
                                    }
                                });
                            }
                            addDataAndRefreshUi(msg,false);
                        }
                    }

                    @Override
                    public void onError(SpeechError error) {
                        ToastUtil.showToast(ChatActivity.this,"语音解析失败");
                    }
                });
                mDialog.show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除EventBus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void startBigPicActivity(Intent intent){
        intent.setClass(this,BigPicActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getMessage(Message msg){
        addDataAndRefreshUi(msg,false);
    }

    /**
     * 添加数据并且刷新UI
     * @param msg
     * @param isSpeak
     */
    public void addDataAndRefreshUi(Message msg, boolean isSpeak) {
        list.add(msg);
        if (mAdapter == null){
            mAdapter = new ChatAdapter(list);
            mLvChat.setAdapter(mAdapter);
        }
        mAdapter.setDataSets(list);
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(list.size()-1);
        if (msg.isTuring){
            keepMessage(msg);
            if (isSpeak){
                presenter.startSpeak(msg.message);
            }
        }
    }

    /**
     * 保存聊天记录
     */
    private void keepMessage(Message msg) {
        MessageDao messageDao = MyApplication.session.getMessageDao();
        messageDao.insert(msg);
    }

    /**
     * 获取当前时间
     * @return
     */
    public String getDate(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm ");
        return  formatter.format(date);
    }

    class ChatAdapter extends SuperBaseApapter<Message>{
        public ChatAdapter(List<Message> dataSets) {
            super(dataSets);
        }

        @Override
        public BaseHolder setHolder() {
            return new ChatHolder(ChatActivity.this);
        }
    }
}
