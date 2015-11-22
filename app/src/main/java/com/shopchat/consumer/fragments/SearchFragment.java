package com.shopchat.consumer.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.activities.RetailerListActivity;
import com.shopchat.consumer.adapters.ProductListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 8/31/2015.
 */
public class SearchFragment extends Fragment {

    private CancelClickListener cancelClickListener;

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();

        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, null);

        final ListView lvItemList = (ListView) rootView.findViewById(R.id.lv_item);

        List list = new ArrayList();
        list.add("Apple");
        list.add("Boy");
        list.add("Cat");
        list.add("Dog");
        list.add("Elephant");
        list.add("Fan");
        list.add("Goat");
        list.add("Horse");


        ProductListAdapter listAdapter = new ProductListAdapter(getActivity(), R.layout.list_row_product, list);
        lvItemList.setAdapter(listAdapter);

        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), RetailerListActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                this.cancelClickListener.onCancelClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCancelClickListener(CancelClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }

    public interface CancelClickListener {
        public void onCancelClick();
    }
}
