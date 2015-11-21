package com.stickyheaderviewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.library.StickHeaderWebViewFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewSimpleFragment extends StickHeaderWebViewFragment {

    public static WebViewSimpleFragment newInstance() {
        WebViewSimpleFragment fragment = new WebViewSimpleFragment();
        return fragment;
    }

    public static WebViewSimpleFragment newInstance(String title) {
        WebViewSimpleFragment fragment = new WebViewSimpleFragment();
        fragment.setTitle(title);
        return fragment;
    }

    @Override
    public WebView createWebView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (WebView)inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void bindData() {
        WebView wv_content = getWebView();
        wv_content.loadUrl("http://www.github.com/w446108264");
        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
