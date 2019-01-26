package com.gziolle.promptastic.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.util.Constants;

import static android.app.Activity.RESULT_OK;


public class ScriptDetailsFragment extends Fragment {

    @BindView(R.id.tv_script_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_script_content)
    TextView mContentTextView;

    private String mTitle;
    private String mContent;
    private String mKey;

    public ScriptDetailsFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_script_details, container, false);
        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                mTitle = bundle.getString(Constants.KEY_TITLE);
                mTitleTextView.setText(mTitle);
                mContent = bundle.getString(Constants.KEY_CONTENT);
                mContentTextView.setText(mContent);
                mKey = bundle.getString(Constants.KEY_DATABASE_REF);
            }
        }
        return rootView;
    }

    @OnClick(R.id.fab_edit_script)
    public void editScript(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TITLE, mTitle);
        bundle.putString(Constants.KEY_CONTENT, mContent);
        bundle.putString(Constants.KEY_DATABASE_REF, mKey);

        Intent intent = new Intent(getActivity(), ScriptEditActivity.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, Constants.REQUEST_EDIT_SCRIPT);
    }

    @OnClick(R.id.fab_play_script)
    public void playScript(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_CONTENT, mContent);

        Intent intent = new Intent(getActivity(), PlayScriptActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
}
