package com.github.sasd97.upitter.constants;

/**
 * Created by Alex on 10.06.2016.
 */

public interface RequestCodesConstants {

    int GOOGLE_SIGN_IN_REQUEST = 97;
    int FACEBOOK_SIGN_IN_REQUEST = 98;
    int TWITTER_SIGN_IN_REQUEST = 140;

    int COUNTRY_CHOOSE_LIST_REQUEST = 1000;
    int GALLERY_ACTIVITY_REQUEST = 1001;
    int CATEGORIES_ACTIVITY_REQUEST = 1002;
    int CHOOSE_ON_MAP_POINT_REQUEST = 1003;
    int SHOW_ON_MAP_POINT_REQUEST = 1004;
    int CREATE_QUIZ_REQUEST = 1005;
    int LOCATION_CHANGE_REQUEST = 1006;

    String CODE_RECEIVER_INTENT_NAME = "com.github.sasd97.recievers.HANDLE_SMS_CODE";
}
