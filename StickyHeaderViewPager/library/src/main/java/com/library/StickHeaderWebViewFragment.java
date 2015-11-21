package com.library;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.library.scroll.NestingWebViewScrollView;

/**
 * Created by sj on 15/11/20.
 */
public abstract class StickHeaderWebViewFragment extends StickHeaderScrollViewFragment {

    int layoutId;

    public StickHeaderWebViewFragment() { }

    public StickHeaderWebViewFragment(int layoutId) { this.layoutId = layoutId; }

    WebView webView;

    @Override
    public final ScrollView createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        webView = createWebView(inflater,container,savedInstanceState);
        NestingWebViewScrollView myScrollView = new NestingWebViewScrollView(getContext());
        myScrollView.addView(webView);
        return myScrollView;
    }

    public abstract WebView createWebView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public WebView getWebView() {
        return webView;
    }
}
