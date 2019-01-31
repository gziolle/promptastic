package com.gziolle.promptastic.ui;

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
import android.widget.TextView;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.util.Constants;

import static android.app.Activity.RESULT_OK;


public class ScriptDetailsFragment extends Fragment {

    @BindView(R.id.tv_script_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_script_content)
    TextView mContentTextView;

    public OnEditScriptListener mListener;

    private String mTitle;
    private String mContent;
    private String mKey;

    public interface OnEditScriptListener{
        void onEditScriptSelected(Bundle bundle);
    }

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

        Bundle args = getArguments();

        if(args != null && TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mContent)){
            mTitle = args.getString(Constants.KEY_TITLE);
            mContent = args.getString(Constants.KEY_CONTENT);
            mKey = args.getString(Constants.KEY_DATABASE_REF);
        }

        return rootView;
    }

    @OnClick(R.id.fab_edit_script)
    public void editScript(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TITLE, mTitle);
        bundle.putString(Constants.KEY_CONTENT, mContent);
        bundle.putString(Constants.KEY_DATABASE_REF, mKey);
        mListener.onEditScriptSelected(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTitleTextView.setText(mTitle);
        mContentTextView.setText(mContent);
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

    public void setOnEditScriptListener(OnEditScriptListener listener){
        if(listener != null){
            mListener = listener;
        }
    }

    public void setText(String title, String content){
        mTitle = title;
        mContent = content;
    }
}
