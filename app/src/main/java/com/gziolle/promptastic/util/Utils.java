/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.util;

import com.google.firebase.database.FirebaseDatabase;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;

/*
 * Holds useful methods for the app
 */

public class Utils {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getFirebaseDatabase(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            mDatabase.getReference(Constants.PATH_USERS + FirebaseAuthManager.getInstance().getFirebaseUserId()).keepSynced(true);
        }
        return mDatabase;
    }
}
