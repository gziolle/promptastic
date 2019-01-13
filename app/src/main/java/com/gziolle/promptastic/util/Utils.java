package com.gziolle.promptastic.util;

import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getFirebaseDatabase(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }
}
