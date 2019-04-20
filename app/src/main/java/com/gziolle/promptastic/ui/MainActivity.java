/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.firebase.FirebaseStorageManager;
import com.gziolle.promptastic.interfaces.FirebaseStorageResultInterface;
import com.gziolle.promptastic.util.Constants;
import com.gziolle.promptastic.util.Utils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/*
 * Handles Fragments transactions, callbacks and activity routes
 */

public class MainActivity extends AppCompatActivity implements
        ScriptListFragment.OnScriptSelectedListener, ScriptListFragment.OnAddScriptListener,
        ScriptDetailsFragment.OnScriptListener, ScriptEditFragment.OnScriptSavedListener,
        FirebaseStorageResultInterface {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mDrawerList;

    @BindView(R.id.view_container)
    FrameLayout mViewContainer;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mDisplayNameTextView;
    private ImageView mHeaderPhoto;

    private static final String DETAILS_FRAGMENT_TAG = "details";

    private boolean mTwoPane;
    private Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.script_detail_container) == null) {
            mTwoPane = false;
            fragmentManager.addOnBackStackChangedListener(() -> {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    setToolbarAsUp(v -> getSupportFragmentManager().popBackStack());
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    mDrawerToggle = new ActionBarDrawerToggle(this,
                            mDrawerLayout, mToolbar, R.string.open, R.string.close);
                    mDrawerLayout.addDrawerListener(mDrawerToggle);
                    mDrawerToggle.syncState();
                }
            });
            if (mViewContainer != null) {
                if (savedInstanceState != null) {
                    //No need to create new fragments;
                    return;
                }
                ScriptListFragment listFragment = new ScriptListFragment();
                fragmentManager.beginTransaction().add(R.id.view_container, listFragment).commit();
            }
        } else {
            //Two Pane mode
            mTwoPane = true;
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean(Constants.IS_TWO_PANE, mTwoPane);
            editor.commit();
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_script_list, new ScriptListFragment())
                        .replace(R.id.script_detail_container, new EmptyViewFragment()).commit();
            }
        }

        mDrawerList.setNavigationItemSelectedListener(item -> {
            Intent intent;
            int id = item.getItemId();
            switch (id) {
                case R.id.scripts:
                    break;
                case R.id.settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.about:
                    intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sign_out:
                    FirebaseAuthManager.getInstance().signOut();
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
            }
            return false;
        });

        View headerLayout = mDrawerList.getHeaderView(0);
        mDisplayNameTextView = headerLayout.findViewById(R.id.tv_display_name);
        if (mDisplayNameTextView != null) {
            String displayName = FirebaseAuthManager.getInstance().getFirebaseUserName();
            if (!TextUtils.isEmpty(displayName)) {
                mDisplayNameTextView.setText(displayName);
            }
        }

        mHeaderPhoto = headerLayout.findViewById(R.id.iv_header_photo);
        if(mHeaderPhoto != null){
            Uri photoUri = FirebaseAuthManager.getInstance().getUserPhotoUrl();
            if(photoUri != null){
                Glide.with(this).load(photoUri).into(mHeaderPhoto);
            }
            mHeaderPhoto.setOnClickListener(v -> {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            Constants.CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    pickPhotoFromDevice();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof ScriptListFragment) {
            ScriptListFragment scriptListFragment = (ScriptListFragment) fragment;
            scriptListFragment.setOnScriptSelectedListener(this);
            scriptListFragment.setOnAddScriptListerner(this);
        } else if (fragment instanceof ScriptDetailsFragment) {
            ScriptDetailsFragment scriptDetailsFragment = (ScriptDetailsFragment) fragment;
            scriptDetailsFragment.setOnEditScriptListener(this);
        } else if (fragment instanceof ScriptEditFragment) {
            ScriptEditFragment scriptEditFragment = (ScriptEditFragment) fragment;
            scriptEditFragment.setOnScriptSavedListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(GravityCompat.START, false);
        }

        if (!mTwoPane) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                setToolbarAsUp(v -> getSupportFragmentManager().popBackStack());
            }
        }
    }

    @Override
    public void onScriptSelected(Bundle bundle) {
        ScriptDetailsFragment fragment = new ScriptDetailsFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right_transition, R.anim.exit_to_left_transition,
                R.anim.enter_from_left_transition, R.anim.exit_to_right_transition);
        if (!mTwoPane) {
            transaction.replace(R.id.view_container, fragment, DETAILS_FRAGMENT_TAG);
            transaction.addToBackStack(null);
        } else {
            transaction.replace(R.id.script_detail_container, fragment, DETAILS_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    @Override
    public void onAddScriptButtonSelected() {
        ScriptEditFragment fragment = new ScriptEditFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom_transition, R.anim.exit_to_top_transition,
                R.anim.enter_from_top_transition, R.anim.exit_to_bottom_transition);
        if (!mTwoPane) {
            transaction.replace(R.id.view_container, fragment);
            transaction.addToBackStack(null);
        } else {
            transaction.replace(R.id.script_detail_container, fragment);
        }
        transaction.commit();
    }

    @Override
    public void onEditScriptSelected(Bundle bundle) {
        ScriptEditFragment fragment = new ScriptEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom_transition, R.anim.exit_to_top_transition,
                R.anim.enter_from_top_transition, R.anim.exit_to_bottom_transition);
        if (!mTwoPane) {
            transaction.replace(R.id.view_container, fragment);
            transaction.addToBackStack(null);
        } else {
            transaction.replace(R.id.script_detail_container, fragment);
        }
        transaction.commit();
    }

    @Override
    public void onDeleteScript(Boolean isSuccessful) {
        Snackbar snackbar;
        if(isSuccessful){
            getSupportFragmentManager().popBackStack();
            snackbar = Snackbar.make(
                    mCoordinatorLayout, getString(R.string.script_deleted), Snackbar.LENGTH_SHORT);
            if (mTwoPane) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.script_detail_container, new EmptyViewFragment()).commit();
            }
        }else {
            snackbar = Snackbar.make(
                    mCoordinatorLayout, getString(R.string.script_not_saved),
                    Snackbar.LENGTH_SHORT);
        }

        snackbar.show();

    }

    @Override
    public void onScriptSaved(Script script) {
        ScriptDetailsFragment detailsFragment =
                (ScriptDetailsFragment) getSupportFragmentManager()
                        .findFragmentByTag(DETAILS_FRAGMENT_TAG);
        if (detailsFragment != null) {
            detailsFragment.setText(script.getTitle(), script.getContent());
            if (mTwoPane) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.script_detail_container, detailsFragment).commit();
            }
        }

        if (!mTwoPane) {
            getSupportFragmentManager().popBackStack();
        }
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.script_saved), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(GravityCompat.START, true);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case Constants.CAMERA_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickPhotoFromDevice();
                } else{
                    Snackbar.make(mCoordinatorLayout, getString(R.string.permission_denied), Snackbar.LENGTH_SHORT).show();;
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case Constants.CAMERA_REQUEST_CODE:
                String destinationFileName = "cropped_profile";
                UCrop uCrop = UCrop.of(mPhotoUri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
                uCrop.start(MainActivity.this);
                break;
            case UCrop.REQUEST_CROP:

                if(resultCode == RESULT_OK){
                    final Uri croppedPhotoUri = UCrop.getOutput(data);
                    FirebaseStorageManager manager = new FirebaseStorageManager(this);
                    manager.saveUserPhotoToFirebase(
                            FirebaseAuthManager.getInstance().getFirebaseUserId(),
                            croppedPhotoUri,
                            this);
                    Snackbar.make(mCoordinatorLayout, getString(R.string.profile_photo_soon), Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mCoordinatorLayout, Constants.PLEASE_TRY_AGAIN_LATER, Snackbar.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * Sets the toolbar left button as a "back" button
     * */
    protected void setToolbarAsUp(View.OnClickListener clickListener) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
            mToolbar.setNavigationOnClickListener(clickListener);
        }
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }

    private void pickPhotoFromDevice(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            File profilePhotoFile = null;

            try{
                profilePhotoFile = Utils.createImageFile(this);
            } catch(IOException ex){
                Snackbar.make(mCoordinatorLayout, ex.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            if(profilePhotoFile != null){
                mPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY,
                        profilePhotoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onFirebaseStorageResult(Uri downloadUri) {
        FirebaseAuthManager.getInstance().setUserPhotoUri(downloadUri);
        Glide.with(this).load(downloadUri).into(mHeaderPhoto);
        Snackbar.make(mCoordinatorLayout, getString(R.string.profile_photo_added), Snackbar.LENGTH_SHORT).show();
    }
}
