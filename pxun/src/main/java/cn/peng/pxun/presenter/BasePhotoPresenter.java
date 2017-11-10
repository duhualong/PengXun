package cn.peng.pxun.presenter;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.LogUtil;

/**
 * Created by tofirst on 2017/11/9.
 */

public class BasePhotoPresenter extends BasePresenter{
    private UpLoadFileListener mUpLoadFileListener;

    public BasePhotoPresenter(BaseActivity activity) {
        super(activity);
    }

    public BasePhotoPresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 上传文件到Bmob后台
     * @param path 文件路径
     */
    public void upLoadFile(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    String iconPath = bmobFile.getFileUrl();
                    if (mUpLoadFileListener != null){
                        mUpLoadFileListener.onUpLoadFinish(iconPath);
                    }
                }else{
                    LogUtil.e("头像上传", e.getMessage());
                    if (mUpLoadFileListener != null) {
                        mUpLoadFileListener.onUpLoadFinish("");
                    }
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                if (mUpLoadFileListener != null) {
                    mUpLoadFileListener.onUpLoadProgress(value);
                }
            }
        });
    }

    /**
     * 启动系统照相机
     * @param requestCode
     */
    public void startCamera(int requestCode) {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "temp"+ ".jpg";// 照片命名
        String filePath = AppConfig.CACHE_PATH;
        File out = new File(AppConfig.CACHE_PATH);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(filePath, fileName);
        String imageFilePath = filePath + fileName;// 该照片的绝对路径
        Uri uri = Uri.fromFile(out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 2);
        imageCaptureIntent.putExtra("FilePath", imageFilePath);
        context.startActivityForResult(imageCaptureIntent, requestCode);
    }

    /**
     * 启动系统图库
     * @param requestCode
     */
    public void startPicDepot(int requestCode) {
        Uri uri = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 从Intent对象中获取Uri
     * @param intent
     * @return
     */
    public Uri getUri(Intent intent) {
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            return uri;
        }
        uri = intent.getData();
        return uri;
    }

    /**
     * 从Uri对象中获取文件路径
     * @param uri
     * @return
     */
    public String getPathFromUri(Uri uri) {
        String path = "";
        path = uri.getPath();

        try {
            ContentResolver cr = context.getContentResolver();
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

    public void setUpLoadFileListener(UpLoadFileListener listener){
        this.mUpLoadFileListener = listener;
    }

    /**
     * 上传文件的回调接口
     */
    public interface UpLoadFileListener {

        void onUpLoadFinish(String path);

        void onUpLoadProgress(int value);
    }
}
