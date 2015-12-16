package com.shopchat.consumer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.activities.LandingActivity;
import com.shopchat.consumer.adapters.InboxMessageListAdapter;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.entities.InboxMessageEntity;
import com.shopchat.consumer.services.InboxDataService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<InboxMessageEntity>>{

    private OnFragmentInteractionListener mListener;

    private EditText edtText_search;
    private ListView lvInboxMessageList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment InboxFragment.
     */
    public static InboxFragment newInstance() {
        InboxFragment fragment = new InboxFragment();
        return fragment;
    }

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        // Initializing the inbox list
        LandingActivity.supportLoaderManager.initLoader(getResources()
                .getInteger(R.integer.LOADER_ADAPTER_INBOX_MESSAGE), null, this).forceLoad();

    }

    private void initViews(View view) {
        lvInboxMessageList = (ListView)view.findViewById(R.id.lvInboxMessageList);
        edtText_search = (EditText)view.findViewById(R.id.edtText_search);
        edtText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Toast.makeText(getActivity(), edtText_search.getText() + "<<<", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onInboxFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<ArrayList<InboxMessageEntity>> onCreateLoader(int id, Bundle args) {
        if(id == getResources().getInteger(R.integer.LOADER_ADAPTER_INBOX_MESSAGE)){
            InboxMessageListLoader inboxMessageListLoader = new InboxMessageListLoader(LandingActivity.mLandingActivityContext);
            return  inboxMessageListLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<InboxMessageEntity>> loader, ArrayList<InboxMessageEntity> data) {
        if(loader.getId() == getResources().getInteger(R.integer.LOADER_ADAPTER_INBOX_MESSAGE)){
            InboxMessageListAdapter inboxMessageListAdapter = new InboxMessageListAdapter(LandingActivity.mLandingActivityContext, data);
            lvInboxMessageList.setAdapter(inboxMessageListAdapter);
            // inboxMessageListAdapter.notifyDataSetChanged();
            // TODO:: Make the list adapter class variable from local
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<InboxMessageEntity>> loader) {
        loader = null;
    }


    private static class InboxMessageListLoader extends AsyncTaskLoader<ArrayList<InboxMessageEntity>>{

        public InboxMessageListLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<InboxMessageEntity> loadInBackground() {
            ArrayList<InboxMessageEntity> inboxMessageEntities = new ArrayList<InboxMessageEntity>();

            InboxDataService inboxDataService = new InboxDataService();
            inboxDataService.setContext(LandingActivity.mLandingActivityContext);

            inboxMessageEntities = inboxDataService.getListOfInboxMessageEntities();

            return inboxMessageEntities;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onInboxFragmentInteraction();
    }

}
