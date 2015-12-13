package com.shopchat.consumer.activities;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.adapters.InboxMessageDetailListAdapter;
import com.shopchat.consumer.managers.DataBaseManager;
import com.shopchat.consumer.models.entities.AnswerEntity;
import com.shopchat.consumer.models.entities.InboxMessageDetailEntity;
import com.shopchat.consumer.models.entities.RetailerEntity;
import com.shopchat.consumer.services.InboxDataService;

import org.w3c.dom.Text;

import java.security.PrivilegedAction;

public class InboxMessageDetailActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<InboxMessageDetailEntity>{

    private String questionId;
    private InboxMessageDetailEntity inboxMessageDetailEntity;

    public static Context mInboxMessageDetailActivityContext;

    private ListView lvQuestionDetails;
    private TextView tvBackToInbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_message_detail);

        Bundle questionInformation = getIntent().getExtras();
        this.questionId = questionInformation.getString("QUESTION_ID");

        initViews();
        getSupportLoaderManager().initLoader(getResources().getInteger(R.integer.LOADER_QUESTION_DETAILS), null, this).forceLoad();
    }

    private void initViews() {
        lvQuestionDetails = (ListView)findViewById(R.id.lvQuestionDetails);
        tvBackToInbox = (TextView)findViewById(R.id.tvBackToInbox);
        tvBackToInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        inboxMessageDetailEntity = new InboxMessageDetailEntity();
        mInboxMessageDetailActivityContext = InboxMessageDetailActivity.this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox_message_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<InboxMessageDetailEntity> onCreateLoader(int id, Bundle args) {
        if(id == getResources().getInteger(R.integer.LOADER_QUESTION_DETAILS)){
            InboxMessageDetailLoader inboxMessageDetailLoader = new InboxMessageDetailLoader(this, this.questionId);
            return inboxMessageDetailLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<InboxMessageDetailEntity> loader, InboxMessageDetailEntity data) {
        if(loader.getId() == getResources().getInteger(R.integer.LOADER_QUESTION_DETAILS)){
            InboxMessageDetailListAdapter inboxMessageDetailListAdapter = new InboxMessageDetailListAdapter(
                    InboxMessageDetailActivity.mInboxMessageDetailActivityContext, data);
            this.lvQuestionDetails.setAdapter(inboxMessageDetailListAdapter);
            // TODO:: Make the list adapter class variable from local
        }
    }

    @Override
    public void onLoaderReset(Loader<InboxMessageDetailEntity> loader) {
        loader = null;
    }

    private static class InboxMessageDetailLoader extends AsyncTaskLoader<InboxMessageDetailEntity> {

        private String questionId;
        private Context context;

        public InboxMessageDetailLoader(Context context, String qId) {
            super(context);
            this.context = context;
            this.questionId = qId;
        }

        @Override
        public InboxMessageDetailEntity loadInBackground() {

            if(this.questionId != null){
                InboxMessageDetailEntity inboxMessageDetailEntity = new InboxMessageDetailEntity();
                InboxDataService inboxDataService = new InboxDataService();
                inboxDataService.setContext(InboxMessageDetailActivity.mInboxMessageDetailActivityContext);
                return inboxDataService.getMessageDetailFromQuestionId(this.questionId);
            }
            return null;
        }
    }
}
