package com.shopchat.consumer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.shopchat.consumer.R;


public class SlidingTabStrip extends LinearLayout {
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 4;
    private static final int TAB_SELECTED_INDICATOR_COLOR_DEFAULT = R.color.red;

    private final int mSelectedIndicatorThickness;
    private final Paint mSelectedIndicatorPaint;

    private int mSelectedPosition;
    private float mSelectionOffset;

    SlidingTabStrip(Context context) {
        this(context, null);
    }

    SlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        final float density = getResources().getDisplayMetrics().density;
        mSelectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);

        int mSelectedIndicatorColor = getResources().getColor(TAB_SELECTED_INDICATOR_COLOR_DEFAULT);

        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);
            mSelectedIndicatorColor = typedArray.getColor(R.styleable.SlidingTabLayout_selected_tab_indicator_color, mSelectedIndicatorColor);
            typedArray.recycle();
        }

        mSelectedIndicatorPaint = new Paint();
        mSelectedIndicatorPaint.setColor(mSelectedIndicatorColor);
    }

    /**
     * Specifies where we need to draw at.
     *
     * @param position       This is the tab farthest left.
     * @param positionOffset This is how close the underline should be drawn to its final destination.
     */
    public void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();

        if (childCount > 0) {
            final View selectedTab = getChildAt(mSelectedPosition);
            int left = selectedTab.getLeft();
            int right = selectedTab.getRight();

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }

            canvas.drawRect(left, height - mSelectedIndicatorThickness, right,
                    height, mSelectedIndicatorPaint);
        }
    }
}