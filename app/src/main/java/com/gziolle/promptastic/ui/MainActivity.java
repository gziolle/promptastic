package com.gziolle.promptastic.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;

public class MainActivity extends AppCompatActivity implements ScriptListFragment.OnScriptSelectedListener, ScriptListFragment.OnAddScriptListener,
    ScriptDetailsFragment.OnEditScriptListener, ScriptEditFragment.OnScriptSavedListener{

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mDrawerList;

    @BindView(R.id.view_container)
    FrameLayout mViewContainer;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ActionBarDrawerToggle mDrawerToggle;

    private static final String DETAILS_FRAGMENT_TAG = "details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.sign_out:
                        FirebaseAuthManager.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                }
                return false;
            }
        });

        if(mViewContainer != null){
            if(savedInstanceState != null){
                //No need to create new fragments;
                return;
            }
            ScriptListFragment listFragment = new ScriptListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.view_container, listFragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if(fragment instanceof ScriptListFragment){
            ScriptListFragment scriptListFragment = (ScriptListFragment) fragment;
            scriptListFragment.setOnScriptSelectedListener(this);
            scriptListFragment.setOnAddScriptListerner(this);
        } else if(fragment instanceof ScriptDetailsFragment){
            ScriptDetailsFragment scriptDetailsFragment = (ScriptDetailsFragment) fragment;
            scriptDetailsFragment.setOnEditScriptListener(this);
        } else if(fragment instanceof ScriptEditFragment){
            ScriptEditFragment scriptEditFragment = (ScriptEditFragment) fragment;
            scriptEditFragment.setOnScriptSavedListener(this);
        }
    }

    @Override
    public void onScriptSelected(Bundle bundle) {
        ScriptDetailsFragment fragment = new ScriptDetailsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.view_container, fragment, DETAILS_FRAGMENT_TAG);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onAddScriptButtonSelected() {
        ScriptEditFragment fragment = new ScriptEditFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.view_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onEditScriptSelected(Bundle bundle) {
        ScriptEditFragment fragment = new ScriptEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.view_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onScriptSaved(Script script) {
        ScriptDetailsFragment detailsFragment = (ScriptDetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG);
        if(detailsFragment != null){
            detailsFragment.setText(script.getTitle(), script.getContent());
        }
        getSupportFragmentManager().popBackStack();
    }
}
