package com.template.bannervideo;

import android.view.View;

public class ViewModel {
    private String path;
    private View view;

    public ViewModel() {
    }

    public ViewModel(String path, View view) {
        this.path = path;
        this.view = view;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
