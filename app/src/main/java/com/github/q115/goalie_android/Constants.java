package com.github.q115.goalie_android;

/*
 * Copyright 2017 Qi Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Constants {
    public static final String URL = BuildConfig.DEBUG ? "http://104.197.166.11:8080" : "https://goalie.es";
    public static final String KEY = BuildConfig.DEBUG ? "xxx" : "xxxx";
    public static final String CUSTOM_KEY = BuildConfig.DEBUG ? "CUSTOM_KEY" : "CUSTOM_KEY_PROD";
    public static final String PREFERENCE_FILE_NAME = "PREFERENCE_FILE_NAME";

    public static final String FAILED_TO_CONNECT = "Connection failure, please ensure internet connection is active.";
    public static final String FAILED_TO_SEND = "Failed to reach server, please try again.";
    public static final String FAILED = "Failed";

    public static final int MAX_USERNAME_LENGTH = 30;

    //profile image sizes
    public static final int IMAGE_JPG_QUALITY = 99;
    public static final int PROFILE_ROW_SIZE = 75; // dp
    public static final int PROFILE_IMAGE_WIDTH = 600; // px
    public static final int PROFILE_IMAGE_HEIGHT = 600;
    public static final float ROUNDED_PROFILE = 10f; // the bigger the value the more square the image
    public static final float CIRCLE_PROFILE = 2f; // 2 = circle, the bigger the value the more square the image

    public static final int ASYNC_CONNECTION_NORMAL_TIMEOUT = 9 * 1000; // 9 seconds
    public static final int ASYNC_CONNECTION_EXTENDED_TIMEOUT = 16 * 1000; // 16 seconds

    public static final int RESULT_PROFILE_IMAGE_SELECTED = 10;
    public static final int RESULT_PROFILE_IMAGE_TAKEN = 11;
    public static final int RESULT_PROFILE_UPDATE = 12;
    public static final int RESULT_GOAL_SET = 30;
    public static final int RESULT_MY_GOAL_DIALOG = 40;

    public static final int REQUEST_PERMISSIONS_CAMERA_STORAGE = 100;
    public static final int REQUEST_PERMISSIONS_CONTACT = 102;
    public static final int REQUEST_PERMISSIONS_ALARM = 110;
    public static final int REQUEST_PERMISSIONS_NOTIFICATIONS = 120;

    // notification
    public static final int ID_NOTIFICATION_BROADCAST = 607;
}
