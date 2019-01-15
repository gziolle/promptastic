package com.gziolle.promptastic.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gziolle.promptastic.R;

public class ScriptEditFragment extends Fragment {

    @BindView(R.id.et_script_title)
    EditText mScriptTitle;
    @BindView(R.id.et_script_content)
    EditText mScriptContent;
    @BindView(R.id.fab_save_script)
    FloatingActionButton mSaveScriptFab;

    public ScriptEditFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_script_edit, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.fab_save_script)
    public void saveScript(View view) {
        //TODO: send data to firebase.
    }
}
