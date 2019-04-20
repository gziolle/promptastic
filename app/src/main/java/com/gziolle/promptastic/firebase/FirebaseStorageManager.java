/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.firebase;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gziolle.promptastic.interfaces.FirebaseStorageResultInterface;
import com.gziolle.promptastic.util.Constants;

public class FirebaseStorageManager {

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private Context mContext;

    public FirebaseStorageManager(Context context){
        this.mContext = context;
        initManager();
    }

    private void initManager(){
        this.mFirebaseStorage = FirebaseStorage.getInstance();
        this.mStorageReference = mFirebaseStorage.getReferenceFromUrl(Constants.STORAGE_BASE_URL);
    }

    public void saveUserPhotoToFirebase(String userId, Uri imageUri,
                                        FirebaseStorageResultInterface callback){
        StorageReference photoParentRef = mStorageReference.child(userId);
        StorageReference photoRef = photoParentRef.child(imageUri.getLastPathSegment());
        photoRef.putFile(imageUri).continueWithTask(
                task -> {
                    if(!task.isSuccessful()){
                        callback.onFirebaseStorageResult(null);
                    }
                    return photoRef.getDownloadUrl();
                }
        ).addOnSuccessListener(uri -> callback.onFirebaseStorageResult(uri));
    }
}
