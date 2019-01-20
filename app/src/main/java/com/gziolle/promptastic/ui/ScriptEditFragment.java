package com.gziolle.promptastic.ui;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.util.Constants;
import com.gziolle.promptastic.util.Utils;

import static com.gziolle.promptastic.util.Constants.KEY_CONTENT;
import static com.gziolle.promptastic.util.Constants.KEY_DATABASE_REF;
import static com.gziolle.promptastic.util.Constants.KEY_TITLE;
import static com.gziolle.promptastic.util.Constants.PATH_SCRIPTS;
import static com.gziolle.promptastic.util.Constants.PATH_USERS;

public class ScriptEditFragment extends Fragment {

    @BindView(R.id.et_script_title)
    EditText mScriptTitle;
    @BindView(R.id.et_script_content)
    EditText mScriptContent;
    @BindView(R.id.fab_save_script)
    FloatingActionButton mSaveScriptFab;

    private String mScriptKey;

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
        View rootView =  inflater.inflate(R.layout.fragment_script_edit, container, false);
        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();

        if(intent != null){
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(KEY_TITLE);
            if(!TextUtils.isEmpty(title)){
                mScriptTitle.setText(title);
            }
            String content = bundle.getString(KEY_CONTENT);
            if(!TextUtils.isEmpty(content)){
                mScriptContent.setText(content);
            }
            mScriptKey = bundle.getString(KEY_DATABASE_REF);
        }
        return rootView;
    }

    @OnClick(R.id.fab_save_script)
    public void saveScript(View view) {
        final FirebaseDatabase database = Utils.getFirebaseDatabase();
        DatabaseReference ref = database.getReference();

        Script script = new Script(
                mScriptTitle.getText().toString(),
                mScriptContent.getText().toString()
        );

        DatabaseReference scriptsRef;
        if(mScriptKey == null){
            scriptsRef = ref.child(PATH_USERS + FirebaseAuthManager.getInstance().getFirebaseUserId() + PATH_SCRIPTS);
            scriptsRef.push().setValue(script);
        } else{
            scriptsRef = ref.child(PATH_USERS + FirebaseAuthManager.getInstance().getFirebaseUserId() + PATH_SCRIPTS + "/" + mScriptKey);
            scriptsRef.setValue(script);

            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.KEY_TITLE, script.getTitle());
            returnIntent.putExtra(Constants.KEY_CONTENT, script.getContent());
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
        }

        if(getActivity() != null) {
            getActivity().finish();
        }
    }
}
