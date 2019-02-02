package com.gziolle.promptastic.firebase;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.gziolle.promptastic.interfaces.FirebaseResultInterface;

import androidx.annotation.NonNull;

public class FirebaseAuthManager {

    private volatile static FirebaseAuthManager sFirebaseManager;
    private static FirebaseAuth mFirebaseAuth;

    public static FirebaseAuthManager getInstance(){
        if(sFirebaseManager == null){
            sFirebaseManager = new FirebaseAuthManager();
            mFirebaseAuth = FirebaseAuth.getInstance();
        }

        return sFirebaseManager;
    }

    public String getFirebaseUserId(){
        if(mFirebaseAuth.getCurrentUser() != null){
            return mFirebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public String getFirebaseUserName(){
        if(mFirebaseAuth.getCurrentUser() != null){
            return mFirebaseAuth.getCurrentUser().getDisplayName();
        }
        return null;
    }

    public boolean isUserLoggedIn(){
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser == null) {
            return false;
        }
        return true;
    }

    public void createUser(final Context context, String email, String password, String displayName, FirebaseResultInterface callback){
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        if(user != null){
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();

                            user.updateProfile(profileUpdates);
                            callback.onFirebaseResult(task.isSuccessful());
                        }
                    }
                });
    }

    public void loginUser(final Context context, String email, String password, FirebaseResultInterface callback){
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        callback.onFirebaseResult(task.isSuccessful());
                    }
                });
    }

    public void signOut(){
        mFirebaseAuth.signOut();
    }
}
