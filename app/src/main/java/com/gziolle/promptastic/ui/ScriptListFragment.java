/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.util.Constants;
import com.gziolle.promptastic.util.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gziolle.promptastic.util.Constants.KEY_CONTENT;
import static com.gziolle.promptastic.util.Constants.KEY_DATABASE_REF;
import static com.gziolle.promptastic.util.Constants.KEY_TITLE;

/**
 * Displays a list of scripts added by the user
 */

public class ScriptListFragment extends Fragment {

    @BindView(R.id.script_list)
    RecyclerView mScriptRecyclerView;

    @BindView(R.id.empty_view)
    FrameLayout mEmptyView;

    @BindView(R.id.add_script_fab)
    FloatingActionButton mAddScriptFab;

    private FirebaseRecyclerAdapter mAdapter;
    private OnScriptSelectedListener mScriptSelectedListener;
    private OnAddScriptListener mAddScriptListener;

    private String mSelectedItemKey = "";
    private boolean mIsTwoPane = false;

    public interface OnScriptSelectedListener {
        /**
         * Listener method for when a script is selected
         */
        void onScriptSelected(Bundle bundle);
    }

    public interface OnAddScriptListener {
        /**
         * Listener method for when the Add Script button is clicked
         */
        void onAddScriptButtonSelected();
    }

    public ScriptListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_script_list, container, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mScriptRecyclerView.setLayoutManager(layoutManager);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIsTwoPane = prefs.getBoolean(Constants.IS_TWO_PANE, false);
        if (mIsTwoPane) {
            if (savedInstanceState != null) {
                String key = savedInstanceState.getString(Constants.SELECTED_KEY);
                if (!TextUtils.isEmpty(key)) {
                    mSelectedItemKey = key;
                }
            }
        }

        fetchDataFromFirebase();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Fetches the data from Firebase Realtime Daatabase and serves it to the RecyclerView
     */
    private void fetchDataFromFirebase() {
        FirebaseDatabase database = Utils.getFirebaseDatabase();
        Query query = database.getReference().child(Constants.PATH_USERS +
                FirebaseAuthManager.getInstance().getFirebaseUserId() + Constants.PATH_SCRIPTS);

        FirebaseRecyclerOptions<Script> options = new FirebaseRecyclerOptions.Builder<Script>()
                .setQuery(query, snapshot ->
                        new Script(snapshot.child(Constants.KEY_TITLE).getValue().toString(),
                                snapshot.child(Constants.KEY_CONTENT)
                                        .getValue().toString())).build();

        mAdapter = new FirebaseRecyclerAdapter<Script, ScriptViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScriptViewHolder scriptViewHolder,
                                            int position, @NonNull Script script) {
                scriptViewHolder.mTitle.setText(script.getTitle());
                scriptViewHolder.mContent.setText(script.getContent());
                scriptViewHolder.mDatabaseReference = getRef(position);

                if (mIsTwoPane) {
                    if (mSelectedItemKey.equals(scriptViewHolder.mDatabaseReference.getKey())) {
                        scriptViewHolder.mBackground.setSelected(true);
                    } else {
                        scriptViewHolder.mBackground.setSelected(false);
                    }
                }

                scriptViewHolder.itemView.setOnClickListener(v -> {
                    if (mIsTwoPane) {
                        mSelectedItemKey = scriptViewHolder.mDatabaseReference.getKey();
                        notifyDataSetChanged();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_TITLE, scriptViewHolder.mTitle.getText().toString());
                    bundle.putString(KEY_CONTENT, scriptViewHolder.mContent.getText().toString());
                    bundle.putString(
                            KEY_DATABASE_REF, scriptViewHolder.mDatabaseReference.getKey());
                    mScriptSelectedListener.onScriptSelected(bundle);
                });
            }

            @NonNull
            @Override
            public ScriptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.script_list_item, parent, false);

                return new ScriptViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                if (getItemCount() == 0) {
                    mScriptRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mScriptRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        };
        mScriptRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mIsTwoPane)
            outState.putString(Constants.SELECTED_KEY, mSelectedItemKey);

        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.add_script_fab)
    public void addScript(View view) {
        mAddScriptListener.onAddScriptButtonSelected();
    }

    public void setOnScriptSelectedListener(OnScriptSelectedListener listener) {
        if (listener != null) {
            mScriptSelectedListener = listener;
        }
    }

    public void setOnAddScriptListerner(OnAddScriptListener listener) {
        if (listener != null) {
            mAddScriptListener = listener;
        }
    }

    /**
     * ViewHolder class for the script list item
     */
    static class ScriptViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.script_title)
        TextView mTitle;
        @BindView(R.id.script_content)
        TextView mContent;
        @BindView(R.id.cv_script_list_item)
        CardView mBackground;

        DatabaseReference mDatabaseReference;

        public ScriptViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
