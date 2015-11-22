package com.shopchat.consumer.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.adapters.ChatBoardListAdapter;
import com.shopchat.consumer.adapters.FaqAdapter;
import com.shopchat.consumer.listener.ChatBoardListener;
import com.shopchat.consumer.models.ChatBoardModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.FaqModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.task.ChatBoardTask;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.CustomProgress;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sudipta on 9/15/2015.
 */
public class ChatBoardActivity extends BaseActivity {
    public static String PRODUCT = "product";
    public static String PRODUCT_ID = "product_id";
    public static String QUESTION_TEXT = "question_text";
    private EditText edtChatContent;
    private List<ChatBoardModel> chatBoardListItems;
    private ChatBoardListAdapter chatBoardListAdapter;
    private ProductModel productModel;
    private static Context callingActivity;
    private String productId;
    private ListView listViewChatBoard;
    private LinearLayout llSendChat;
    private boolean isFromInbox;
    private String questionText;

    public static Intent getIntent(Context context, ProductModel productModel) {
        Intent intent = new Intent(context, ChatBoardActivity.class);
        intent.putExtra(PRODUCT, productModel);
        callingActivity = context;

        return intent;
    }

    public static Intent getIntent(Context context, String productId, String questionText) {
        Intent intent = new Intent(context, ChatBoardActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(QUESTION_TEXT, questionText);
        callingActivity = context;

        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_board);

        productModel = this.getIntent().getParcelableExtra(PRODUCT);

        setUpSupportActionBar(false, true, "");

        chatBoardListItems = new ArrayList<ChatBoardModel>();

        initViews();
    }

