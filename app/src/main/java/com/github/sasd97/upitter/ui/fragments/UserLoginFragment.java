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

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.CountryCodeChooseActivity;
import com.github.sasd97.upitter.ui.LoginActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Authorization;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.GOOGLE_SIGN_IN_REQUEST;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public class UserLoginFragment extends BaseFragment
        implements GoogleApiClient.OnConnectionFailedListener {

    private Button signGoogleButton;
    private GoogleApiClient client;

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
        client = Authorization.obtainClient(getContext(), getActivity(), this);

        signGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = Auth.GoogleSignInApi.getSignInIntent(client);
                getActivity().startActivityForResult(signIn, GOOGLE_SIGN_IN_REQUEST);
            }
        });
    }

    @Override
    protected void bindViews() {
        signGoogleButton = findById(R.id.google_plus_button_user_login_fragment);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Connection lost", Toast.LENGTH_SHORT).show();
    }
}
