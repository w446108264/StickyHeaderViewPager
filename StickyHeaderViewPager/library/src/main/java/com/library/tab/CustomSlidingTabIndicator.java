package com.library.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.R;

import java.util.Locale;

public class CustomSlidingTabIndicator extends HorizontalScrollView {

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    private static final int[] ATTRS = new int[]{android.R.attr.textSize, android.R.attr.textColor};

    private LinearLayout mTabsContainer;
    private ViewPager mViewPager;
    private LinearLayout.LayoutParams mDefaultTabLayoutParams;
    private LinearLayout.LayoutParams mExpandedTabLayoutParams;

    private ViewPager.OnPageChangeListener mListener;

    private int mTabCount;

    private int mCurrentPosition = 0;
    private float mCurrentPositionOffset = 0.f;

    private Paint mRectPaint;
    private Paint mDividerPaint;

    private int mScrollOffset = 52;
    private int mIndicatorHeight = 8;
    private int mUnderlineHeight = 2;
    private int mDividerPadding = 12;
    private int mTabSidePadding = 24;
    private int mTabTopBtmPadding = 0;
    private int mDividerWidth = 1;

    private int mTabTextSize = 12;
    private int mTabTextColor = 0xFF666666;
    private Typeface mTabTypeface = null;
    private int mTabTypefaceStyle = Typeface.BOLD;

    private int mIndicatorColor = 0xFF666666;
    private int mUnderlineColor = 0x1A000000;
    private int mDividerColor = 0x1A000000;

    @DrawableRes
    private int mTabBackgroundResId = R.drawable.slidingtab_bg;

    private boolean mShouldExpand = false;
    private boolean mTextAllCap = false;

    private int mLastScrollX = 0;

    public CustomSlidingTabIndicator(Context ctx) {
        this(ctx, null);
    }

    public CustomSlidingTabIndicator(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }

    public CustomSlidingTabIndicator(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);

        //Request HorizontalScrollView to stretch its content to fill the viewport
        setFillViewport(true);

        //This view will not do any drawing on its own, clear this flag if you override onDraw()
        setWillNotDraw(false);

        //Layout to hold all the tabs
        mTabsContainer = new LinearLayout(ctx);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        //Add the container to HorizontalScrollView as its only child view
        addView(mTabsContainer);

