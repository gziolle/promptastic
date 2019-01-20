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

import static com.gziolle.promptastic.util.Constants.KEY_CONTENT;
import static com.gziolle.promptastic.util.Constants.KEY_TITLE;

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
        Bundle bundle = intent.getExtras();
        mTitle = bundle.getString(KEY_TITLE);
        mTitleTextView.setText(mTitle);
        mContent = bundle.getString(KEY_CONTENT);
        mContentTextView.setText(mContent);

        return rootView;
    }

    @OnClick(R.id.fab_edit_script)
    public void editScript(){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, mTitle);
        bundle.putString(KEY_CONTENT, mContent);

        Intent intent = new Intent(getActivity(), ScriptEditActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
