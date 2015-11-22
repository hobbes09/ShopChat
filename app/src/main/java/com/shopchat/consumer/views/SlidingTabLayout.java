package com.shopchat.consumer.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopchat.consumer.R;

import java.util.LinkedList;
import java.util.List;

public class SlidingTabLayout extends HorizontalScrollView {
    private static final int TAB_LAYOUT_DEFAULT = R.layout.tab_layout;
    private static final int TAB_TEXTVIEW_DEFAULT = R.id.tv_tab_layout_title;
    private static final int TAB_IMAGEVIEW_DEFAULT = R.id.iv_tab_layout_image;
    private static final int TAB_NORMAL_TITLE_COLOR_DEFAULT = R.color.white;
    private static final int TAB_SELECTED_TITLE_COLOR_DEFAULT = R.color.red;
    private static final int TAB_NORMAL_CHAT_ICON = R.drawable.ic_chat_white;
    private static final int TAB_SELECTED_CHAT_ICON = R.drawable.ic_chat_yellow;
    private static final int TAB_NORMAL_HOME_ICON = R.drawable.ic_home_white;
    private static final int TAB_SELECTED_HOME_ICON = R.drawable.ic_home_yellow;
    private static final int TAB_NORMAL_PROFILE_ICON = R.drawable.ic_profile_white;
    private static final int TAB_SELECTED_PROFILE_ICON = R.drawable.ic_profile_yellow;
    private static final int CHAT = 0;
    private static final int HOME = 1;
    private static final int PROFILE = 2;
    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int mTabViewImageViewId;
    private int mTabNormalTitleColor;
    private int mTabSelectedTitleColor;

    private ViewPagerEventDelegate eventDelegate;

    private ViewPager mViewPager;

    private SlidingTabStrip mTabStrip;


    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        mTabViewLayoutId = TAB_LAYOUT_DEFAULT;
        mTabViewTextViewId = TAB_TEXTVIEW_DEFAULT;
        mTabViewImageViewId = TAB_IMAGEVIEW_DEFAULT;

        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);

            mTabNormalTitleColor = typedArray.getColor(R.styleable.SlidingTabLayout_normal_tab_title_color, getResources().getColor(TAB_NORMAL_TITLE_COLOR_DEFAULT));
            mTabSelectedTitleColor = typedArray.getColor(R.styleable.SlidingTabLayout_selected_tab_title_color, getResources().getColor(TAB_SELECTED_TITLE_COLOR_DEFAULT));

            typedArray.recycle();
        }

        mTabStrip = new SlidingTabStrip(context, attrs);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * Define a custom layout for a tab, and a textview to hold the tab title.
     *
     * @param layoutId   The resource id of the layout to inflate.
     * @param textViewId Resource id of the textview withing the layout to find.
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setTabViewLayoutId(final int layoutId, final int textViewId) {
        mTabViewLayoutId = layoutId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * The pager and its adapter dictate the number of tabs and their title.
     *
     * @param viewPager Dictates the contents to be displayed for each tab.
     */
    public void setViewPager(final ViewPager viewPager) {
        mTabStrip.removeAllViews();

        if (mViewPager != null) {
            throw new IllegalStateException("The ViewPager has already been defined.");
        }

        mViewPager = viewPager;
        if (viewPager != null) {
            eventDelegate = new ViewPagerEventDelegate();
            viewPager.setOnPageChangeListener(eventDelegate);
            eventDelegate.addPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    public void addPageChangeListener(ViewPager.OnPageChangeListener listener) {
        eventDelegate.addPageChangeListener(listener);
    }

    public void removePageChangeListener(ViewPager.OnPageChangeListener listener) {
        eventDelegate.removePageChangeListener(listener);
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            final View tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
            tabView.setOnClickListener(tabClickListener);

            final TextView tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            tabTitleView.setText(adapter.getPageTitle(i));
            tabTitleView.setVisibility(View.GONE);

            mTabStrip.setBackgroundColor(getResources().getColor(R.color.brainstorm));
            mTabStrip.addView(tabView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        final View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            scrollTo(targetScrollX, 0);
            highlightTabTitle(tabIndex);
        }
    }

    private void highlightTabTitle(int position) {

        for (int i = 0; i < mTabStrip.getChildCount(); ++i) {
            TextView title = (TextView) mTabStrip.getChildAt(i).findViewById(mTabViewTextViewId);
            ImageView tabImage = (ImageView) mTabStrip.getChildAt(i).findViewById(mTabViewImageViewId);
            //title.setTextColor(i == position ? mTabSelectedTitleColor : mTabNormalTitleColor);

            if (i == CHAT) {
                if (i == position) {
                    tabImage.setImageResource(TAB_SELECTED_CHAT_ICON);
                } else {
                    tabImage.setImageResource(TAB_NORMAL_CHAT_ICON);
                }
            } else if (i == HOME) {
                if (i == position) {
                    tabImage.setImageResource(TAB_SELECTED_HOME_ICON);
                } else {
                    tabImage.setImageResource(TAB_NORMAL_HOME_ICON);
                }
            } else if (i == PROFILE) {
                if (i == position) {
                    tabImage.setImageResource(TAB_SELECTED_PROFILE_ICON);
                } else {
                    tabImage.setImageResource(TAB_NORMAL_PROFILE_ICON);
                }
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            final int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            final View selectedTitle = mTabStrip.getChildAt(position);

            final int extraOffset;
            if (selectedTitle == null) {
                extraOffset = 0;
            } else {
                extraOffset = (int) (positionOffset * selectedTitle.getWidth());
            }

            scrollToTab(position, extraOffset);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    private class ViewPagerEventDelegate implements ViewPager.OnPageChangeListener {

        private List<ViewPager.OnPageChangeListener> pageChangeListeners;

        public ViewPagerEventDelegate() {
            pageChangeListeners = new LinkedList<>();
        }

        public void addPageChangeListener(ViewPager.OnPageChangeListener listener) {
            pageChangeListeners.add(listener);
        }

        public void removePageChangeListener(ViewPager.OnPageChangeListener listener) {
            pageChangeListeners.remove(listener);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            for (ViewPager.OnPageChangeListener listener : pageChangeListeners) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            for (ViewPager.OnPageChangeListener listener : pageChangeListeners) {
                listener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            for (ViewPager.OnPageChangeListener listener : pageChangeListeners) {
                listener.onPageScrollStateChanged(state);
            }
        }
    }
}
