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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floating;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private RelativeLayout relativeLayout;
    private FloatingActionButton floating_1;
    private FloatingActionButton floating_2;
    private FloatingActionButton floating_3;
    private boolean isFloat_3;
    private int mPosition = 0;
    private boolean animCheck;
    private List<ViewModel> viewModel;
    private Animation animation;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFloating();
        initPermissions();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        floating = findViewById(R.id.floating);
        relativeLayout = findViewById(R.id.rel_floating_group);
        floating_1 = findViewById(R.id.floating_1);
        floating_2 = findViewById(R.id.floating_2);
        floating_3 = findViewById(R.id.floating_3);
    }

    private void initData() {
        viewModel = VideoFileUtil.getInstance().returnViewModel(MainActivity.this);
        pagerAdapter = new PagerAdapter(viewModel);
        viewPager.setAdapter(pagerAdapter);
        loaderView();
    }

    private void initFloating() {
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animCheck){ //顺时针旋转
                    animCheck = false;
                    foAnim();
                }else { //逆时针旋转
                    animCheck = true;
                    toAnim();
                }
                animation.setFillAfter(true);   //动画结束停留在旋转后的位置
                floating.startAnimation(animation);
            }
        });

        floating_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareText(MainActivity.this,"这是一段分享的文字","分享文本");
            }
        });

        floating_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFloat_3){
                    isFloat_3 = false;
                    floating_3.setImageResource(R.drawable.icon_praise);
                }else {
                    isFloat_3 = true;
                    floating_3.setImageResource(R.drawable.icon_praise_ok);
                }
            }
        });
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

    private void toAnim(){
        relativeLayout.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_floating_positive);
    }

    private void foAnim(){
        relativeLayout.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_floating_negative);
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
    protected void onRestart() {
        super.onRestart();
        loaderView();
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