    private void initViews() {

        listViewChatBoard = (ListView) findViewById(R.id.lv_chat_board);
        final Button btnSendChat = (Button) findViewById(R.id.btn_send_chat);
        edtChatContent = (EditText) findViewById(R.id.edt_chat_content);
        llSendChat = (LinearLayout) findViewById(R.id.ll_send_chat);
        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtChatContent.getText())) {
                    sendChat();
                } else {
                    Utils.showGenericDialog(ChatBoardActivity.this, R.string.enter_text);
                }

            }
        });

        if (callingActivity instanceof LandingActivity) {
            this.productId = this.getIntent().getStringExtra(PRODUCT_ID);
            this.questionText = this.getIntent().getStringExtra(QUESTION_TEXT);
            createChatBoardRowFromInbox();
            llSendChat.setVisibility(View.GONE);
            isFromInbox = true;
        } else {
            isFromInbox = false;
        }

        chatBoardListAdapter = new ChatBoardListAdapter(this, chatBoardListItems, isFromInbox);
        listViewChatBoard.setAdapter(chatBoardListAdapter);

        final Button btnFaq = (Button) findViewById(R.id.btn_faq);
        btnFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFaqDialog();
            }
        });

    }

    private void createChatBoardRowFromInbox() {

       /* MultiMap<String, ProductModel> chatMap = Utils.getShopChatApplication(this).getChatMap();

        List<ProductModel> chatList = (List<ProductModel>) chatMap.get(productId);*/


        List<ProductModel> chatList = Utils.getShopChatApplication(this).getChatList();

        for (ProductModel productModel : chatList){
            if(productModel.getConsumerChatContent().equalsIgnoreCase(questionText)){
                this.productModel = productModel;
            }
        }


            ChatBoardModel chatBoardModel = new ChatBoardModel(productModel.getConsumerChatContent(), true, 0, null, productModel, productModel.getChatTimeStamp());
            chatBoardListItems.add(chatBoardModel);
            int retailerCount = 1;
            for (RetailerModel retailerModel : productModel.getRetailerModels()) {
                ChatBoardModel chatBoardModelRetailer = new ChatBoardModel(retailerModel.getRetailerChatContent(), false, retailerCount, retailerModel, productModel, retailerModel.getChatTimeStamp());
                chatBoardListItems.add(chatBoardModelRetailer);
                retailerCount++;
            }
    }

    private void showFaqDialog() {
        Dialog faqDialog = new Dialog(this);
        faqDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        faqDialog.setContentView(R.layout.dialog_faq);
        faqDialog.show();

        final ListView lvFaq = (ListView) faqDialog.findViewById(R.id.lv_faq);

        List<FaqModel> listFaq = new ArrayList<FaqModel>();
        listFaq.add(new FaqModel(""));
        listFaq.add(new FaqModel(""));
        listFaq.add(new FaqModel(""));
        listFaq.add(new FaqModel(""));
        listFaq.add(new FaqModel(""));
        listFaq.add(new FaqModel(""));


        FaqAdapter faqAdapter = new FaqAdapter(this, listFaq);
        lvFaq.setAdapter(faqAdapter);

    }

    private void sendChat() {

        Utils.getShopChatApplication(this).setChatBoardModels(createChatBoardRow());

        final CustomProgress progressDialog = Utils.getProgressDialog(this);
        ChatBoardListener chatBoardListener = new ChatBoardListener() {
            @Override
            public void onSendChatStart() {
                progressDialog.show();
            }

            @Override
            public void onSendChatSuccess() {
                progressDialog.dismiss();
                chatBoardListAdapter.notifyDataSetChanged();
                callLandingActivity();
            }

            @Override
            public void onSendChatFailure(ErrorModel errorModel) {
                progressDialog.dismiss();
                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(ChatBoardActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(ChatBoardActivity.this, errorModel.getErrorMessage());
                        break;
                    default:
                        rollBackChat();
                        break;
                }

            }
        };

        ChatBoardTask chatBoardTask = new ChatBoardTask(this, chatBoardListener);
        chatBoardTask.sendChat();
    }

    private void callLandingActivity() {
        startActivity(LandingActivity.getIntent(this, ChatBoardActivity.class.toString(), productModel));
    }

    private void rollBackChat() {
        // Last item of List
        chatBoardListItems.remove(chatBoardListItems.size() - 1);
        Utils.getShopChatApplication(ChatBoardActivity.this).getChatBoardModels().clear();
        chatBoardListAdapter.notifyDataSetChanged();

        // TODO Localization
        Toast.makeText(ChatBoardActivity.this, "Sending failed!", Toast.LENGTH_SHORT).show();
    }

    private List<ChatBoardModel> createChatBoardRow() {

        ChatBoardModel chatBoardModel = new ChatBoardModel(edtChatContent.getText().toString(), true, 0, null, productModel, null);
        chatBoardListItems.add(chatBoardModel);
        edtChatContent.setText("");
        //Utils.hideKeyBoard(ChatBoardActivity.this, edtChatContent);
        List<RetailerModel> selectedRetailerList = new ArrayList<>();
        if (callingActivity instanceof LandingActivity) {
            selectedRetailerList = getSelectedRetailerList();
        } else {
            selectedRetailerList = ((ShopChatApplication) getApplication()).getRetailerList();
        }

        int retailerCount = 1;
        for (RetailerModel retailerModel : selectedRetailerList) {
            ChatBoardModel chatBoardModelRetailer = new ChatBoardModel("Reply pending", false, retailerCount, retailerModel, productModel, null);
            chatBoardListItems.add(chatBoardModelRetailer);
            retailerCount++;
        }

        return chatBoardListItems;
    }

    private List<RetailerModel> getSelectedRetailerList() {
        List<RetailerModel> selectedRetailerList = new ArrayList<>();
        MultiMap<String, ProductModel> chatMap = Utils.getShopChatApplication(this).getChatMap();

        List<ProductModel> chatList = (List<ProductModel>) chatMap.get(productId);

        MultiMap<String, RetailerModel> retailerMap = new MultiValueMap<>();
        for (ProductModel productModel : chatList) {

            for (RetailerModel retailerModel : productModel.getRetailerModels()) {
                retailerMap.put(retailerModel.getRetailerId(), retailerModel);
            }
        }

        Iterator iterator = retailerMap.entrySet().iterator();

        while (iterator.hasNext()) {
            MultiValueMap.Entry entry = (MultiValueMap.Entry) iterator.next();
            ArrayList<RetailerModel> retailerModelList = (ArrayList) entry.getValue();
            if (retailerModelList != null && !retailerModelList.isEmpty()) {
                selectedRetailerList.add(retailerModelList.get(0));
            }
        }

        return selectedRetailerList;
    }


}
