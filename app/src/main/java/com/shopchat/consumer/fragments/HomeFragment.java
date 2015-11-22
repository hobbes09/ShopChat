package com.shopchat.consumer.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.activities.ProductListActivity;
import com.shopchat.consumer.adapters.CategoryListAdapter;
import com.shopchat.consumer.listener.ProductListener;
import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.task.ProductTask;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.CustomProgress;

import java.util.List;

/**
 * Created by Sudipta on 8/9/2015.
 */
public class HomeFragment extends Fragment {

    private CustomProgress progressDialog;
    private int preLast;
    private boolean isLoading = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);

        final ListView lvHomeList = (ListView) rootView.findViewById(R.id.lv_home_list);
        lvHomeList.addHeaderView(getHeaderView());

        List<CategoryModel> list = Utils.getShopChatApplication(getActivity()).getCategoryModelList();
       /* list.add(new CategoryModel("Electrical Shop", R.drawable.electrical_shop));
        list.add(new CategoryModel("Electronics Repair", R.drawable.electronics_repair));
        list.add(new CategoryModel("Grocery", R.drawable.grocery));
        list.add(new CategoryModel("Home Appliance", R.drawable.home_appliance));
        list.add(new CategoryModel("Kitchen Appliance", R.drawable.kitchen_appliance));
        list.add(new CategoryModel("Laptop Shop", R.drawable.laptop_shop));
        list.add(new CategoryModel("Medicine", R.drawable.medicine));
        list.add(new CategoryModel("Mobile Shop", R.drawable.mobile_shop));*/

        CategoryListAdapter adapter = new CategoryListAdapter(getActivity(), R.layout.list_row_home, list);
        lvHomeList.setAdapter(adapter);

        lvHomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    CategoryModel categoryModel = (CategoryModel) parent.getAdapter().getItem(position);
                    initProductFetch(categoryModel);
                }
            }
        });



        return rootView;
    }

    private void initProductFetch(final CategoryModel categoryModel) {
        progressDialog = Utils.getProgressDialog(getActivity());
        ProductListener productListener = new ProductListener() {
            @Override
            public void onProductFetchStart() {
                progressDialog.show();
            }

            @Override
            public void onProductFetchSuccess(List<ProductModel> productModels) {
                progressDialog.dismiss();
                if (!productModels.isEmpty()) {
                    categoryModel.setProductModels(productModels);
                    getActivity().startActivity(ProductListActivity.getIntent(getActivity(), categoryModel));
                } else {
                    Utils.showGenericDialog(getActivity(), R.string.no_data);
                }

            }

            @Override
            public void onProductFetchFailure(ErrorModel errorModel) {
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

        ProductTask productionTask = new ProductTask(getActivity(), productListener, categoryModel);
        productionTask.fetchProduct();
    }

    private View getHeaderView() {
        View headerView = null;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflater.inflate(R.layout.list_home_header, null);

        return headerView;
    }
}
