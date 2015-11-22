package com.shopchat.consumer.views;

import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopchat.consumer.R;

/**
 * Created by Sudipta on 8/8/2015.
 */
public class ActionBarHome extends FrameLayout {
    private Context context;
    private TextView tvSelectedLocation;
    private TextView tvSelectedState;
    private ImageView ivActionMenuMultiple;
    public OnActionBarItemClickListener listener;
    private Fragment visibleFragment;
    private TextView tvSave;

    public ActionBarHome(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.action_bar_home, null);
        addView(view);

        tvSelectedLocation = (TextView) view.findViewById(R.id.tv_selected_location);
        tvSelectedState = (TextView) view.findViewById(R.id.tv_selected_state);
        ivActionMenuMultiple = (ImageView) view.findViewById(R.id.iv_action_multiple);
        tvSave = (TextView) view.findViewById(R.id.tv_save);


        final ImageView ivMenuIcon = (ImageView) view.findViewById(R.id.iv_menu);
        ivMenuIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedLocationListener().onMenuIconClick();
            }
        });

        tvSelectedLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedLocationListener().onSelectedLocationClick();
            }
        });

        ivActionMenuMultiple.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedLocationListener().onMultipleActionMenuClick();
            }
        });

        tvSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedLocationListener().onMultipleActionMenuClick();
            }
        });

    }

    public void setOnActionBarItemClickListener(OnActionBarItemClickListener listener) {
        this.listener = listener;
    }

    public OnActionBarItemClickListener getSelectedLocationListener() {
        OnActionBarItemClickListener listener = (OnActionBarItemClickListener) context;
        return listener;
    }

    public interface OnActionBarItemClickListener {
        public void onSelectedLocationClick();

        public void onMenuIconClick();

        public void onMultipleActionMenuClick();
    }

    public String getSelectedLocation() {
        return tvSelectedLocation.getText().toString();
    }

    public void setSelectedLocation(String selectedLocation) {
        this.tvSelectedLocation.setText(selectedLocation);
    }

    public String getSelectedState() {
        return tvSelectedState.getText().toString();
    }

    public void setSelectedState(String selectedState) {
        this.tvSelectedState.setText(selectedState);
    }

    /**
     * Change the action bar menu icon drawable
     *
     * @param resourceId
     */
    public void changeMultipleActionMenuIcon(int resourceId) {
        ivActionMenuMultiple.setImageResource(resourceId);
        tvSave.setVisibility(View.GONE);
    }

    public void showSaveMenu() {
        ivActionMenuMultiple.setVisibility(View.GONE);
        tvSave.setVisibility(View.VISIBLE);
    }

    public void showActionBarMultipleMenuIcon() {
        ivActionMenuMultiple.setVisibility(View.VISIBLE);
    }

    public void hideActionBarMultipleMenuIcon() {
        ivActionMenuMultiple.setVisibility(View.GONE);
    }

    public Fragment getVisibleFragment() {
        return visibleFragment;
    }

    public void setVisibleFragment(Fragment visibleFragment) {
        this.visibleFragment = visibleFragment;
    }
}
