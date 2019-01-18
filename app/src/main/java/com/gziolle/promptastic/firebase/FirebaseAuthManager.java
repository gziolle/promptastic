package com.gziolle.promptastic.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gziolle.promptastic.ui.MainActivity;

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

    public boolean isUserLoggedIn(){
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser == null) {
            return false;
        }
        return true;
    }

    public void loginUser(final Context context, String email, String password){
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Failed to login", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent profileIntent = new Intent(context, MainActivity.class);
                            context.startActivity(profileIntent);
                        }
                    }
                });
    }

    public void signOut(){
        mFirebaseAuth.signOut();
    }
}