        //Convert the dimensions to DP
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset, dm);
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
        mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight, dm);
        mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);
        mTabSidePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabSidePadding, dm);
        mTabTopBtmPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabTopBtmPadding, dm);
        mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, dm);
        mTabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTabTextSize, dm);

        //Get system attrs (android:textSize & android:textColor)
        TypedArray typedArray = ctx.obtainStyledAttributes(attrs, ATTRS);

        mTabTextSize = typedArray.getDimensionPixelSize(0, mTabTextSize);
        mTabTextColor = typedArray.getColor(1, mTabTextColor);

        typedArray.recycle();

        //Get custom attrs
        typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.CustomSlidingTabIndicator);

        mIndicatorColor = typedArray.getColor(R.styleable.CustomSlidingTabIndicator_STIindicatorColor, mIndicatorColor);
        mUnderlineColor = typedArray.getColor(R.styleable.CustomSlidingTabIndicator_STIunderlineColor, mUnderlineColor);
        mDividerColor = typedArray.getColor(R.styleable.CustomSlidingTabIndicator_STIdividerColor, mDividerColor);
        mTabTextColor = typedArray.getColor(R.styleable.CustomSlidingTabIndicator_STItextColor, mTabTextColor);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STIindicatorHeight, mIndicatorHeight);
        mUnderlineHeight = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STIunderlineHeight, mUnderlineHeight);
        mDividerPadding = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STIdividersPadding, mDividerPadding);
        mTabSidePadding = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STItabLeftRightPadding, mTabSidePadding);
        mScrollOffset = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STIscrollOffSet, mScrollOffset);
        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STItabTextSize, mTabTextSize);
        mTabBackgroundResId = typedArray.getResourceId(R.styleable.CustomSlidingTabIndicator_STItabBackground, mTabBackgroundResId);
        mShouldExpand = typedArray.getBoolean(R.styleable.CustomSlidingTabIndicator_STIshouldExpand, mShouldExpand);
        mTextAllCap = typedArray.getBoolean(R.styleable.CustomSlidingTabIndicator_STItextCaps, mTextAllCap);
        mTabTopBtmPadding = typedArray.getDimensionPixelSize(R.styleable.CustomSlidingTabIndicator_STItabTopBtmPadding, mTabTopBtmPadding);

        typedArray.recycle();

        //Paint to draw the rectangle box
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        //Paint to draw the divider
        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerWidth);

        //Default: width = wrap_content, height = match_parent
        mDefaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        //Expanded: width = 0, height = match_parent, weight = 1.0f
        mExpandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    public void setViewPager(ViewPager pager) {
        mViewPager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have an adapter instance");
        }
        pager.setOnPageChangeListener(new PageListener());

        notifyDataSetChange();
    }

    public void setPageListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    /**
     * Reload Tab View
     */
    public void notifyDataSetChange() {
        // Remove all the views within the container
        mTabsContainer.removeAllViews();

        mTabCount = mViewPager.getAdapter().getCount();

        for (int i = 0; i < mTabCount; ++i) {

            if (mViewPager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) mViewPager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
            }
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                mCurrentPosition = mViewPager.getCurrentItem();
                scrollToChild(mCurrentPosition, 0);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Check if view is currently in edit mode
        if (isInEditMode() || mTabCount == 0) return;

        //Get the height of this view
        final int height = getHeight();

        //Draw UnderLine
        mRectPaint.setColor(mUnderlineColor);
        canvas.drawRect(0, height - mUnderlineHeight, mTabsContainer.getWidth(), height, mRectPaint);

        //Draw indicator line
        mRectPaint.setColor(mIndicatorColor);

        //Default: draw line below current tab
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);

        //Get left and right position of tab relative to its parent
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        //If there is an offset, start interpolating left and right coordinates between current and next tab
        if (mCurrentPositionOffset > 0f && mCurrentPosition < mTabCount - 1) {
            View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (mCurrentPositionOffset * nextTabLeft + (1f - mCurrentPositionOffset) * lineLeft);
            lineRight = (mCurrentPositionOffset * nextTabRight + (1f - mCurrentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, height - mIndicatorHeight, lineRight, height, mRectPaint);

        //draw divider
        mDividerPaint.setColor(mDividerColor);
        for (int i = 0; i < mTabCount - 1; ++i) {
            View tab = mTabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), height - mDividerPadding, mDividerPaint);
        }
    }

    private void updateTabStyles() {
        //Custom view needs no further styling here, all done in xml
        if (mTabBackgroundResId != R.drawable.slidingtab_bg) {
            return;
        }

        for (int i = 0; i < mTabCount; ++i) {
            View view = mTabsContainer.getChildAt(i);

            view.setBackgroundResource(mTabBackgroundResId);

            if (view instanceof TextView) {
                TextView tab = (TextView) view;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
                tab.setTypeface(mTabTypeface, mTabTypefaceStyle);
                tab.setTextColor(mTabTextColor);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (mTextAllCap) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(Locale.ENGLISH));
                    }
                }
            }
        }
    }

    private void addTextTab(final int position, String title) {

        if (mTabBackgroundResId != R.drawable.slidingtab_bg) {
            View view = LayoutInflater.from(getContext()).inflate(mTabBackgroundResId, null);

            if (view instanceof ViewGroup) {
                int numberOfChild = ((ViewGroup) view).getChildCount();

                if (numberOfChild > 0) {
                    // TextView must always be the first child
                    TextView titleView = (TextView) ((ViewGroup) view).getChildAt(0);
                    titleView.setText(title);
                    addTab(position, view);
                }
            } else if (view instanceof TextView) {
                TextView titleTextView = (TextView) view;
                titleTextView.setText(title);
                addTab(position, titleTextView);
            }

        } else {
            TextView tab = new TextView(getContext());
            tab.setText(title);
            tab.setGravity(Gravity.CENTER);
            tab.setSingleLine();

            addTab(position, tab);
        }
    }

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);

        addTab(position, tab);
    }

    private void addTab(final int position, final View tab) {
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });

        tab.setPadding(mTabSidePadding, mTabTopBtmPadding, mTabSidePadding, mTabTopBtmPadding);
        mTabsContainer.addView(tab, position, mShouldExpand ? mExpandedTabLayoutParams : mDefaultTabLayoutParams);
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount == 0) return;

        //The new x position of the indicator when scrolling occurs
        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= mScrollOffset;
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            //Move the horizontal scroll view
            //Y coordinate is zero
            scrollTo(newScrollX, 0);
        }

        if (offset == 0) {
            setTextColorChange(mCurrentPosition);
        }
    }

    private void setTextColorChange(int position) {
        int childCount = mTabsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mTabsContainer.getChildAt(i).setActivated(false);
        }
        mTabsContainer.getChildAt(position).setActivated(true);
//        if (position > 0)             mTabsContainer.getChildAt(position - 1).setActivated(false);
//        if (position < mTabCount - 1) mTabsContainer.getChildAt(position + 1).setActivated(false);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        /**
         * @param position
         * @param positionOffset       The amount moved by the scroll, 0.2 means an offset of
         *                             2% of the tab width from the left to the right
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));

            //Redraw the indicator constantly as the scroll moves
            //Invalidate() will trigger a onDraw()
            invalidate();

            if (mListener != null) {
                mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mViewPager.getCurrentItem(), 0);
            }

            if (mListener != null) {
                mListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mListener != null) {
                mListener.onPageSelected(position);
            }
        }
    }

    public LinearLayout getTabsContainer() {
        return mTabsContainer;
    }

    public View getViewOfPosition(int position) {
        View view = null;
        if (mTabsContainer.getChildCount() > position) {
            view = mTabsContainer.getChildAt(position);
        }
        return view;
    }

    /**
     * Storing and Restoring the state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.currentPosition;

        //Request a re-measure and redraw
        requestLayout();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = mCurrentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}


