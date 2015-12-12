package com.shopchat.consumer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shopchat.consumer.R;
import com.shopchat.consumer.adapters.HomePageFragmentAdapter;
//import com.shopchat.consumer.fragments.ChatFragment;
import com.shopchat.consumer.fragments.HomeFragment;
import com.shopchat.consumer.fragments.InboxFragment;
import com.shopchat.consumer.fragments.ProfileFragment;
import com.shopchat.consumer.fragments.ShopChatNavigationFragment;
import com.shopchat.consumer.listener.ChatListener;
import com.shopchat.consumer.listener.CityListener;
import com.shopchat.consumer.listener.NewMessageCountListener;
import com.shopchat.consumer.managers.DataBaseManager;
import com.shopchat.consumer.managers.SharedPreferenceManager;
import com.shopchat.consumer.models.ChatDisplayModel;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.models.entities.AnswerEntity;
import com.shopchat.consumer.models.entities.QuestionEntity;
import com.shopchat.consumer.services.InboxDataService;
import com.shopchat.consumer.task.ChatTask;
import com.shopchat.consumer.task.CityTask;
import com.shopchat.consumer.task.NewMessageCountTask;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.ActionBarHome;
import com.shopchat.consumer.views.CustomProgress;
import com.shopchat.consumer.views.SlidingTabLayout;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sudipta on 8/7/2015.
 */
public class LandingActivity extends BaseActivity implements ActionBarHome.OnActionBarItemClickListener, InboxFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<String> {

    public static Context mLandingActivityContext;
    public static LoaderManager supportLoaderManager;
    public static Resources resources;
    public static String CALLING_ACTIVITY = "calling_activity";
    public static String PRODUCT = "product";
    public static String PRODUCT_ID = "product_id";
    public static final int REFRESH_TIME = 20000;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ShopChatNavigationFragment mNavigationDrawerFragment;
    private HomePageFragmentAdapter adapter;
    private SaveClickListener saveClickListener;
    private ActionBarHome actionBarHome;
    private CustomProgress progressDialog;
    //private ChatAdapterListener chatAdapterListener;
    private ProductModel productModel;
    private Timer timer;
    private DataBaseManager dataBaseManager;

