package cn.peng.pxun.presenter.base;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tofirst on 2017/10/30.
 */

public class BaseMoviePresenter extends BasePresenter{
    protected Retrofit retrofit;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            // 添加通用的Header
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
                    return chain.proceed(builder.build());
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public BaseMoviePresenter(BaseActivity activity) {
        super(activity);
        initRetrofit();
    }

    public BaseMoviePresenter(BaseFragment fragment) {
        super(fragment);
        initRetrofit();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_MOVIE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

}
