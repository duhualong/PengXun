package cn.peng.pxun.presenter.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.BasePresenter;
import cn.peng.pxun.ui.activity.UserInfoActivity;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by tofirst on 2017/11/6.
 */

public class UserInfoPresenter extends BasePresenter{
    private UserInfoActivity activity;

    public UserInfoPresenter(UserInfoActivity activity) {
        super(activity);
        this.activity = activity;
    }

    public void upDataUserInfo(User user){
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    activity.updataResult(AppConfig.SUCCESS);
                } else {
                    activity.updataResult(AppConfig.ERROR);
                }
            }
        });
    }

    public void startCamera(int requestCode) {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "temp"+ ".jpg";// 照片命名
        String filePath = AppConfig.CACHEPATH;
        File out = new File(AppConfig.CACHEPATH);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(filePath, fileName);
        String imageFilePath = filePath + fileName;// 该照片的绝对路径
        Uri uri = Uri.fromFile(out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 2);
        imageCaptureIntent.putExtra("FilePath", imageFilePath);
        activity.startActivityForResult(imageCaptureIntent, requestCode);
    }

    public void upLoadIcon(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    String iconPath = bmobFile.getFileUrl();
                    activity.onupLoadIconFinish(iconPath);
                }else{
                    LogUtil.e("头像上传", e.getMessage());
                    activity.onupLoadIconFinish(null);
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    public void startPicDepot(int requestCode) {
        Uri uri = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    public Uri getUri(Intent intent) {
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            return uri;
        }
        uri = intent.getData();
        return uri;
    }

    public String getPathFromUri(Uri uri) {
        String path = "";
        path = uri.getPath();

        try {
            ContentResolver cr = activity.getContentResolver();
            Cursor cursor = cr.query(uri, new String[] { MediaStore.MediaColumns.DATA },
                    null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}
