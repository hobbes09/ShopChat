package com.shopchat.consumer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.activities.RetailerListActivity;
import com.shopchat.consumer.adapters.ProductListAdapter;
import com.shopchat.consumer.listener.RetailerListener;
import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.task.RetailerTask;
import com.shopchat.consumer.utils.SearchManager;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.CustomProgress;

import java.util.List;

/**
 * Created by Sudipta on 8/31/2015.
 */
public class ProductListFragment extends Fragment {

    public static final String CATEGORY = "category";
    private List productList;
    private ProductListAdapter listAdapter;
    private ListView lvItemList;
    private CategoryModel categoryModel;
    private ProductModel productModel;
    private CustomProgress progressDialog;

    public static Fragment getInstance(CategoryModel categoryModel) {
        Fragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CATEGORY, categoryModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryModel = (CategoryModel) getArguments().get(CATEGORY);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_item_list, null);

        lvItemList = (ListView) rootView.findViewById(R.id.lv_item);
        final EditText searchEditText = (EditText) rootView.findViewById(R.id.edtText_search);
        searchEditText.addTextChangedListener(textWatcher);

        productList = categoryModel.getProductModels();
       /* list.add(new ProductModel("Apple"));
        list.add(new ProductModel("Boy"));
        list.add(new ProductModel("Cat"));
        list.add(new ProductModel("Dog"));
        list.add(new ProductModel("Elephant"));
        list.add(new ProductModel("Fan"));
        list.add(new ProductModel("Goat"));
        list.add(new ProductModel("Horse"));*/


        listAdapter = new ProductListAdapter(getActivity(), R.layout.list_row_product, productList);
        lvItemList.setAdapter(listAdapter);

        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productModel = (ProductModel) parent.getAdapter().getItem(position);
                initRetailerFetch();
            }
        });
        return rootView;
    }

    private void initRetailerFetch() {
        progressDialog = Utils.getProgressDialog(getActivity());
        RetailerListener retailerListener = new RetailerListener() {
            @Override
            public void onRetailerFetchStart() {
                progressDialog.show();
            }

            @Override
            public void onRetailerFetchSuccess(List<RetailerModel> retailerModels) {
                progressDialog.dismiss();
                if (!retailerModels.isEmpty()) {
                    productModel.setRetailerModels(retailerModels);
                    getActivity().startActivity(RetailerListActivity.getIntent(getActivity(), productModel));
                } else {

                    Utils.showGenericDialog(getActivity(), R.string.no_data);
                }
            }

            @Override
            public void onRetailerFetchFailure(ErrorModel errorModel) {
                progressDialog.dismiss();

                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(getActivity(), errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(getActivity(), errorModel.getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        };

        RetailerTask retailerTask = new RetailerTask(getActivity(), retailerListener, productModel);
        retailerTask.fetchRetailer();
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
            SearchManager searchManager = new SearchManager(productList, s.toString(), SearchManager.SearchableEnum.ITEM);
            searchManager.setSearchListener(searchListener);
            searchManager.initSearch();
        }
    };

    SearchManager.SearchListener searchListener = new SearchManager.SearchListener() {
        @Override
        public void onSearchSuccess(List<Object> searchedList) {
            listAdapter = new ProductListAdapter(getActivity(), R.layout.list_row_product, searchedList);
            lvItemList.setAdapter(listAdapter);
            lvItemList.invalidate();

        }

        @Override
        public void onSearchFail() {
            // TODO Localization
            //Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSearchStringEmpty() {
            listAdapter = new ProductListAdapter(getActivity(), R.layout.list_row_product, productList);
            lvItemList.setAdapter(listAdapter);
            lvItemList.invalidate();
        }
    };
}
