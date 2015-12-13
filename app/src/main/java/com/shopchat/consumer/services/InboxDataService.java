package com.shopchat.consumer.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.activities.LandingActivity;
import com.shopchat.consumer.managers.DataBaseManager;
import com.shopchat.consumer.managers.SharedPreferenceManager;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.entities.AnswerEntity;
import com.shopchat.consumer.models.entities.InboxMessageDetailEntity;
import com.shopchat.consumer.models.entities.InboxMessageEntity;
import com.shopchat.consumer.models.entities.MessageUnitEntity;
import com.shopchat.consumer.models.entities.ProductEntity;
import com.shopchat.consumer.models.entities.QuestionEntity;
import com.shopchat.consumer.models.entities.RetailerEntity;
import com.shopchat.consumer.models.pojos.AllQuestionsPojo;
import com.shopchat.consumer.models.pojos.AnswerPojo;
import com.shopchat.consumer.models.pojos.PagePojo;
import com.shopchat.consumer.models.pojos.ProductPojo;
import com.shopchat.consumer.models.pojos.QuestionPojo;
import com.shopchat.consumer.models.pojos.RetailerPojo;
import com.shopchat.consumer.network.HttpPostClient;
import com.shopchat.consumer.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sourin on 03/12/15.
 */
public class InboxDataService {

    private Context context;    // Must be initialised to use any service
    private int pageNumber;     // Must be called to fetch data from Network

    private HttpPostClient httpPostClient;      // Self initialised

    private int responseCode;       // Returned for network call methods
    private String errorMessage;    // Returned for network call methods

    private DataBaseManager dataBaseManager;

    public HttpPostClient getHttpPostClient() {
        return httpPostClient;
    }

