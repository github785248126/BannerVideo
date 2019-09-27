package com.template.bannervideo;

import android.content.Context;

/**
 * 创建日期：2019/9/27
 * 创建人：崔斌浩
 * QQ:785248126
 * 说明：
 */
public class CustomerVideoView extends android.widget.VideoView {
    public CustomerVideoView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 其实就是在这里做了一些处理。
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
