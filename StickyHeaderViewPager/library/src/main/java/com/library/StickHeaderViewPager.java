package com.library;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.library.scroll.ScrollFragment;
import com.library.scroll.ScrollHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sj on 15/11/20.
 */
public class StickHeaderViewPager extends StickHeaderLayout implements ScrollHolder, ViewPager.OnPageChangeListener {

    private static final int ID_VIEWPAGER = 1;
    private Context mContext;

    protected LinearLayout mStickheader;
    protected ViewPager mViewPager;

    private int mStickViewHeight;
    private int mStickHeaderHeight;
    private int mMinHeaderTranslation;

    private ViewPagerAdapter mAdapter;

    private List<ScrollFragment> mScrollFragmentList = new ArrayList<>();

    public StickHeaderViewPager(Context context) {
        this(context, null);
    }

    public StickHeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // add viewpager
        mViewPager = new ViewPager(context);
        mViewPager.setId(ID_VIEWPAGER);
        addView(mViewPager, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // add stickheader
        mStickheader = new LinearLayout(context);
        mStickheader.setOrientation(LinearLayout.VERTICAL);
        addView(mStickheader, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() < 2) {
            super.addView(child, index, params);
        } else {
            if (mStickheader.getChildCount() > 2) {
                throw new IllegalStateException("only can host 2 elements");
            }
            mStickheader.addView(child, params);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mStickheader.getChildCount() < 2) {
            throw new IllegalStateException("stickHeader must have 2 elements");
        }
        initStickHeaderViewHight();
    }

    private void initStickHeaderViewHight() {
        final ViewTreeObserver vto = mStickheader.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mStickHeaderHeight = mStickheader.getMeasuredHeight();
                mStickViewHeight = mStickheader.getChildAt(1).getMeasuredHeight();
                if (mStickHeaderHeight > 0 && mStickViewHeight > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mStickheader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mStickheader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    updateChildViewHight();
                }
            }
        });
    }

    private void updateChildViewHight() {
        if (mStickHeaderHeight != 0 && mStickViewHeight != 0) {
            mMinHeaderTranslation = -mStickHeaderHeight + mStickViewHeight;
            if (mScrollFragmentList != null) {
                for (ScrollFragment scrollFragment : mScrollFragmentList) {
                    scrollFragment.updatePlaceHolderViewHight(mStickHeaderHeight);
                }
            }
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int currentItem = mViewPager.getCurrentItem();
        if (positionOffsetPixels > 0) {
            SparseArrayCompat<ScrollHolder> scrollTabHolders = mAdapter.getScrollTabHolders();

            ScrollHolder fragmentContent;
            if (position < currentItem) {
                fragmentContent = scrollTabHolders.valueAt(position);
            } else {
                fragmentContent = scrollTabHolders.valueAt(position + 1);
            }

            fragmentContent.adjustScroll((int) (mStickheader.getHeight() + mStickheader.getTranslationY()), mStickheader.getHeight());
        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollHolder> scrollTabHolders = mAdapter.getScrollTabHolders();

        if (scrollTabHolders == null || scrollTabHolders.size() != mAdapter.getCount()) {
            return;
        }

        ScrollHolder currentHolder = scrollTabHolders.valueAt(position);
        currentHolder.adjustScroll((int) (mStickheader.getHeight() + mStickheader.getTranslationY()), mStickheader.getHeight());
    }

    @Override
    public void onPageScrollStateChanged(int state) { }


    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) { }

    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            scrollHeader(getScrollY(view));
        }
    }

    @Override
    public void onScrollViewScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            scrollHeader(view.getScrollY());
        }
    }

    @Override
    public void onRecyclerViewScroll(RecyclerView view, int scrollY, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            scrollHeader(scrollY);
        }
    }

    private void scrollHeader(int scrollY) {
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mStickheader.setTranslationY(translationY);
    }

    private int getScrollY(AbsListView view) {
        View child = view.getChildAt(0);
        if (child == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = child.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mStickHeaderHeight;
        }

        return -top + firstVisiblePosition * child.getHeight() + headerHeight;
    }

    private class ViewPagerAdapter extends StickHeaderViewPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm, StickHeaderViewPager stickHeaderViewPager) {
            super(fm, stickHeaderViewPager);
        }

        @Override
        public Fragment getItem(int position) {
            return mScrollFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mScrollFragmentList == null ? 0 : mScrollFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((ScrollFragment) getItem(position)).getTitle();
        }
    }

    private void update(StickHeaderViewPagerBuilder buider) {
        if (buider == null) {
            return;
        }
        if (buider.fm == null) {
            throw new IllegalStateException("FragmentManager is null");
        }
        if (buider.scrollFragmentList == null) {
            throw new IllegalStateException("At least one scrollFragment");
        }
        mAdapter = new ViewPagerAdapter(buider.fm, this);
        mScrollFragmentList = buider.scrollFragmentList;
        mViewPager.setAdapter(mAdapter);
    }

    public static class StickHeaderViewPagerBuilder {

        private StickHeaderViewPager stickHeaderViewPager;
        private List<ScrollFragment> scrollFragmentList;
        private FragmentManager fm;

        protected StickHeaderViewPagerBuilder(final StickHeaderViewPager stickHeaderViewPager) {
            this.stickHeaderViewPager = stickHeaderViewPager;
        }

        public StickHeaderViewPagerBuilder setFragmentManager(FragmentManager fm) {
            this.fm = fm;
            return this;
        }

        public void notifyData() {
            stickHeaderViewPager.update(this);
        }

        public StickHeaderViewPagerBuilder addScrollFragments(ScrollFragment... fragments) {
            if (fragments == null || fragments.length == 0) {
                throw new IllegalStateException("can't add a null fragment");
            }
            if (scrollFragmentList == null) {
                scrollFragmentList = new ArrayList<>();
            }
            for (ScrollFragment scrollFragment : fragments) {
                scrollFragment.setPosition(scrollFragmentList.size());
                scrollFragmentList.add(scrollFragment);
            }
            return this;
        }

        public static StickHeaderViewPagerBuilder stickTo(final StickHeaderViewPager stickHeaderViewPager) {
            return new StickHeaderViewPagerBuilder(stickHeaderViewPager);
        }
    }
}
