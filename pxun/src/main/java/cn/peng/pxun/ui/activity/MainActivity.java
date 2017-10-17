package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.peng.pxun.MyApplication;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.adapter.MenuAdapter;
import cn.peng.pxun.ui.fragment.FindFragment;
import cn.peng.pxun.ui.fragment.MainFragment;
import cn.peng.pxun.ui.fragment.MineFragment;
import cn.peng.pxun.utils.ThreadUtils;
import cn.peng.pxun.utils.ToastUtil;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fl_root)
    FrameLayout mFlRoot;
    @BindView(R.id.tv_toolbar_title)
    public TextView mTvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.iv_main_main)
    ImageView mIvMainMain;
    @BindView(R.id.tv_main_main)
    TextView mTvMainMain;
    @BindView(R.id.ll_main_mian)
    LinearLayout mLlMainMian;
    @BindView(R.id.iv_main_find)
    ImageView mIvMainFind;
    @BindView(R.id.tv_main_find)
    TextView mTvMainFind;
    @BindView(R.id.ll_main_find)
    LinearLayout mLlMainFind;
    @BindView(R.id.iv_main_mine)
    ImageView mIvMainMine;
    @BindView(R.id.tv_main_mine)
    TextView mTvMainMine;
    @BindView(R.id.ll_main_mine)
    LinearLayout mLlMainMine;

    private GuillotineAnimation mMenu;
    private ListView mLv_menu;
    private Button mBt_loginout;
    private FragmentManager mFm;
    private MainFragment mainFragment;
    private FindFragment findFragment;
    private MineFragment mineFragment;
    private static Boolean isExit = false;


    @Override
    public int setLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();

        SDKInitBuilder builder = new SDKInitBuilder(this).setSecret("77bd9b637dd3aff6").
                setTuringKey(AppConfig.TURING_APP_KEY).setUniqueId("1136313078");
        SDKInit.init(builder, new InitListener() {
            @Override
            public void onComplete() {
                AppConfig.isInitTuring = true;
                Log.i("ChatActivity","图灵机器人初始化成功");
            }
            @Override
            public void onFail(String s) {
                AppConfig.isInitTuring = false;
                Log.i("ChatActivity","图灵机器人初始化失败");
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        View menuLayout = LayoutInflater.from(this).inflate(R.layout.menu_main, null);
        mFlRoot.addView(menuLayout);
        mMenu = new GuillotineAnimation.GuillotineBuilder(menuLayout, menuLayout.findViewById(R.id.iv_menu90), mIvMenu)
                .setStartDelay(200)
                .setActionBarViewForAnimation(mToolbar)
                .setClosedOnStart(true)
                .build();
        mBt_loginout = (Button) menuLayout.findViewById(R.id.bt_loginout);
        mLv_menu = (ListView) menuLayout.findViewById(R.id.lv_menu);
        mLv_menu.setAdapter(new MenuAdapter());

        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        if (findFragment == null) {
            findFragment = new FindFragment();
        }
        if (mineFragment == null) {
            mineFragment = new MineFragment();
        }

        //初始化Fragment
        mFm = getSupportFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        ft.replace(R.id.fl_main, mainFragment);
        ft.commit();

        //刷新ActionBar和按钮标签
        mTvToolbarTitle.setText("消息");
        refreshTab(mTvMainMain, mIvMainMain, mTvMainFind, mIvMainFind, mTvMainMine, mIvMainMine);
    }

    @Override
    protected void initListener() {
        mLlMainMian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, mainFragment);
                ft.commit();
                mTvToolbarTitle.setText("消息");
                refreshTab(mTvMainMain, mIvMainMain, mTvMainFind, mIvMainFind, mTvMainMine, mIvMainMine);
            }
        });
        mLlMainFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, findFragment);
                ft.commit();
                mTvToolbarTitle.setText("发现");
                refreshTab(mTvMainFind, mIvMainFind, mTvMainMain, mIvMainMain, mTvMainMine, mIvMainMine);
            }
        });
        mLlMainMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mFm.beginTransaction();
                ft.replace(R.id.fl_main, mineFragment);
                ft.commit();
                mTvToolbarTitle.setText("我的");
                refreshTab(mTvMainMine, mIvMainMine, mTvMainFind, mIvMainFind, mTvMainMain, mIvMainMain);
            }
        });
        mBt_loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                //清空登录信息
                                SharedPreferences.Editor editor = MyApplication.sp.edit();
                                editor.putBoolean("isLogin", false);
                                editor.commit();
                                //返回登录界面
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(MainActivity.this, "注销帐号失败,请稍后重试!");
                            }
                        });
                    }
                });
            }
        });
        mLv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this, SearchActivity.class);
                        intent.putExtra("searchType",SearchActivity.SEARCH_USER);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, CreateGroupActivity.class);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, SearchActivity.class);
                        intent.putExtra("searchType",SearchActivity.SEARCH_GROUP);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this, FeedbackActivity.class);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, SettingActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("searchType",SearchActivity.SEARCH_USER);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mMenu.isOpen) {
                mMenu.close();
            } else {
                Timer tExit = null;
                if (isExit == false) {
                    isExit = true; // 准备退出
                    ToastUtil.showToast(this, "在按一次退出鹏讯");

                    tExit = new Timer();
                    tExit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isExit = false; // 取消退出
                        }
                    }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

                } else {
                    finish();
                    System.exit(0);
                }
            }
        }
        return false;
    }


    /**
     * 刷新按钮标签的UI
     */
    private void refreshTab(TextView tv1, ImageView iv1, TextView tv2, ImageView iv2, TextView tv3, ImageView iv3) {
        tv1.setTextColor(Color.parseColor("#45C01A"));
        iv1.setSelected(true);
        tv2.setTextColor(Color.parseColor("#999999"));
        iv2.setSelected(false);
        tv3.setTextColor(Color.parseColor("#999999"));
        iv3.setSelected(false);
    }
}
