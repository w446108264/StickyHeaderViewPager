package com.library;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.library.scroll.ScrollHolder;
import com.library.scroll.ScrollHolderFragment;

/**
 * Created by sj on 15/11/20.
 */
public abstract class StickHeaderViewPagerAdapter extends FragmentPagerAdapter {

    private SparseArrayCompat<ScrollHolder> mScrollTabHolders;
    private StickHeaderViewPager mStickHeaderViewPager;

    public StickHeaderViewPagerAdapter(FragmentManager fm, StickHeaderViewPager stickHeaderViewPager) {
        super(fm);
        mStickHeaderViewPager = stickHeaderViewPager;
        mScrollTabHolders = new SparseArrayCompat<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        mScrollTabHolders.put(position, (ScrollHolder) object);
        ((ScrollHolderFragment)object).bindScrollTabHolder(mStickHeaderViewPager);
        return object;
    }

    public SparseArrayCompat<ScrollHolder> getScrollTabHolders() {
        return mScrollTabHolders;
    }
}
