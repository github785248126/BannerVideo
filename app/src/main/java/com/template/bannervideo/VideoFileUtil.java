package com.template.bannervideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2019/9/27
 * 创建人：崔斌浩
 * QQ:785248126
 * 说明：副屏广告工具类
 */
public class VideoFileUtil {
    public static VideoFileUtil videoFileUtil;
    private String VIDEO_PATH = "/cashierMedia";
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }
    };

    private VideoFileUtil(){}

    public static VideoFileUtil getInstance(){
        if (videoFileUtil == null) {
            synchronized (VideoFileUtil.class){
                if (videoFileUtil == null) {
                    videoFileUtil = new VideoFileUtil();
                }
            }
        }
        return videoFileUtil;
    }

    /**
     * 文件路径
     * @return
     */
    private String videoFilePath(){
        return Environment.getExternalStorageDirectory() + VIDEO_PATH;
    }

    /**
     * 文件夹下所有的文件路径
     * @return
     */
    private List<String> videoPathList(){
        List<String> pathList = new ArrayList<>();
        File file = new File(videoFilePath());
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                pathList.add(files[i].getAbsolutePath());
            }
        } else {
            file.mkdir();
        }
        return pathList;
    }

    /**
     * 判断文件类型添加View
     * @return
     */
    public List<ViewModel> returnViewModel(Context context){
        List<ViewModel> videoModelList = new ArrayList<>();
        List<String> pathList = videoPathList();

        for (int i = 0; i < pathList.size(); i++) {
            String mPath = pathList.get(i);
            if (mPath.contains(".mp4")) {
                CustomerVideoView videoView = initVideoView(context, mPath);
                videoModelList.add(new ViewModel(mPath,videoView));
            } else {
                ImageView imageView = initImageView(context,mPath);
                videoModelList.add(new ViewModel(mPath,imageView));
            }
        }
        return videoModelList;
    }

    /**
     * 获取视频控件
     * @param context
     * @param path
     * @return
     */
    private CustomerVideoView initVideoView(Context context,String path){
        CustomerVideoView videoView = new CustomerVideoView(context);
        videoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.setVideoPath(path);
        videoView.setOnPreparedListener(onPreparedListener);
        return videoView;
    }

    /**
     * 获取图片控件
     * @param context
     * @param path
     * @return
     */
    private ImageView initImageView(Context context,String path){
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(Uri.parse(path));
        return imageView;
    }
}