    public void setHttpPostClient(HttpPostClient httpPostClient) {
        this.httpPostClient = httpPostClient;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean fetchAllQuestionDataForInbox(){

        httpPostClient = new HttpPostClient();
        httpPostClient.setUrl(Constants.BASE_URL + Constants.CHAT_URL);

        String payload;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNumber", this.pageNumber);
            jsonObject.put("size", String.valueOf(Constants.MESSAGE_BATCH_SIZE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        payload = jsonObject.toString();

        httpPostClient.setPayload(payload);

        LoginModel loginModel = ((ShopChatApplication) this.context.getApplicationContext()).getLoginModel();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(payload.length()));
        headers.put("Cookie", loginModel.getCookieName() + "=" + loginModel.getCookieValue());

        httpPostClient.setHeaders(headers);
        boolean success = httpPostClient.sendPostRequest();

        if(success && (httpPostClient.getResponseCode() == 200)){

            String mJsonResp = httpPostClient.getResponse();
            JsonReader reader = new JsonReader(new StringReader(mJsonResp));
            reader.setLenient(true);
            Gson gson = new Gson();
            AllQuestionsPojo allQuestionsPojo = gson.fromJson(reader, AllQuestionsPojo.class);
            extractAndStoreQuestionsFromPojo(allQuestionsPojo);

            this.setResponseCode(httpPostClient.getResponseCode());
            this.setErrorMessage("");

        }else {
            if(success){
                Log.v("LokalChat", "URL:" + httpPostClient.getUrl());
                Log.v("LokalChat", "ResponseCode:" + httpPostClient.getResponseCode());
                Log.v("LokalChat", "Response:" + httpPostClient.getResponse());
                this.setResponseCode(httpPostClient.getResponseCode());
                this.setErrorMessage(LandingActivity.resources.getString(R.string.invalid_request));
            }else{
                Log.v("LokalChat", "URL:" + httpPostClient.getUrl());
                Log.v("LokalChat", "ResponseCode: 0");
                Log.v("LokalChat", "Response: Cannot connect to network/server !");
                this.setResponseCode(0);
                this.setErrorMessage(LandingActivity.resources.getString(R.string.error_no_network));
            }
        }

        return success;
    }

    private boolean extractAndStoreQuestionsFromPojo(AllQuestionsPojo allQuestionsPojo){

        // Initialising database manager
        this.dataBaseManager = new DataBaseManager(getContext());

        /*  Saving page related information  */
        SharedPreferences.Editor spEditor = SharedPreferenceManager.getSharedPreferenceEditor(this.context);

        PagePojo pagePojo = allQuestionsPojo.getPage();
        SharedPreferenceManager.setValueInSharedPreference(spEditor, SharedPreferenceManager.KEY_TOTAL_INBOX_CONVERSATIONS,
                Integer.toString(pagePojo.getTotalElements()));
        SharedPreferenceManager.setValueInSharedPreference(spEditor, SharedPreferenceManager.KEY_TOTAL_INBOX_PAGES,
                Integer.toString(pagePojo.getTotalPages()));
        SharedPreferenceManager.setValueInSharedPreference(spEditor, SharedPreferenceManager.KEY_INBOX_LATEST_PAGE,
                Integer.toString(pagePojo.getNumber()));

        /*  Saving questions and answers  */
        QuestionPojo[] questionPojos = allQuestionsPojo.getContent();

        for(int qpIndex = 0; qpIndex < questionPojos.length; qpIndex++){
            QuestionPojo questionPojo = questionPojos[qpIndex];
            ProductPojo productPojo = questionPojo.getProduct();

            QuestionEntity questionEntity = new QuestionEntity();
            ProductEntity productEntity = new ProductEntity();

            productEntity.setProductId(productPojo.getId());
            productEntity.setCategory(productPojo.getCategory());
            productEntity.setCategoryDescription(productPojo.getCategoryDescription());
            productEntity.setProductName(productPojo.getProductName());
            productEntity.setProductDescription(productPojo.getProductDescription());
            productEntity.setImageurl(productPojo.getImageurl());

            Log.v("debug@@", "product->" + productEntity.getProductId() + "--" + productEntity.getProductName());
            if(this.dataBaseManager.getProductEntityFromProductId(productEntity.getProductId()) == null){
                this.dataBaseManager.addProductEntity(productEntity);
            }

            questionEntity.setQuestionId(questionPojo.getId());
            questionEntity.setQuestionText(questionPojo.getQuestionText());
            questionEntity.setProductId(questionPojo.getProduct().getId());
            questionEntity.setUpdatedAt(System.currentTimeMillis()/1000l);
            // Store question in db
            Log.v("debug@@", "question->" + questionEntity.getQuestionId() + "--" + questionEntity.getQuestionText());
            if(this.dataBaseManager.getQuestionEntityFromQuestionId(questionEntity.getQuestionId()) == null){
                this.dataBaseManager.addQuestionEntity(questionEntity);
            }

            AnswerPojo[] answerPojos = questionPojo.getAnswers();
            for(int apIndex = 0; apIndex < answerPojos.length; apIndex++){

                AnswerPojo answerPojo = answerPojos[apIndex];
                RetailerPojo retailerPojo = answerPojo.getRetailer();

                AnswerEntity answerEntity = new AnswerEntity();
                RetailerEntity retailerEntity = new RetailerEntity();

                retailerEntity.setRetailerId(retailerPojo.getId());
                retailerEntity.setShopName(retailerPojo.getShopName());
                // Store retailer in db
                Log.v("debug@@", "retailer->" + retailerEntity.getRetailerId() + "--" + retailerEntity.getShopName());
                if(this.dataBaseManager.getRetailerEntityFromRetailerId(retailerEntity.getRetailerId()) == null){
                    this.dataBaseManager.addRetailerEntity(retailerEntity);
                }

                answerEntity.setAnswerId(answerPojo.getId());
                answerEntity.setAnswerText(answerPojo.getAnswerText());
                answerEntity.setRetailerId(answerPojo.getRetailerId());
                answerEntity.setQuestionId(questionEntity.getQuestionId());
                answerEntity.setUpdatedAt(System.currentTimeMillis()/1000l);
                // Store answer in db
                Log.v("debug@@", "answer->" + answerEntity.getAnswerId() + "--" + answerEntity.getAnswerText());
                if(this.dataBaseManager.getAnswerEntityFromAnswerId(answerEntity.getAnswerId()) == null){
                    this.dataBaseManager.addAnswerEntity(answerEntity);
                }else{
                    this.dataBaseManager.updateAnswerEntity(answerEntity);
                }

            }

        }



        return false;
    }

    public int getNumberOfUpdatedQuestions(){
        int newUpdates = 0;

        httpPostClient = new HttpPostClient();
        httpPostClient.setUrl(Constants.BASE_URL + Constants.NEW_MESSAGE_COUNT_URL);
        String payload = "";

        httpPostClient.setPayload(payload);

        LoginModel loginModel = ((ShopChatApplication) this.context.getApplicationContext()).getLoginModel();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(payload.length()));
        headers.put("Cookie", loginModel.getCookieName() + "=" + loginModel.getCookieValue());

        httpPostClient.setHeaders(headers);
        boolean success = httpPostClient.sendPostRequest();

        if(success && (httpPostClient.getResponseCode() == 200)){

            String updateResponse = httpPostClient.getResponse();
            newUpdates = Integer.valueOf(updateResponse);

        }else {
            if(success){
                Log.v("LokalChat", "URL:" + httpPostClient.getUrl());
                Log.v("LokalChat", "ResponseCode:" + httpPostClient.getResponseCode());
                Log.v("LokalChat", "Response:" + httpPostClient.getResponse());
                this.setResponseCode(httpPostClient.getResponseCode());
                this.setErrorMessage(LandingActivity.resources.getString(R.string.invalid_request));
            }else{
                Log.v("LokalChat", "URL:" + httpPostClient.getUrl());
                Log.v("LokalChat", "ResponseCode: 0");
                Log.v("LokalChat", "Response: Cannot connect to network/server !");
                this.setResponseCode(0);
                this.setErrorMessage(LandingActivity.resources.getString(R.string.error_no_network));
            }
        }

        return newUpdates;
    }

    public ArrayList<InboxMessageEntity> getListOfInboxMessageEntities(){

        ArrayList<InboxMessageEntity> inboxMessageEntities = new ArrayList<InboxMessageEntity>();
        ArrayList<QuestionEntity> questionEntities = new ArrayList<QuestionEntity>();
        QuestionEntity questionEntity;
        InboxMessageEntity inboxMessageEntity;
        ProductEntity productEntity;
        // Initialising database manager
        this.dataBaseManager = new DataBaseManager(getContext());

        questionEntities = this.dataBaseManager.getAllQuestionEntities();
        for(int index = 0; index < questionEntities.size(); index++){
            questionEntity = questionEntities.get(index);
            productEntity = new ProductEntity();
            productEntity = this.dataBaseManager.getProductEntityFromProductId(questionEntity.getProductId());

            inboxMessageEntity = new InboxMessageEntity();
            inboxMessageEntity.setQuestionId(questionEntity.getQuestionId());
            inboxMessageEntity.setMessageDetail(questionEntity.getQuestionText());
            inboxMessageEntity.setMessageHeader(productEntity.getProductName());

            inboxMessageEntities.add(inboxMessageEntity);
        }
        return inboxMessageEntities;
    }

    public InboxMessageDetailEntity getMessageDetailFromQuestionId(String questionId){

        InboxMessageDetailEntity inboxMessageDetailEntity = new InboxMessageDetailEntity();
        MessageUnitEntity questionMessageUnitEntity = new MessageUnitEntity();
        ArrayList<MessageUnitEntity> answersMessageUnitEntity = new ArrayList<MessageUnitEntity>();
        ArrayList<AnswerEntity> answerEntities = new ArrayList<AnswerEntity>();
        AnswerEntity answerEntity;
        MessageUnitEntity answerMessageUnitEntity;
        RetailerEntity retailerEntity;

        this.dataBaseManager = new DataBaseManager(this.getContext());

        if(questionId != null && this.context != null){

            QuestionEntity questionEntity = this.dataBaseManager.getQuestionEntityFromQuestionId(questionId);
            questionMessageUnitEntity.setSpeaker("You");
            questionMessageUnitEntity.setMessage(questionEntity.getQuestionText());
            questionMessageUnitEntity.setWrittenAt(String.valueOf(questionEntity.getUpdatedAt())); //TODO: Change from epoch to DateTime
            questionMessageUnitEntity.setIsQuestion(true);
            questionMessageUnitEntity.setIsAnswer(false);
            questionMessageUnitEntity.setReferenceId(questionId);

            answerEntities = this.dataBaseManager.getAnswerEntitiesFromQuestionId(questionId);


            for(int index = 0; index < answerEntities.size(); index++){
                answerEntity = new AnswerEntity();
                answerMessageUnitEntity = new MessageUnitEntity();
                retailerEntity = new RetailerEntity();

                answerEntity = answerEntities.get(index);
                retailerEntity = this.dataBaseManager.getRetailerEntityFromRetailerId(answerEntity.getRetailerId());

//                try{
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                    answerMessageUnitEntity.setSpeaker("Retailer");
//                } // TODO: Check why there is no retailer in DB
                Log.v("debug@@11", retailerEntity.getRetailerId() + retailerEntity.getShopName());
                answerMessageUnitEntity.setSpeaker(retailerEntity.getShopName());
                answerMessageUnitEntity.setMessage(answerEntity.getAnswerText());
                answerMessageUnitEntity.setWrittenAt(String.valueOf(answerEntity.getUpdatedAt()));
                answerMessageUnitEntity.setIsQuestion(false);
                answerMessageUnitEntity.setIsAnswer(true);
                answerMessageUnitEntity.setReferenceId(answerEntity.getAnswerId());

                answersMessageUnitEntity.add(answerMessageUnitEntity);
            }

            inboxMessageDetailEntity.setQuestionMessageUnitEntity(questionMessageUnitEntity);
            inboxMessageDetailEntity.setAnswersMessageUnitEntity(answersMessageUnitEntity);
            return inboxMessageDetailEntity;
        }
        return null;
    }

}
