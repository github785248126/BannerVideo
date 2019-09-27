package com.template.bannervideo;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import java.util.List;

/**
 * 创建日期：2019/9/27
 * 创建人：崔斌浩
 * QQ:785248126
 * 说明：ViewPager轮播适配器
 */
public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
    private List<ViewModel> viewModelList;

    public PagerAdapter(List<ViewModel> viewList) {
        this.viewModelList = viewList;
    }

    @Override
    public int getCount() {
        return viewModelList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewModelList.get(position).getView());
        return viewModelList.get(position).getView();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewModelList.get(position).getView());
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
