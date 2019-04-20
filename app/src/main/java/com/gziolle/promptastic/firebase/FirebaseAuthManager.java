/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.gziolle.promptastic.interfaces.FirebaseResultInterface;

/*
 * Manages Firebase Authentication for the entire app
 */

public class FirebaseAuthManager {

    private volatile static FirebaseAuthManager sFirebaseManager;
    private static FirebaseAuth mFirebaseAuth;

    public static FirebaseAuthManager getInstance() {
        if (sFirebaseManager == null) {
            sFirebaseManager = new FirebaseAuthManager();
            mFirebaseAuth = FirebaseAuth.getInstance();
        }

        return sFirebaseManager;
    }

    public String getFirebaseUserId() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            return mFirebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public String getFirebaseUserName() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            return mFirebaseAuth.getCurrentUser().getDisplayName();
        }
        return null;
    }

    public boolean isUserLoggedIn() {
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        return currentUser != null;
    }

    /**
     * Attempts to create a user with the information provided
     * */
    public void createUser(final Context context, String email, String password,
                           String displayName, FirebaseResultInterface callback) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdates =
                                new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName).build();

                        user.updateProfile(profileUpdates);
                    }
                    callback.onFirebaseResult(task.isSuccessful(), task.getException());
                });
    }

    /**
     * Attempts to perform a login with the user's information
     * */
    public void loginUser(final Context context, String email,
                          String password, FirebaseResultInterface callback) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task ->
                        callback.onFirebaseResult(task.isSuccessful(), task.getException()));
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }

    public Uri getUserPhotoUrl(){
        return mFirebaseAuth.getCurrentUser().getPhotoUrl();
    }

    public void setUserPhotoUri(Uri photoUri) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates =
                    new UserProfileChangeRequest.Builder()
                            .setPhotoUri(photoUri).build();
            user.updateProfile(profileUpdates);
        }
    }
}
