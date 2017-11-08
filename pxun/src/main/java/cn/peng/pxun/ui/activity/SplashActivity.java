package cn.peng.pxun.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.utils.AppUtil;

public class SplashActivity extends Activity {
    private  boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_splash);

        isLogin = MyApplication.sp.getBoolean("isLogin", false);
        loadActivity();
        if (isLogin){
            AppUtil.setAppUser();
        }
    }

    /**
     * 加载Activity
     */
    private void loadActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isLogin){
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