    public static Intent getIntent(Context context, String callingActivity, ProductModel productModel) {
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(CALLING_ACTIVITY, callingActivity);
        intent.putExtra(PRODUCT, productModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mLandingActivityContext = LandingActivity.this;
        supportLoaderManager = getSupportLoaderManager();
        resources = getResources();

        dataBaseManager = new DataBaseManager(mLandingActivityContext);

        setUpSupportActionBar(true, false, "Home");

        mNavigationDrawerFragment = (ShopChatNavigationFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.closeNavigationDrawer();

        // Initiate loader to start loading inbox data
        getSupportLoaderManager().initLoader(getResources().getInteger(R.integer.LOADER_INBOX_DATA), null, this).forceLoad();

        initViews();
        // initChatFetchTask("0", false, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        actionBarHome = getActionBarHome();
        setUpSelectedLocation();

        // fetchChatPeriodically();
    }

//    private void fetchChatPeriodically() {
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initNewMessageTask();
//                    }
//                });
//            }
//        }, 0, REFRESH_TIME);
//    }

//    private void initNewMessageTask() {
//
//        NewMessageCountListener newMessageCountListener = new NewMessageCountListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(String response) {
//                if (response.equalsIgnoreCase("0")) {
//
//                } else {
//                    if (viewPager.getCurrentItem() == 0) {
//                        initChatFetchTask("0", false, true);
//                    } else {
//                        initChatFetchTask("0", false, false);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(ErrorModel errorModel) {
//                switch (errorModel.getErrorType()) {
//                    case ERROR_TYPE_NO_NETWORK:
//                        Utils.showNetworkDisableDialog(LandingActivity.this, errorModel.getErrorMessage());
//                        break;
//                    case ERROR_TYPE_SERVER:
//                        Utils.showGenericDialog(LandingActivity.this, errorModel.getErrorMessage());
//                        break;
//                    default:
//
//                        break;
//                }
//            }
//        };
//
//        NewMessageCountTask newMessageCountTask = new NewMessageCountTask(this, newMessageCountListener);
//        newMessageCountTask.fetchNewMessage();
//    }

    /*
    Get fresh inbox data from server
     */
//    private void initChatFetchTask(final String pageCounter, final boolean isLoadMore, final boolean isFromAlertDialog) {
//
//        progressDialog = Utils.getProgressDialog(this);
//        ChatListener chatListener = new ChatListener() {
//            @Override
//            public void onChatFetchStart() {
//                // progressDialog.show();
//            }
//
//            @Override
//            public void onChatFetchSuccess(List<ProductModel> chatList) {
//                //progressDialog.dismiss();
//
//                List<ChatDisplayModel> chatDisplayModelList = createInboxDisplayList(chatList);
//
//
//                if (!isLoadMore) {
//                    if (isFromAlertDialog) {
//                        // TODO Localization
//                        showNewMessageAvailableDialog(LandingActivity.this, "There is a new message available, would you like to read it?", chatList, chatDisplayModelList);
//                    } else {
//                        setChatData(chatList, chatDisplayModelList);
//                        chatAdapterListener.onChatFetchSuccess(Utils.getShopChatApplication(LandingActivity.this).getChatDisplayModels());
//                    }
//
//                } else {
//                    addChatData(chatList, chatDisplayModelList);
//                    chatAdapterListener.onChatFetchSuccess(Utils.getShopChatApplication(LandingActivity.this).getChatDisplayModels());
//
//                }
//
//            }
//
//            @Override
//            public void onChatFetchFailure(ErrorModel errorModel) {
//                // progressDialog.dismiss();
//                chatAdapterListener.onChatFetchFailure(errorModel);
//
//            }
//        };
//
//        ChatTask chatTask = new ChatTask(this, chatListener, pageCounter);
//        chatTask.fetchChat();
//    }
//
//    private void addChatData(List<ProductModel> chatList, List<ChatDisplayModel> chatDisplayModelList) {
//        Utils.getShopChatApplication(LandingActivity.this).getChatDisplayModels().addAll(chatDisplayModelList);
//        Utils.getShopChatApplication(LandingActivity.this).getChatList().addAll(chatList);
//    }
//
//    private void setChatData(List<ProductModel> chatList, List<ChatDisplayModel> chatDisplayModelList) {
//        Utils.getShopChatApplication(LandingActivity.this).setChatDisplayModels(chatDisplayModelList);
//        Utils.getShopChatApplication(LandingActivity.this).setChatList(chatList);
//    }
//
//
//    ChatFragment.PageListener pageListener = new ChatFragment.PageListener() {
//        @Override
//        public void onLoadMore() {
//            int currentPageNumber = Utils.getShopChatApplication(LandingActivity.this).getCurrentChatPageNumber() + 1;
//            initChatFetchTask(String.valueOf(currentPageNumber), true, false);
//        }
//    };
//
//    private void storeAndDisplayChat(List<ChatDisplayModel> chatDisplayModelList) {
//        List<ChatDisplayModel> cachedChatDisplayList = Utils.getShopChatApplication(LandingActivity.this).getChatDisplayModels();
//
//        if (cachedChatDisplayList != null && !cachedChatDisplayList.isEmpty()) {
//
//            for (ChatDisplayModel cachedChatDisplayModel : cachedChatDisplayList) {
//                String replyContent = cachedChatDisplayModel.getRetailerConsolidatedChatContent();
//                String productId = cachedChatDisplayModel.getProductId();
//
//                for (ChatDisplayModel chatDisplayModel : chatDisplayModelList) {
//                    if (productId.equalsIgnoreCase(chatDisplayModel.getProductId())) {
//                        if (!replyContent.equalsIgnoreCase(chatDisplayModel.getRetailerConsolidatedChatContent())) {
//                            chatDisplayModel.setIsRead(true);
//                        }
//                    }
//                }
//
//            }
//        }
//        Utils.getShopChatApplication(LandingActivity.this).setChatDisplayModels(chatDisplayModelList);
//        chatAdapterListener.onChatFetchSuccess(chatDisplayModelList);
//
//    }

//    private List<ChatDisplayModel> createInboxDisplayList(List<ProductModel> chatList) {
//        List<ChatDisplayModel> chatDisplayModelList = new ArrayList<ChatDisplayModel>();
//
//
//        for (ProductModel productModel : chatList) {
//            ChatDisplayModel chatDisplayModel = new ChatDisplayModel();
//            chatDisplayModel.setProductId(productModel.getProductId());
//            chatDisplayModel.setProductName(productModel.getProductName());
//            StringBuilder stringBuilder = new StringBuilder();
//            for (RetailerModel retailerModel : productModel.getRetailerModels()) {
//                if (!retailerModel.getRetailerChatContent().isEmpty()) {
//                    stringBuilder.append(retailerModel.getRetailerChatContent());
//                    stringBuilder.append(" ");
//                }
//            }
//            chatDisplayModel.setConsumerChatContent(productModel.getConsumerChatContent());
//            chatDisplayModel.setRetailerConsolidatedChatContent(stringBuilder.toString());
//            chatDisplayModelList.add(chatDisplayModel);
//        }
//
//
//        return chatDisplayModelList;
//    }
//
//    private List<ChatDisplayModel> createChatDisplayList(MultiMap<String, ProductModel> chatMap) {
//        List<ChatDisplayModel> chatDisplayModelList = new ArrayList<ChatDisplayModel>();
//
//        Iterator iterator = chatMap.entrySet().iterator();
//
//        while (iterator.hasNext()) {
//            MultiValueMap.Entry entry = (MultiValueMap.Entry) iterator.next();
//
//            ArrayList<ProductModel> productList = (ArrayList) entry.getValue();
//            ChatDisplayModel chatDisplayModel = new ChatDisplayModel();
//            StringBuilder stringBuilder = new StringBuilder();
//            for (ProductModel productModel : productList) {
//                chatDisplayModel.setProductId(productModel.getProductId());
//                chatDisplayModel.setProductName(productModel.getProductName());
//                for (RetailerModel retailerModel : productModel.getRetailerModels()) {
//                    if (!retailerModel.getRetailerChatContent().isEmpty()) {
//                        stringBuilder.append(retailerModel.getRetailerChatContent());
//                        stringBuilder.append(" ");
//                    }
//                }
//
//               /* String latestTimeStamp = (productModel.getRetailerModels().get(productModel.getRetailerModels().size()-1)).getChatTimeStamp();
//                chatDisplayModel.setTimeStamp(latestTimeStamp);*/
//            }
//
//
//            chatDisplayModel.setRetailerConsolidatedChatContent(stringBuilder.toString());
//            chatDisplayModelList.add(chatDisplayModel);
//
//        }
//
//        return chatDisplayModelList;
//    }


    private void setUpSelectedLocation() {
        final String selectedCity = Utils.getPersistenceData(this, Constants.CITY_PREFERENCE);
        final String selectedLocation = Utils.getPersistenceData(this, Constants.LOCATION_PREFERENCE);
        if (TextUtils.isEmpty(selectedCity) || TextUtils.isEmpty(selectedLocation)) {
            showPickCityDialog();
        } else {
            getActionBarHome().setSelectedState(selectedCity);
            getActionBarHome().setSelectedLocation(selectedLocation);
        }
    }

    private void initViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            adapter = new HomePageFragmentAdapter(getFragmentManager());
        } else {
            adapter = new HomePageFragmentAdapter(getFragmentManager());
            adapter.setReuseFragments(false);
        }

//        ChatFragment chatFragment = new ChatFragment();
//        chatFragment.setPageListener(pageListener);

        InboxFragment inboxFragment = new InboxFragment();

        adapter.addFragment(getString(R.string.chat_fragment), inboxFragment);
        adapter.addFragment(getString(R.string.home_fragment), new HomeFragment());
        adapter.addFragment(getString(R.string.profile_fragment), new ProfileFragment());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        viewPager.setAdapter(adapter);
        slidingTabLayout.setViewPager(viewPager);

        String callingActivity = this.getIntent().getStringExtra(CALLING_ACTIVITY);
        productModel = this.getIntent().getParcelableExtra(PRODUCT);
        if (callingActivity != null && callingActivity.equalsIgnoreCase(ChatBoardActivity.class.toString()) && productModel != null) {
            viewPager.setCurrentItem(0);
            // initChatFetchTask("0", false, false);
        } else {
            viewPager.setCurrentItem(1);
        }

        slidingTabLayout.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (adapter.getItem(position) instanceof ProfileFragment) {
                   /* changeActionBarMultipleMenuIcon(R.drawable.ic_arrow_back_white_24dp);
                    getActionBarHome().hideActionBarMultipleMenuIcon();*/
                    getActionBarHome().setVisibleFragment(new ProfileFragment());
                } else {
                    changeActionBarMultipleMenuIcon(R.drawable.ic_share);
                    getActionBarHome().showActionBarMultipleMenuIcon();
                    getActionBarHome().setVisibleFragment(null);
                }

                if (adapter.getItem(position) instanceof InboxFragment) {
                    //initChatFetchTask();
                    Toast.makeText(getApplicationContext(), "Page changed to inbox", Toast.LENGTH_SHORT).show();
                }

                Utils.hideKeyBoard(LandingActivity.this);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onSelectedLocationClick() {
        initCityFetch();
    }

    @Override
    public void onMultipleActionMenuClick() {
        if (adapter.getItem(viewPager.getCurrentItem()) instanceof ProfileFragment) {
            saveClickListener.onSaveClick();
        } else {
            // TODO Replace with actual data
            Utils.callShareIntent(this, "test subject", "test body");
        }
    }

    /*public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i("key pressed", String.valueOf(event.getKeyCode()));
        return super.dispatchKeyEvent(event);
    }*/

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }else if (event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION){

            saveClickListener.onSaveClick();
            return true;
        }else{
            return false;
        }

    }*/

    @Override
    public void onMenuIconClick() {
        mNavigationDrawerFragment.openNavigationDrawer();
    }

    public void setSaveClickListener(SaveClickListener listener) {
        this.saveClickListener = listener;
    }


    public interface SaveClickListener {
        void onSaveClick();
    }

