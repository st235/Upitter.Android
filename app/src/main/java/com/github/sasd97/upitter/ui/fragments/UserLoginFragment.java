package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.UserHolder;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.user.UserResponseModel;
import com.github.sasd97.upitter.services.query.SocialAuthorizationQueryService;
import com.github.sasd97.upitter.ui.TapeActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Authorization;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Arrays;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.GOOGLE_SIGN_IN_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.TWITTER_SIGN_IN_REQUEST;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public class UserLoginFragment extends BaseFragment
        implements GoogleApiClient.OnConnectionFailedListener,
        FacebookCallback<LoginResult>,
        SocialAuthorizationQueryService.OnSocialAuthorizationListener {

    private GoogleApiClient googleClient;
    private SocialAuthorizationQueryService service = SocialAuthorizationQueryService.getService(this);

    private Button signFacebookButton;
    private Button signGoogleButton;
    private Button signTwitterButton;

    public static UserLoginFragment getFragment() {
        return new UserLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        googleClient = Authorization.google(getContext(), getActivity(), this);
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
                        exception.printStackTrace();
                        showErrorSnackbar();
                    }
                });
            }
        });

        signFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(UserLoginFragment.this, Arrays.asList("public_profile", "email"));
            }
        });

        signGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = Auth.GoogleSignInApi.getSignInIntent(googleClient);
                startActivityForResult(signIn, GOOGLE_SIGN_IN_REQUEST);
            }
        });
    }

    @Override
    protected void bindViews() {
        signGoogleButton = findById(R.id.google_plus_button_user_login_fragment);
        signFacebookButton = findById(R.id.facebook_button_user_login_fragment);
        signTwitterButton = findById(R.id.twitter_button_user_login_fragment);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar
                .make(getView(), getString(R.string.error_authorization_login_activity), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        service.notifyByFacebook(loginResult.getAccessToken().getToken());
    }

    @Override
    public void onCancel() {
        Snackbar
                .make(getView(), getString(R.string.cancel_authorization_login_activity), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onError(FacebookException error) {
        showErrorSnackbar();
    }

    @Override
    public void onServerNotify(UserResponseModel userResponseModel) {
        UserModel.Builder builder = new UserModel
                .Builder()
                .nickname(userResponseModel.getNickname())
                .sex(userResponseModel.getSex())
                .isVerify(userResponseModel.isVerify())
                .accessToken(userResponseModel.getToken());

        if (userResponseModel.isField(userResponseModel.getName())) builder.name(userResponseModel.getName());
        if (userResponseModel.isField(userResponseModel.getSurname())) builder.surname(userResponseModel.getSurname());
        if (userResponseModel.isField(userResponseModel.getDescription())) builder.description(userResponseModel.getDescription());
        if (userResponseModel.isField(userResponseModel.getAvatarUrl())) builder.avatarUrl(userResponseModel.getAvatarUrl());
        if (userResponseModel.isField(userResponseModel.getEmail())) builder.email(userResponseModel.getEmail());

        UserHolder.save(builder.build());

        Snackbar
                .make(getView(), getString(R.string.authorized_successfully_login_activity), Snackbar.LENGTH_LONG)
                .show();

        startActivity(new Intent(getActivity(), TapeActivity.class));
    }

    @Override
    public void onNotifyError(int code, String message) {
        Log.d("CODE", String.valueOf(code));
        showErrorSnackbar();
    }

    private void showErrorSnackbar() {
        Snackbar
                .make(getView(), getString(R.string.error_authorization_login_activity), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TWITTER_SIGN_IN_REQUEST) {
            Authorization.twitter().onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            Authorization.facebook().onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (requestCode == GOOGLE_SIGN_IN_REQUEST && resultCode == FragmentActivity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) Authorization.obtainGoogle(service, result);
            else showErrorSnackbar();
            return;
        }
    }
}
