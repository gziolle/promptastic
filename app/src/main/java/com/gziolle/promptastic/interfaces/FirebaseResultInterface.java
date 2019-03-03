/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.interfaces;

/*
 * Interface to manage results sent from Firebase Realtime Database Servers
 */

public interface FirebaseResultInterface {

    void onFirebaseResult(boolean isSuccessful, Exception exception);
}
