package com.shopchat.consumer.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.shopchat.consumer.activities.LandingActivity;
import com.shopchat.consumer.utils.Constants;

/**
 * Created by sourin on 16/12/15.
 */
public class InboxDataSyncIntentService extends IntentService {

    public static final String SERVICE_TYPE = "SERVICE_TYPE";
    public static final String INBOX_POLLED_MESSAGE_SYNC_SERVICE = "INBOX_POLLED_MESSAGE_SYNC_SERVICE";

    private static final int REFRESH_TIME = 20000;
    private int numNewMessage = 1;

    public InboxDataSyncIntentService() {
        super("InboxDataSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("Sync>>>", "service inside ..." + intent.getStringExtra(SERVICE_TYPE));

        if(intent.getStringExtra(SERVICE_TYPE).equalsIgnoreCase(INBOX_POLLED_MESSAGE_SYNC_SERVICE)){
            Log.d("Sync>>>","here i am..");
            while(this.numNewMessage > 0){
                Log.d("Sync>>>","here i am..inside");
                this.numNewMessage = 0;
                InboxDataService inboxDataService = new InboxDataService();
                inboxDataService.setContext(LandingActivity.mLandingActivityContext);
                while (numNewMessage == 0){
                    try {
                        Thread.sleep(REFRESH_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    numNewMessage = inboxDataService.getNumberOfUpdatedQuestions();
                    Log.d("LokatChat", "Number of new messages polled = " + String.valueOf(numNewMessage));
                }

                if(numNewMessage > 0){
                    Log.d("LokatChat", "Fetching new messages : " + String.valueOf(numNewMessage));
//                SharedPreferences.Editor editor = SharedPreferenceManager.getSharedPreferenceEditor(LandingActivity.mLandingActivityContext);
                    int numPages = (int)Math.ceil((double)numNewMessage/ Constants.MESSAGE_BATCH_SIZE);
                    for(int pageNum = 0; pageNum < numPages; pageNum++){
                        Log.d("LokatChat", "Fetching page number: " + String.valueOf(pageNum));
                        inboxDataService.setPageNumber(pageNum);
                        inboxDataService.fetchAllQuestionDataForInbox();
//                    SharedPreferenceManager.setValueInSharedPreference(editor,
//                            SharedPreferenceManager.KEY_INBOX_LATEST_PAGE, String.valueOf(pageNum));
                    }

                }

            }

        }

    }
}
