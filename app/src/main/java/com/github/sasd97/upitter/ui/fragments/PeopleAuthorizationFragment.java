package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.models.response.pointers.UserPointerModel;
import com.github.sasd97.upitter.services.query.UserAuthorizationQueryService;
import com.github.sasd97.upitter.ui.PeopleFeedActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.services.AuthorizationService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orhanobut.logger.Logger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Arrays;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.GOOGLE_SIGN_IN_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.TWITTER_SIGN_IN_REQUEST;
import static com.github.sasd97.upitter.Upitter.*;


/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public class PeopleAuthorizationFragment extends BaseFragment
        implements GoogleApiClient.OnConnectionFailedListener,
        FacebookCallback<LoginResult>,
        UserAuthorizationQueryService.OnSocialAuthorizationListener {

    private GoogleApiClient googleClient;
    private UserAuthorizationQueryService service = UserAuthorizationQueryService.getService(this);

    @BindView(R.id.facebook_button_user_login_fragment) Button signFacebookButton;
    @BindView(R.id.google_plus_button_user_login_fragment) Button signGoogleButton;
    @BindView(R.id.twitter_button_user_login_fragment) Button signTwitterButton;

    public PeopleAuthorizationFragment() {
        super(R.layout.fragment_user_authorization);
    }

    public static PeopleAuthorizationFragment getFragment() {
        return new PeopleAuthorizationFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] facebookScope = getResources().getStringArray(R.array.facebook_app_scope);
        googleClient = AuthorizationService.google(getContext(), getActivity(), this);
        LoginManager.getInstance().registerCallback(AuthorizationService.facebook(), this);

        signTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationService.twitter().authorize(getActivity(), new Callback<TwitterSession>() {
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
                LoginManager.getInstance().logInWithReadPermissions(PeopleAuthorizationFragment.this, Arrays.asList(facebookScope));
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
    protected void setupViews() {
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
    public void onServerNotify(UserPointerModel userPointerModel) {
        Logger.json(userPointerModel.toString());

        PeopleModel.Builder builder = new PeopleModel
                .Builder()
                .id(userPointerModel.getId())
                .nickname(userPointerModel.getNickname())
                .sex(userPointerModel.getSex())
                .isVerify(userPointerModel.isVerify())
                .accessToken(userPointerModel.getToken());

        if (userPointerModel.getName() != null) builder.name(userPointerModel.getName());
        if (userPointerModel.getSurname() != null) builder.surname(userPointerModel.getSurname());
        if (userPointerModel.getDescription() != null) builder.description(userPointerModel.getDescription());
        if (userPointerModel.getAvatarUrl() != null) builder.avatarUrl(userPointerModel.getAvatarUrl());
        if (userPointerModel.getEmail() != null) builder.email(userPointerModel.getEmail());

        setHolder(PeopleHolder.getHolder());
        getHolder().save(builder.build());

        Snackbar
                .make(getView(), getString(R.string.authorized_successfully_login_activity), Snackbar.LENGTH_LONG)
                .show();

        startActivity(new Intent(getActivity(), PeopleFeedActivity.class));
    }

    @Override
    public void onError(ErrorModel errorModel) {
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
            AuthorizationService.twitter().onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            AuthorizationService.facebook().onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (requestCode == GOOGLE_SIGN_IN_REQUEST && resultCode == FragmentActivity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) AuthorizationService.obtainGoogle(service, result);
            else showErrorSnackbar();
        }
    }
}
