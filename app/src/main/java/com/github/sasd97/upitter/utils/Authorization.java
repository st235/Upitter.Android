package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.facebook.FacebookSdk;
import com.github.sasd97.upitter.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Alex on 10.06.2016.
 */

public class Authorization {

    private static GoogleSignInOptions googleSignInOptions;
    private static GoogleApiClient client;

    private Authorization() {}

    public static void init(Context context) {
        FacebookSdk.sdkInitialize(context);
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(context.getString(R.string.twitter_consumer_key),
                context.getString(R.string.twitter_consumer_secret));
        Fabric.with(context, new TwitterCore(authConfig));

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_server_client_id))
                .requestEmail()
                .build();
    }

    public static GoogleApiClient obtainClient(@NonNull Context context,
                                               @NonNull FragmentActivity activity,
                                               @NonNull GoogleApiClient.OnConnectionFailedListener listener) {
        return new GoogleApiClient.Builder(context)
                .enableAutoManage(activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }
}
