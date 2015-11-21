package com.library.scroll;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by sj on 15/11/20.
 */
public abstract class ScrollFragment<T extends View> extends ScrollHolderFragment {

    private static final int NO_SCROLL_X = 0;

    private int mPosition;

    private int mScrollY;

    View placeHolderView;

    public void updatePlaceHolderViewHight(int height){
        if(mView instanceof RecyclerView){
            if(((RecyclerView) mView).getAdapter() != null){
                placeHolderView = ((RecyclerWithHeaderAdapter)(((RecyclerView) mView).getAdapter())).getPlaceHolderView();
            }
        }

        if(placeHolderView != null){
            ViewGroup.LayoutParams params = placeHolderView.getLayoutParams();
            params.height = height;
            placeHolderView.setLayoutParams(params);
        }
    }

    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    private T mView;

    public T getScrollView() {
        return mView;
    }

    public final void setPosition(int position) {
        this.mPosition = position;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = createView(inflater, container, savedInstanceState);

        if (mView == null) {
            throw new IllegalStateException("ScrollFragment onCreateView error");
        }

        if (mView instanceof ListView) {
            placeHolderView =  new View(getContext());
            ((ListView) mView).addHeaderView(placeHolderView);

            ((ListView) mView).setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (mScrollTabHolder != null) {
                        mScrollTabHolder.onListViewScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
                    }
                }
            });
        } else if (mView instanceof ScrollView) {

            if(!(mView instanceof NotifyingListenerScrollView)){
                throw new IllegalStateException("ScrollView must extends NotifyingListenerScrollView");
            }

            placeHolderView = new View(getContext());

            View contentView = ((ScrollView) mView).getChildAt(0);
            ((ScrollView) mView).removeView(contentView);

            LinearLayout childLayout = new LinearLayout(getContext());
            childLayout.setOrientation(LinearLayout.VERTICAL);
            childLayout.addView(placeHolderView);
            childLayout.addView(contentView);

            ((ScrollView) mView).addView(childLayout);

            ((NotifyingListenerScrollView) mView).setOnScrollChangedListener(new NotifyingListenerScrollView.OnScrollChangedListener() {
                @Override
                public void onScrollChanged(ScrollView v, int l, int t, int oldl, int oldt) {
                    if (mScrollTabHolder != null) {
                        mScrollTabHolder.onScrollViewScroll(v, l, t, oldl, oldt, mPosition);
                    }
                }
            });
        } else if (mView instanceof RecyclerView) {

            ((RecyclerView) mView).addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    mScrollY += dy;

                    if (mScrollTabHolder != null) {
                        mScrollTabHolder.onRecyclerViewScroll(recyclerView, mScrollY, mPosition);
                    }
                }
            });
        }

        bindData();
        return mView;
    }

    public abstract T createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void bindData();

    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) {
        if (mView == null) return;

        if (mView instanceof ListView) {
            if (scrollHeight == 0 && ((ListView) mView).getFirstVisiblePosition() >= 1) {
                return;
            }
            ((ListView) mView).setSelectionFromTop(1, scrollHeight);
        } else if (mView instanceof ScrollView) {
            ((ScrollView) mView).scrollTo(NO_SCROLL_X, headerHeight - scrollHeight);
        } else if (mView instanceof RecyclerView) {
            mScrollY = headerHeight - scrollHeight;
            if(((RecyclerView) mView).getLayoutManager() != null){
                if(((RecyclerView) mView).getLayoutManager() instanceof LinearLayoutManager){
                    ((LinearLayoutManager)((RecyclerView) mView).getLayoutManager()).scrollToPositionWithOffset(0, -mScrollY);
                } else if(((RecyclerView) mView).getLayoutManager() instanceof GridLayoutManager){
                    ((GridLayoutManager)((RecyclerView) mView).getLayoutManager()).scrollToPositionWithOffset(0, -mScrollY);
                }
            }
        }
    }
}
