package cn.peng.pxun.presenter.fragment;


import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.MovieBean;
import cn.peng.pxun.presenter.base.BaseMoviePresenter;
import cn.peng.pxun.server.MovieServer;
import cn.peng.pxun.ui.fragment.MovieFragment;
import cn.peng.pxun.utils.LogUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tofirst on 2017/10/30.
 */

public class MoviePresenter extends BaseMoviePresenter {
    private MovieFragment fragment;

    public MoviePresenter(MovieFragment fragment) {
        super(fragment);
        this.fragment = fragment;
    }

    public void getMovieList(int start, int end) {
        retrofit.create(MovieServer.class).getInTheatersMovieList(start, end)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!isNetUsable(mContext)) {
                            showToast("网络不可用");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onNext(@NonNull MovieBean movieBean) {
                        fragment.setData(movieBean, AppConfig.SUCCESS);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        fragment.setData(null, AppConfig.ERROR);
                        LogUtil.e("error:" + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d("getMovieList>>onComplete");
                    }
                });
    }
}
