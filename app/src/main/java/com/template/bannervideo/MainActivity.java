package com.template.bannervideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floating;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int mPosition = 0;
    private List<ViewModel> viewModel;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPermissions();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        floating = findViewById(R.id.floating);
    }

    private void initData() {
        viewModel = VideoFileUtil.getInstance().returnViewModel(MainActivity.this);
        pagerAdapter = new PagerAdapter(viewModel);
        viewPager.setAdapter(pagerAdapter);
        loaderView();
    }

    /**
     * 加载视频图片
     */
    private void loaderView() {
        ViewModel model = viewModel.get(mPosition);
        String path = model.getPath();

        if (path.contains(".mp4")) {
            CustomerVideoView customerVideoView = (CustomerVideoView) model.getView();
            customerVideoView.setOnCompletionListener(onCompletionListener);
            customerVideoView.start();
        } else {
            countDownTimer.start();
        }
    }

    /**
     * 监听视频播放完成
     */
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mPosition == viewModel.size() - 1) {
                mPosition = 0;
            } else {
                mPosition++;
            }

            viewPager.setCurrentItem(mPosition);
            loaderView();
        }
    };

    /**
     * 计时器
     */
    private CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (mPosition == viewModel.size() - 1) {
                mPosition = 0;
            } else {
                mPosition++;
            }

            viewPager.setCurrentItem(mPosition);
            loaderView();
        }
    };

    private void initPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, permissions, 0);
            } else {
                initData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    finish();
                } else {
                    initData();
                }
            }
        }
    }
}
