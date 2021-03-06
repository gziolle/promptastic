/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.util;

/*
 * Holds all constants used by the app
 */

public class Constants {

    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DATABASE_REF = "database_ref";
    public static final String KEY_SCRIPT_EXTRA = "script";

    public static final String PATH_USERS = "users/";
    public static final String PATH_SCRIPTS = "/scripts";

    public static final int REQUEST_EDIT_SCRIPT = 101;

    public static final int COUNTDOWN_TOTAL_TIME = 3000;
    public static final int COUNTDOWN_TIME_INTERVAL = 1000;
    public static final String PLAY_SCRIPT_DEFAULT_TEXT_SIZE = "40";

    public static final String PLAY_SCRIPT_VERY_SLOW_DURATION = "200000";
    public static final String PLAY_SCRIPT_SLOW_DURATION = "150000";
    public static final String PLAY_SCRIPT_DEFAULT_DURATION = "100000";
    public static final String PLAY_SCRIPT_FAST_DURATION = "75000";
    public static final String PLAY_SCRIPT_VERY_FAST_DURATION = "50000";

    public static final String PLAY_SCRIPT_THEME_LIGHT = "light";
    public static final String PLAY_SCRIPT_THEME_DARK = "dark";

    public static final String ACTION_UPDATE_WIDGET = "com.gziolle.promptastic.UPDATE_WIDGET";
    public static final int NAME_MAX_LENGTH = 60;

    public static final String SELECTED_ITEM = "selectedItem";
    public static final String IS_TWO_PANE = "isTwoPane";
    public static final String SELECTED_KEY = "mSelectedKey";

    public static final String FIRST_USE_KEY = "first_key";

    public static final String STORAGE_BASE_URL = "gs://promptastic-88819.appspot.com";

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 102;
    public static final int CAMERA_REQUEST_CODE = 12;

    public static final String PLEASE_TRY_AGAIN_LATER = "Please, try again later";
    public static final String PROFILE_PHOTO_ADDED = "Profile Photo added!";
    public static final String FILE_PROVIDER_AUTHORITY = "com.gziolle.promptastic.fileprovider";
}
