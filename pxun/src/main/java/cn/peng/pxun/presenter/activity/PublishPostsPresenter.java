package cn.peng.pxun.presenter.activity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.peng.pxun.modle.bmob.Posts;
import cn.peng.pxun.presenter.base.BasePhotoPresenter;
import cn.peng.pxun.ui.activity.PublishPostsActivity;

/**
 * Created by msi on 2017/11/26.
 */

public class PublishPostsPresenter extends BasePhotoPresenter{

    private PublishPostsActivity activity;

    public PublishPostsPresenter(PublishPostsActivity activity) {
        super(activity);
        this.activity = activity;
    }

    /**
     * 发表帖子
     * @param posts
     */
    public void publishPosts(Posts posts) {
        posts.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    activity.publishSuccess();
                } else {
                    showToast("服务器连接较慢，请稍后重试");
                }
            }
        });
    }
}
