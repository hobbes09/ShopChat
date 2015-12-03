//package com.shopchat.consumer.fragments;
//
//import android.app.Fragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.shopchat.consumer.R;
//import com.shopchat.consumer.activities.ChatBoardActivity;
//import com.shopchat.consumer.activities.LandingActivity;
//import com.shopchat.consumer.adapters.ChatListAdapter;
//import com.shopchat.consumer.models.ChatDisplayModel;
//import com.shopchat.consumer.models.ErrorModel;
//import com.shopchat.consumer.models.ProductModel;
//import com.shopchat.consumer.utils.Constants;
//import com.shopchat.consumer.utils.SearchManager;
//import com.shopchat.consumer.utils.Utils;
//import com.shopchat.consumer.views.CustomProgress;
//
//import java.util.List;
//
///**
// * Created by Sudipta on 8/9/2015.
// */
//public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
//
//    private SwipeRefreshLayout swipeRefreshLayout;
//    int yPos = 0;
//    int xPos = 0;
//    private ListView lvChat;
//    private LinearLayout llContent;
//    private FrameLayout flProgress;
//    private ProgressBar progressBar;
//    private TextView tvNoData;
//    private List chatDisplayModels;
//    private ProductModel productModel;
//    private ChatListAdapter chatAdapter;
//    private CustomProgress progressDialog;
//    private boolean isLoading = false;
//    private RelativeLayout flLoadMore;
//    private PageListener pageListener;
//    private ProgressBar loadMoreProgressBar;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ((LandingActivity) getActivity()).setChatAdapterListener(chatAdapterListener);
//        productModel = ((LandingActivity) getActivity()).getProductModel();
//        progressDialog = ((LandingActivity) getActivity()).getProgressDialog();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat, null);
//        //swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
//        lvChat = (ListView) rootView.findViewById(R.id.lv_chat);
//        llContent = (LinearLayout) rootView.findViewById(R.id.ll_content);
//        flProgress = (FrameLayout) rootView.findViewById(R.id.fl_progress);
//        tvNoData = (TextView) rootView.findViewById(R.id.tv_no_data);
//        flLoadMore = (RelativeLayout) rootView.findViewById(R.id.fl_load_more);
//        flLoadMore.setVisibility(View.GONE);
//
//        loadMoreProgressBar = (ProgressBar) rootView.findViewById(R.id.load_more_progress);
//        loadMoreProgressBar.setVisibility(View.GONE);
//
//        final EditText searchEditText = (EditText) rootView.findViewById(R.id.edtText_search);
//        searchEditText.addTextChangedListener(textWatcher);
//
//
//        chatDisplayModels = Utils.getShopChatApplication(getActivity()).getChatDisplayModels();
//
//
//        lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ChatDisplayModel chatDisplayModel = (ChatDisplayModel) parent.getAdapter().getItem(position);
//                //getActivity().startActivity(ChatBoardActivity.getIntent(getActivity(), chatDisplayModel.getProductId()));
//                startActivityForResult(ChatBoardActivity.getIntent(getActivity(), chatDisplayModel.getProductId(), chatDisplayModel.getConsumerChatContent()), 0);
//            }
//        });
//
//
//        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int previousFirst = -1;
//                if (lvChat.getAdapter() == null)
//                    return;
//
//                if (lvChat.getAdapter().getCount() == 0)
//                    return;
//                if (previousFirst != firstVisibleItem) {
//                    previousFirst = firstVisibleItem;
//
//                    Constants.SELECTED_POSITION = firstVisibleItem;
//                }
//                int l = visibleItemCount + firstVisibleItem;
//                if (l >= totalItemCount && !isLoading) {
//                    // It is time to add new data. We call the listener
//                    //isLoading = true;
//                    int totalPageNumber = Utils.getShopChatApplication(getActivity()).getNumberOfChatPage();
//                    int currentPageNumber = Utils.getShopChatApplication(getActivity()).getCurrentChatPageNumber() + 1;
//                    if (currentPageNumber < totalPageNumber) {
//                        showLoadMore();
//                    } else {
//                        hideLoadMore();
//                    }
//                } else {
//                    hideLoadMore();
//                }
//            }
//        });
//
//
//
//        flLoadMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pageListener.onLoadMore();
//                loadMoreProgressBar.setVisibility(View.VISIBLE);
//            }
//        });
//
//        if (!chatDisplayModels.isEmpty()) {
//            llContent.setVisibility(View.VISIBLE);
//            tvNoData.setVisibility(View.GONE);
//            displayList(chatDisplayModels);
//            showLoadMore();
//        } else {
//            llContent.setVisibility(View.GONE);
//        }
//
//        return rootView;
//
//    }
//
//    private void hideLoadMore() {
//        flLoadMore.setVisibility(View.GONE);
//    }
//
//    private void showLoadMore() {
//        flLoadMore.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        /*if (!chatDisplayModels.isEmpty()) {
//            llContent.setVisibility(View.VISIBLE);
//            tvNoData.setVisibility(View.GONE);
//            displayList(chatDisplayModels);
//        } else {
//            llContent.setVisibility(View.GONE);
//            if (progressDialog != null && !progressDialog.isShowing()) {
//                tvNoData.setVisibility(View.VISIBLE);
//            }
//        }*/
//    }
//
//    private void displayList(List<Object> chatDisplayModelList) {
//        chatAdapter = new ChatListAdapter(getActivity(), R.layout.list_row_chat, chatDisplayModelList, productModel);
//        lvChat.setAdapter(chatAdapter);
//        lvChat.invalidate();
//
//        if(Constants.SELECTED_POSITION != -1){
//            lvChat.setSelection(Constants.SELECTED_POSITION);
//
//        }
//    }
//
//
//    @Override
//    public void onRefresh() {
//
//    }
//
//    private LandingActivity.ChatAdapterListener chatAdapterListener = new LandingActivity.ChatAdapterListener() {
//        @Override
//        public void onChatFetchSuccess(List chatDisplayModels) {
//            if (!chatDisplayModels.isEmpty()) {
//                llContent.setVisibility(View.VISIBLE);
//                tvNoData.setVisibility(View.GONE);
//                displayList(chatDisplayModels);
//                ChatFragment.this.chatDisplayModels = chatDisplayModels;
//            } else {
//                llContent.setVisibility(View.GONE);
//                tvNoData.setVisibility(View.VISIBLE);
//            }
//
//            loadMoreProgressBar.setVisibility(View.GONE);
//
//        }
//
//        @Override
//        public void onChatFetchFailure(ErrorModel errorModel) {
//            switch (errorModel.getErrorType()) {
//                case ERROR_TYPE_NO_NETWORK:
//                    Utils.showNetworkDisableDialog(getActivity(), errorModel.getErrorMessage());
//                    break;
//                case ERROR_TYPE_SERVER:
//                    Utils.showGenericDialog(getActivity(), errorModel.getErrorMessage());
//                    break;
//                default:
//                    llContent.setVisibility(View.GONE);
//                    tvNoData.setVisibility(View.VISIBLE);
//                    break;
//            }
//
//            flLoadMore.setVisibility(View.GONE);
//            loadMoreProgressBar.setVisibility(View.GONE);
//
//        }
//    };
//
//    TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            SearchManager searchManager = new SearchManager(chatDisplayModels, s.toString(), SearchManager.SearchableEnum.CHAT);
//            searchManager.setSearchListener(searchListener);
//            searchManager.initSearch();
//        }
//    };
//
//    SearchManager.SearchListener searchListener = new SearchManager.SearchListener() {
//        @Override
//        public void onSearchSuccess(List<Object> searchedList) {
//            displayList(searchedList);
//        }
//
//        @Override
//        public void onSearchFail() {
//            // TODO Localization
//            //Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onSearchStringEmpty() {
//            displayList(chatDisplayModels);
//        }
//    };
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        this.productModel = null;
//    }
//
//    public void setPageListener(PageListener pageListener) {
//        this.pageListener = pageListener;
//    }
//
//    public interface PageListener {
//        public void onLoadMore();
//    }
//
//
//}
