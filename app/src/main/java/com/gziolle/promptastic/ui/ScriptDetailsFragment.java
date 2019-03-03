/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.util.Constants;
import com.gziolle.promptastic.util.Utils;

import static android.app.Activity.RESULT_OK;
import static com.gziolle.promptastic.util.Constants.PATH_SCRIPTS;
import static com.gziolle.promptastic.util.Constants.PATH_USERS;

/*
 * Displays the script's title and content, as well as some script options
 */

public class ScriptDetailsFragment extends Fragment {

    @BindView(R.id.tv_script_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_script_content)
    TextView mContentTextView;

    @BindView(R.id.floating_menu)
    FloatingActionsMenu mFloatingMenu;

    @BindView(R.id.fl_progress_holder)
    FrameLayout mProgressHolder;

    private OnScriptListener mEditListener;

    private String mTitle;
    private String mContent;
    private String mKey;

    public interface OnScriptListener{
        void onEditScriptSelected(Bundle bundle);
        void onDeleteScript();
    }

    public ScriptDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_script_details, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();

        if(args != null && TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mContent)){
            mTitle = args.getString(Constants.KEY_TITLE);
            mContent = args.getString(Constants.KEY_CONTENT);
            mKey = args.getString(Constants.KEY_DATABASE_REF);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTitleTextView.setText(mTitle);
        mContentTextView.setText(mContent);
    }

    @OnClick(R.id.fab_edit_script)
    public void editScript(){
        mFloatingMenu.collapse();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TITLE, mTitle);
        bundle.putString(Constants.KEY_CONTENT, mContent);
        bundle.putString(Constants.KEY_DATABASE_REF, mKey);
        mEditListener.onEditScriptSelected(bundle);
    }

    @OnClick(R.id.fab_play_script)
    public void playScript(){
        mFloatingMenu.collapse();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_CONTENT, mContent);

        sendUpdateWidgetIntent();
        Intent intent = new Intent(getActivity(), PlayScriptActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.fab_delete_script)
    public void deleteScript(){
        mFloatingMenu.collapse();
        mProgressHolder.setVisibility(View.VISIBLE);
        final FirebaseDatabase database = Utils.getFirebaseDatabase();
        DatabaseReference ref = database.getReference();
        DatabaseReference scriptsRef = ref.child(PATH_USERS
                + FirebaseAuthManager.getInstance().getFirebaseUserId()
                + PATH_SCRIPTS + "/" + mKey);

        scriptsRef.removeValue().addOnCompleteListener(task -> {
            mProgressHolder.setVisibility(View.GONE);
            if(task.isSuccessful()){
                mEditListener.onDeleteScript();
            } else {
                Toast.makeText(getActivity(), "Deu ruim", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_EDIT_SCRIPT && resultCode == RESULT_OK){
            mTitle = data.getStringExtra(Constants.KEY_TITLE);
            mTitleTextView.setText(mTitle);
            mContent = data.getStringExtra(Constants.KEY_CONTENT);
            mContentTextView.setText(mContent);
        }
    }

    public void setOnEditScriptListener(OnScriptListener listener){
        if(listener != null){
            mEditListener = listener;
        }
    }

    void setText(String title, String content){
        mTitle = title;
        mContent = content;
    }

    private void sendUpdateWidgetIntent(){
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_UPDATE_WIDGET);
        Script script = new Script(mTitle, mContent);
        intent.putExtra(Constants.KEY_SCRIPT_EXTRA, script);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
        getActivity().sendBroadcast(intent);
    }
}
