package com.library;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.library.scroll.ScrollFragment;

/**
 * Created by sj on 15/11/20.
 */
public abstract class StickHeaderRecyclerViewFragment extends ScrollFragment<RecyclerView> {

    int layoutId;

    public StickHeaderRecyclerViewFragment() { }

    public StickHeaderRecyclerViewFragment(int layoutId) { this.layoutId = layoutId; }

    @Override
    public RecyclerView createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = layoutId > 0 ? (RecyclerView) inflater.inflate(layoutId, container, false) : null;
        if (recyclerView == null) {
            recyclerView = new RecyclerView(getContext());
        }
        return recyclerView;
    }
}
