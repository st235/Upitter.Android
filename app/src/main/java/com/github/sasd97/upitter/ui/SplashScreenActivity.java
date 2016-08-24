package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.sasd97.upitter.holders.CompanyHolder;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.holders.UserHolder;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.Upitter.setHolder;
import static com.github.sasd97.upitter.holders.UserHolder.isUserAvailable;

/**
 * Created by Alexadner Dadukin on 24.08.2016.
 */

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    @Override
    protected void setupViews() {
        if (isUserAvailable()) {
            initUser();
            return;
        }

        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
        finish();
    }

    private void initUser() {
        Class<?> target;

        switch (UserModel.UserType.getTypeByValue(UserHolder.getUserType())) {
            case People:
                setHolder(PeopleHolder.getHolder());
                target = PeopleFeedActivity.class;
                break;
            case Company:
                setHolder(CompanyHolder.getHolder());
                target = CompanyFeedActivity.class;
                break;
            default:
                setHolder(PeopleHolder.getHolder());
                target = PeopleFeedActivity.class;
                break;
        }

        getHolder().restore();

        Intent intent = new Intent(this, target);
        startActivity(intent);
        finish();
    }
}