//    public void setChatAdapterListener(ChatAdapterListener chatAdapterListener) {
//        this.chatAdapterListener = chatAdapterListener;
//    }
//
//    public interface ChatAdapterListener {
//        void onChatFetchSuccess(List<ChatDisplayModel> chatDisplayModels);
//
//        void onChatFetchFailure(ErrorModel errorModel);
//    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isVisible()) {
            mNavigationDrawerFragment.closeNavigationDrawer();
        } else {
            Utils.showExitConfirmationDialog(this);
        }
    }

    public ActionBarHome getActivityActionBar() {
        return actionBarHome;
    }

    public void showPickCityDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.please_pick_location)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        initCityFetch();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeActivity();
                    }
                })
                .show();
    }

    private void initCityFetch() {

        progressDialog = Utils.getProgressDialog(this);
        CityListener cityListener = new CityListener() {
            @Override
            public void onCityFetchedStart() {
                progressDialog.show();
            }

            @Override
            public void onCityFetchSuccess(List<CityModel> cityModelList) {
                progressDialog.dismiss();
                Utils.getShopChatApplication(LandingActivity.this).setCityModelList(cityModelList);
                startActivity(CityActivity.getIntent(LandingActivity.this));
            }

            @Override
            public void onCityFetchFailure(ErrorModel errorModel) {
                progressDialog.dismiss();

                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(LandingActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(LandingActivity.this, errorModel.getErrorMessage());
                        break;
                    default:
                        break;
                }

            }
        };

        CityTask cityTask = new CityTask(this, cityListener);
        cityTask.fetchCity();
    }

    private void closeActivity() {
        this.finish();
    }

    public ProductModel getProductModel() {
        return this.productModel;
    }

    public CustomProgress getProgressDialog() {
        return progressDialog;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Constants.SELECTED_POSITION = -1;
        Utils.getShopChatApplication(this).getChatDisplayModels().clear();
    }



    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if(id == getResources().getInteger(R.integer.LOADER_INBOX_DATA)){
            Toast.makeText(getApplicationContext(), "Inbox data loader initiated", Toast.LENGTH_SHORT).show();
            InboxDataLoader inboxDataLoader = new InboxDataLoader(getApplicationContext());
            inboxDataLoader.setPageNumber(0);
            return inboxDataLoader;
        }else if(id == getResources().getInteger(R.integer.LOADER_POLL_INBOX_DATA)){
            Toast.makeText(getApplicationContext(), "Looking for new messages....", Toast.LENGTH_SHORT).show();
            PollInboxDataLoader pollInboxDataLoader = new PollInboxDataLoader(getApplicationContext());
            return pollInboxDataLoader;
        }else{
            Toast.makeText(getApplicationContext(), "No resource could be located for loading..", Toast.LENGTH_SHORT).show();
            NoResourceLoader noResourceLoader = new NoResourceLoader(getApplicationContext());
            return noResourceLoader;
        }


    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(loader.getId() == getResources().getInteger(R.integer.LOADER_INBOX_DATA)){
            // Initiate loader to start loading inbox data
            getSupportLoaderManager().initLoader(getResources().getInteger(R.integer.LOADER_POLL_INBOX_DATA), null, this).forceLoad();
        }else if(loader.getId() == getResources().getInteger(R.integer.LOADER_POLL_INBOX_DATA)){
            getSupportLoaderManager().initLoader(getResources().getInteger(R.integer.LOADER_POLL_INBOX_DATA), null, this).forceLoad();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private static class InboxDataLoader extends AsyncTaskLoader<String>{

        private int pageNumber;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public InboxDataLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            InboxDataService inboxDataService = new InboxDataService();
            inboxDataService.setContext(LandingActivity.mLandingActivityContext);
            inboxDataService.setPageNumber(this.getPageNumber());
            inboxDataService.fetchAllQuestionDataForInbox();
            SharedPreferences.Editor editor = SharedPreferenceManager.getSharedPreferenceEditor(LandingActivity.mLandingActivityContext);
            SharedPreferenceManager.setValueInSharedPreference(editor,
                    SharedPreferenceManager.KEY_INBOX_LATEST_PAGE, String.valueOf(this.getPageNumber()));
            return "";
        }
    }

    private static class PollInboxDataLoader extends AsyncTaskLoader<String>{

        public PollInboxDataLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            int numNewMessage = 0;
            InboxDataService inboxDataService = new InboxDataService();
            inboxDataService.setContext(LandingActivity.mLandingActivityContext);
            while (numNewMessage == 0){
                try {
                    Thread.sleep(REFRESH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                numNewMessage = inboxDataService.getNumberOfUpdatedQuestions();
                Log.v("LokatChat", "Number of new messages polled = " + String.valueOf(numNewMessage));
            }
            if(numNewMessage > 0){
                Log.v("LokatChat", "Fetching new messages : " + String.valueOf(numNewMessage));
                SharedPreferences.Editor editor = SharedPreferenceManager.getSharedPreferenceEditor(LandingActivity.mLandingActivityContext);
                int numPages = (int)Math.ceil((double)numNewMessage/Constants.MESSAGE_BATCH_SIZE);
                for(int pageNum = 0; pageNum < numPages; pageNum++){
                    Log.v("LokatChat", "Fetching page number: " + String.valueOf(pageNum));
                    inboxDataService.setPageNumber(pageNum);
                    inboxDataService.fetchAllQuestionDataForInbox();
                    SharedPreferenceManager.setValueInSharedPreference(editor,
                            SharedPreferenceManager.KEY_INBOX_LATEST_PAGE, String.valueOf(pageNum));
                }

            }
            return null;
        }
    }


    private static class NoResourceLoader extends AsyncTaskLoader<String>{

        public NoResourceLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            return null;
        }
    }

    @Override
    public void onInboxFragmentInteraction() {

    }
}
