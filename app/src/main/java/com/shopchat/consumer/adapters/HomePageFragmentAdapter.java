package com.shopchat.consumer.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Pair;


import java.util.ArrayList;
import java.util.List;

public class HomePageFragmentAdapter extends FragmentPagerAdapter {

    private List<Pair<String, Fragment>> fragments = new ArrayList<>();

    public HomePageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).second;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(String title, Fragment fragment) {
        this.fragments.add(new Pair<>(title, fragment));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).first;
    }
}
