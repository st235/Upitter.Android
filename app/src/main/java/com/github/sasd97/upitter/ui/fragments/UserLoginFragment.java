package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.services.query.AuthorizationQueryService;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Authorization;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Arrays;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.GOOGLE_SIGN_IN_REQUEST;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public class UserLoginFragment extends BaseFragment
        implements GoogleApiClient.OnConnectionFailedListener,
        FacebookCallback<LoginResult> {

    private Button signFacebookButton;
    private Button signGoogleButton;
    private Button signTwitterButton;

    private GoogleApiClient client;
    private AuthorizationQueryService service;

    public static UserLoginFragment getFragment(AuthorizationQueryService service) {
        UserLoginFragment fragment = new UserLoginFragment();
        fragment.setAuthorizationService(service);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = Authorization.google(getContext(), getActivity(), this);
        LoginManager.getInstance().registerCallback(Authorization.facebook(), this);

        signTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authorization.twitter().authorize(getActivity(), new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        service.notifyByTwitter(result.data.getAuthToken().token, result.data.getAuthToken().secret);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
            }
        });

        signFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
            }
        });

        signGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = Auth.GoogleSignInApi.getSignInIntent(client);
                getActivity().startActivityForResult(signIn, GOOGLE_SIGN_IN_REQUEST);
            }
        });
    }

    private void setAuthorizationService(AuthorizationQueryService service) {
        this.service = service;
    }

    @Override
    protected void bindViews() {
        signGoogleButton = findById(R.id.google_plus_button_user_login_fragment);
        signFacebookButton = findById(R.id.facebook_button_user_login_fragment);
        signTwitterButton = findById(R.id.twitter_button_user_login_fragment);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Connection lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        service.notifyByFacebook(loginResult.getAccessToken().getToken());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }
}
