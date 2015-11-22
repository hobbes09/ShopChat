package com.shopchat.consumer.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.shopchat.consumer.R;
import com.shopchat.consumer.fragments.ProductListFragment;
import com.shopchat.consumer.fragments.SearchFragment;
import com.shopchat.consumer.models.CategoryModel;

/**
 * Created by Sudipta on 8/9/2015.
 */
public class ProductListActivity extends BaseActivity {

    public static final String CATEGORY = "category";
    private FragmentManager fragmentManager;
    private CategoryModel categoryModel;


    public static Intent getIntent(Context context, CategoryModel categoryModel) {
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtra(CATEGORY, categoryModel);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        categoryModel = this.getIntent().getParcelableExtra(CATEGORY);
        setUpSupportActionBar(false, true, categoryModel.getItemName());

        initViews();
    }

    private void initViews() {

        fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = ProductListFragment.getInstance(categoryModel);
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                replaceWithSearchFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceWithSearchFragment() {

        Fragment searchFragment = new SearchFragment().newInstance();
        ((SearchFragment) searchFragment).setCancelClickListener(cancelClickListener);
        fragmentManager.beginTransaction()
                .replace(R.id.container, searchFragment)
                .commit();
    }

    private SearchFragment.CancelClickListener cancelClickListener = new SearchFragment.CancelClickListener() {
        @Override
        public void onCancelClick() {
            Fragment itemListFragment = new ProductListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, itemListFragment)
                    .commit();
        }
    };

}
