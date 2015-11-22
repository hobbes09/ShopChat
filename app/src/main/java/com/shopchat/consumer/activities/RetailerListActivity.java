package com.shopchat.consumer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.adapters.RetailerListAdapter;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.utils.SearchManager;
import com.shopchat.consumer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 8/14/2015.
 */
public class RetailerListActivity extends BaseActivity {

    public static final String PRODUCT = "product";
    private List<RetailerModel> selectedRetailerList;
    private ListView lvRetailerList;
    private List retailerList;
    private ProductModel productModel;

    public static Intent getIntent(Context context, ProductModel productModel) {
        Intent intent = new Intent(context, RetailerListActivity.class);
        intent.putExtra(PRODUCT, productModel);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_list);

        productModel = this.getIntent().getParcelableExtra(PRODUCT);

        setUpSupportActionBar(false, true, "Retailer Name");

        ((ShopChatApplication) getApplication()).setRetailerList(new ArrayList<RetailerModel>());
        selectedRetailerList = ((ShopChatApplication) getApplication()).getRetailerList();
        initViews();
    }

    private void initViews() {

        final EditText searchEditText = (EditText) findViewById(R.id.edtText_search);
        searchEditText.addTextChangedListener(textWatcher);

        lvRetailerList = (ListView) findViewById(R.id.lv_retailer);

        retailerList = productModel.getRetailerModels();
       /* list.add(new RetailerModel("Apple"));
        list.add(new RetailerModel("Boy"));
        list.add(new RetailerModel("Cat"));
        list.add(new RetailerModel("Dog"));
        list.add(new RetailerModel("Elephant"));
        list.add(new RetailerModel("Fan"));
        list.add(new RetailerModel("Goat"));
        list.add(new RetailerModel("Horse"));
        list.add(new RetailerModel("Ink"));
        list.add(new RetailerModel("Jug"));
        list.add(new RetailerModel("Kite"));
        list.add(new RetailerModel("Lion"));
        list.add(new RetailerModel("Mango"));
        list.add(new RetailerModel("Net"));
        list.add(new RetailerModel("Orange"));*/


        RetailerListAdapter listAdapter = new RetailerListAdapter(this, R.layout.list_row_retailer, retailerList);
        lvRetailerList.setAdapter(listAdapter);

        lvRetailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_retailer);
                if (!checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    RetailerModel retailerModel = (RetailerModel) parent.getAdapter().getItem(position);
                    selectedRetailerList.add(retailerModel);

                } else {
                    checkBox.setChecked(false);
                    RetailerModel retailerModel = (RetailerModel) parent.getAdapter().getItem(position);
                    if (selectedRetailerList.contains(retailerModel)) {
                        selectedRetailerList.remove(retailerModel);
                    }
                }
            }
        });

        final Button btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedRetailerList.isEmpty()) {
                    startActivity(ChatBoardActivity.getIntent(RetailerListActivity.this,productModel));
                } else {
                    showChooseRetailerDialog();
                }
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            SearchManager searchManager = new SearchManager(retailerList, s.toString(), SearchManager.SearchableEnum.RETAILER);
            searchManager.setSearchListener(searchListener);
            searchManager.initSearch();
        }
    };

    SearchManager.SearchListener searchListener = new SearchManager.SearchListener() {
        @Override
        public void onSearchSuccess(List<Object> searchedList) {
            RetailerListAdapter listAdapter = new RetailerListAdapter(RetailerListActivity.this, R.layout.list_row_retailer, searchedList);
            lvRetailerList.setAdapter(listAdapter);
            lvRetailerList.invalidate();
        }

        @Override
        public void onSearchFail() {
            // TODO Localization
            //Toast.makeText(RetailerListActivity.this, "No data found!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSearchStringEmpty() {
            RetailerListAdapter listAdapter = new RetailerListAdapter(RetailerListActivity.this, R.layout.list_row_retailer, retailerList);
            lvRetailerList.setAdapter(listAdapter);
            lvRetailerList.invalidate();
        }
    };

    private void showChooseRetailerDialog() {
        Utils.showGenericDialog(this, R.string.choose_retailer);
    }
}
