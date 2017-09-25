package cn.peng.pxun.presenter.activity;

import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;

import java.util.ArrayList;
import java.util.Random;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.Constant;
import cn.peng.pxun.modle.bean.TuringBean;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.modle.bean.VoiceBean;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.utils.UIUtils;
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
     * 初始化图灵机器人
     */
    public void initTuring() {
        SDKInitBuilder builder = new SDKInitBuilder(mActivity).setSecret("77bd9b637dd3aff6").
                setTuringKey(Constant.TURING_APP_KEY).setUniqueId("1136313078");
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
     * 请求图灵机器人的接口
     * @param ask
     */
    public void requestTuring(String ask) {
        if (mTaManager != null){
            mTaManager.requestTuringAPI(ask);
        }
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
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(UIUtils.getContext(), null);
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

    public String getPicURL() {
        String pic_url = Constant.BELLE_PIC[new Random().nextInt(Constant.BELLE_PIC.length)];
        return pic_url;
    }
}
