package com.library;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.library.scroll.ScrollFragment;

/**
 * Created by sj on 15/11/20.
 */
public abstract class StickHeaderScrollViewFragment extends ScrollFragment<ScrollView> {

    int layoutId;

    public StickHeaderScrollViewFragment() { }

    public StickHeaderScrollViewFragment(int layoutId) { this.layoutId = layoutId; }

    @Override
    public ScrollView createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = layoutId > 0 ? (ScrollView) inflater.inflate(layoutId, container, false) : null;
        if (scrollView == null) {
            throw new IllegalStateException("ScrollView can't be null");
        }
        return scrollView;
    }
}
