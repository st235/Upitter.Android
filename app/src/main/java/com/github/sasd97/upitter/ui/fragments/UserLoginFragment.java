package com.github.sasd97.upitter.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseFragment;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public class UserLoginFragment extends BaseFragment {

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
    }
}
