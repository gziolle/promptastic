/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.util.Utils;

import static com.gziolle.promptastic.util.Constants.KEY_CONTENT;
import static com.gziolle.promptastic.util.Constants.KEY_DATABASE_REF;
import static com.gziolle.promptastic.util.Constants.KEY_TITLE;
import static com.gziolle.promptastic.util.Constants.PATH_SCRIPTS;
import static com.gziolle.promptastic.util.Constants.PATH_USERS;

/*
 * Allows and manages script creation and editing
 */

public class ScriptEditFragment extends Fragment {

    @BindView(R.id.et_script_title)
    EditText mScriptTitle;
    @BindView(R.id.et_script_content)
    EditText mScriptContent;
    @BindView(R.id.fab_save_script)
    FloatingActionButton mSaveScriptFab;
    @BindView(R.id.fl_progress_holder)
    FrameLayout mProgressHolder;

    private String mScriptKey;

    public interface OnScriptSavedListener {
        void onScriptSaved(Script script);
    }

    private OnScriptSavedListener mListener;

    public ScriptEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_script_edit, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();

        if (args != null) {
            String title = args.getString(KEY_TITLE);
            if (!TextUtils.isEmpty(title)) {
                mScriptTitle.setText(title);
            }
            String content = args.getString(KEY_CONTENT);
            if (!TextUtils.isEmpty(content)) {
                mScriptContent.setText(content);
            }
            mScriptKey = args.getString(KEY_DATABASE_REF);
        }
        return rootView;
    }

    /**
     * Saves the script edited by the user
     * */
    @OnClick(R.id.fab_save_script)
    public void saveScript(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }

        mProgressHolder.setVisibility(View.VISIBLE);
        final FirebaseDatabase database = Utils.getFirebaseDatabase();
        DatabaseReference ref = database.getReference();
        DatabaseReference scriptsRef;

        Script script = new Script(
                mScriptTitle.getText().toString(),
                mScriptContent.getText().toString()
        );

        if (TextUtils.isEmpty(script.getTitle()) || TextUtils.isEmpty(script.getContent())) {
            Toast.makeText(getActivity(), getString(R.string.edit_blank_error), Toast.LENGTH_SHORT).show();
        } else {
            if (mScriptKey == null) {
                scriptsRef = ref.child(PATH_USERS + FirebaseAuthManager.getInstance().getFirebaseUserId() + PATH_SCRIPTS);
                scriptsRef.push().setValue(script).addOnCompleteListener(task -> {
                    mProgressHolder.setVisibility(View.GONE);
                    mListener.onScriptSaved(script);

                });
            } else {
                scriptsRef = ref.child(PATH_USERS + FirebaseAuthManager.getInstance().getFirebaseUserId() + PATH_SCRIPTS + "/" + mScriptKey);
                scriptsRef.setValue(script).addOnCompleteListener(task -> {
                    mProgressHolder.setVisibility(View.GONE);
                    mListener.onScriptSaved(script);
                });
            }
        }
    }

    public void setOnScriptSavedListener(OnScriptSavedListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }
}
