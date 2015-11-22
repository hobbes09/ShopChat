package com.shopchat.consumer.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.shopchat.consumer.R;
import com.shopchat.consumer.views.ActionBarHome;

/**
 * Created by Sudipta on 8/7/2015.
 */
public class BaseActivity extends ActionBarActivity {

    private ActionBarHome actionBarHome;
    private ActionBar actionBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Configure Action Bar
     *
     * @param isHome
     * @param showHomeAsUp
     * @param title
     */
    protected void setUpSupportActionBar(boolean isHome, boolean showHomeAsUp, String title) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null == toolbar) {
            throw new RuntimeException("ActionBar cannot be setup if there is no Toolbar matching 'R.id.toolbar'.");
        }

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (isHome) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBarHome = new ActionBarHome(this);
            actionBar.setCustomView(actionBarHome);
            toolbar.setContentInsetsAbsolute(0, 0);
            return;
        }

        if (showHomeAsUp) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        boolean showTitle = !TextUtils.isEmpty(title);
        actionBar.setDisplayShowTitleEnabled(showTitle);

        if (showTitle) {
            actionBar.setTitle(title);
        }
    }

    /**
     * Change the action bar menu icon drawable.
     *
     * @param resourceId
     */
    public void changeActionBarMultipleMenuIcon(int resourceId) {
        if (actionBarHome != null) {
            actionBarHome.changeMultipleActionMenuIcon(resourceId);
        }
    }

    protected ActionBarHome getActionBarHome() {
        return this.actionBarHome;
    }

    protected void hideHamburgerIcon() {
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Change the hamburger menu icon drawable
     *
     * @param resId
     */
    protected void changeHamburgerIcon(int resId){
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(resId);
    }
}
