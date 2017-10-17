package cn.peng.pxun.presenter.activity;

import android.util.Log;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.TuringBean;
import cn.peng.pxun.modle.bean.VoiceBean;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.modle.greendao.MessageDao;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.utils.ThreadUtils;
import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

/**
 * ChatActivity的业务类
 */
public class ChatPresenter extends BasePresenter{
    private ChatActivity mActivity;
    /** 机器人管理器 */
    private TuringApiManager mTaManager;

    public ChatPresenter(BaseActivity activity) {
        super(activity);
        mActivity = (ChatActivity) activity;
    }

    /**
     * 解析语音录入的每个字符
     * @param result
     * @return 解析后的字符
     */
    public String processData(String result) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(result, VoiceBean.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<VoiceBean.WsBean> ws = voiceBean.ws;
        for (VoiceBean.WsBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * 让机器人把汉字说出来
     * @param answerContent 要讲的话
     */
    public void startSpeak(String answerContent) {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(mActivity, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录12.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "60");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(answerContent, null);
    }

    /**
     * 初始化图灵机器人
     */
    public void initTuring() {
        SDKInitBuilder builder = new SDKInitBuilder(mActivity).setSecret("77bd9b637dd3aff6").
                setTuringKey(AppConfig.TURING_APP_KEY).setUniqueId("1136313078");
        SDKInit.init(builder, new InitListener() {
            @Override
            public void onComplete() {
                mTaManager = new TuringApiManager(mActivity);
                mTaManager.setHttpListener(new HttpConnectionListener() {
                    @Override
                    public void onError(ErrorMessage message) {
                        Message msg = new Message();
                        msg.isTuring = true;
                        msg.message = "对不起,你的话太深奥了!";
                        msg.fromUserID = "tuling";
                        msg.toUserID = MyApplication.sp.getString("phone","");
                        msg.messageType = Message.TEXT_TYPE;

                        mActivity.addDataAndRefreshUi(msg,false);
                    }
                    @Override
                    public void onSuccess(RequestResult result) {
                        String s = result.getContent().toString();
                        Gson gson = new Gson();
                        TuringBean turing = gson.fromJson(s,TuringBean.class);

                        Message msg = new Message();
                        msg.isTuring = true;
                        msg.message = turing.text;
                        msg.fromUserID = "tuling";
                        msg.toUserID = MyApplication.sp.getString("phone","");
                        if (turing.code == 200000){
                            msg.messageType = Message.PIC_TYPE;
                            msg.picURL = getPicURL();
                        }else {
                            msg.messageType = Message.TEXT_TYPE;
                        }

                        mActivity.addDataAndRefreshUi(msg,true);
                    }
                });
            }
            @Override
            public void onFail(String s) {
                Log.i("ChatActivity","图灵机器人初始化失败");
                Message msg = new Message();
                msg.isTuring = true;
                msg.message = "智能小白初始化失败";
                msg.fromUserID = "tuling";
                msg.toUserID = MyApplication.sp.getString("phone","");
                msg.messageType = Message.TEXT_TYPE;

                mActivity.addDataAndRefreshUi(msg,false);
            }
        });
    }

    /**
     * 获取图灵机器人的聊天记录
     * @return
     */
    public List<Message> getTulingMessages() {
        MessageDao messageDao = MyApplication.session.getMessageDao();
        QueryBuilder queryBuilder = messageDao.queryBuilder();
        List<Message> list = queryBuilder.where(queryBuilder
                .or(queryBuilder
                        .and(MessageDao.Properties.FromUserID.eq(MyApplication.sp.getString("phone", "")),
                                MessageDao.Properties.ToUserID.eq("tuling")), queryBuilder
                        .and(MessageDao.Properties.FromUserID.eq("tuling"),
                                MessageDao.Properties.ToUserID.eq(MyApplication.sp.getString("phone", "")))))
                .list();
        return list;
    }

    /**
     * 从环信获取用户聊天记录
     * @param toChatUserId
     * @return
     */
    public List<Message> getHuanxinMessages(String toChatUserId) {
        List<Message> list = new ArrayList<>();
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUserId);
        if (conversation != null) {
            EMMessage lastMessage = conversation.getLastMessage();
            int count = 19;
            if (list.size() >= 19) {
                count = list.size() + 1;
            }
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            messages.add(lastMessage);

            if (messages != null && messages.size() > 0) {
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
        return list;
    }

    /**
     * 请求图灵机器人的接口
     * @param ask
     */
    public void requestTuring(String ask) {
        if (mTaManager != null){
            mTaManager.requestTuringAPI(ask);
        }
    }

    /**
     * 异步发送文本消息
     * @param msg
     */
    public void sendTextMessage(final String msg, final String toChatUserId, final boolean isGroup) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = EMMessage.createTxtSendMessage(msg, toChatUserId);
                if (isGroup) {
                    message.setChatType(EMMessage.ChatType.GroupChat);
                }
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }

    /**
     * 随机获取一张图片的url
     * @return
     */
    public String getPicURL() {
        String pic_url = AppConfig.BELLE_PIC[new Random().nextInt(AppConfig.BELLE_PIC.length)];
        return pic_url;
    }

    /**
     * 获取当前时间
     * @return
     */
    public String getDate(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm ");
        return formatter.format(date);
    }

    /**
     * 保存聊天记录
     */
    public void keepMessage(Message msg) {
        MessageDao messageDao = MyApplication.session.getMessageDao();
        messageDao.insert(msg);
    }

}
