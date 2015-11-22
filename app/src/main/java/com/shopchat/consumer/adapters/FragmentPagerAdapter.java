package com.shopchat.consumer.adapters;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is a port of the FragmentPagerAdapter from the v4 support library
 * that does not use support fragments. It requires the use of a childFragmentManager
 * if the adapter itself is used inside of a fragment. If used inside of a fragment on an
 * Api before v17 (when childFragmentManager was introduced), set {@code reuseFragments} to false.
 */
public abstract class FragmentPagerAdapter extends PagerAdapter {
    private static final String TAG = "FragmentPagerAdapter";

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private boolean reuseFragments = true;

    public FragmentPagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = null;
        if (shouldReuseFragments()) {
            fragment = mFragmentManager.findFragmentByTag(name);
        }

        if (fragment != null) {
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
        }

        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            compatSetUserVisibleHint(fragment, false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        if (shouldReuseFragments()) {
            mCurTransaction.detach((Fragment) object);
        } else {
            mCurTransaction.remove((Fragment) object);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                compatSetUserVisibleHint(mCurrentPrimaryItem, false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                compatSetUserVisibleHint(fragment, true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public long getItemId(int position) {
        return position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    public final void setReuseFragments(boolean reuse) {
        this.reuseFragments = reuse;
    }

    public final boolean shouldReuseFragments() {
        return this.reuseFragments;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void compatSetUserVisibleHint(Fragment fragment, boolean value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            fragment.setUserVisibleHint(value);
        }
    }
}
