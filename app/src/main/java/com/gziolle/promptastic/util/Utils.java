/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.util;

import android.content.Context;
import android.os.Environment;

import com.google.firebase.database.FirebaseDatabase;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Holds useful methods for the app
 */

public class Utils {

    private static FirebaseDatabase mDatabase;

    /**
     * Returns the firebase Realtime Database instance
     * */
    public static FirebaseDatabase getFirebaseDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            mDatabase.getReference(Constants.PATH_USERS +
                    FirebaseAuthManager.getInstance().getFirebaseUserId()).keepSynced(true);
        }
        return mDatabase;
    }

    String currentPhotoPath;

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "profile_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
