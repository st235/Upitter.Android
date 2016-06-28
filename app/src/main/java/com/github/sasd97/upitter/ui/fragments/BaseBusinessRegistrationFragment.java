package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.PhonesRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesActivity;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.Names;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.PUT_CROPPED_IMAGE;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.GALLERY_ACTIVITY_REQUEST;

/**
 * Created by Alexadner Dadukin on 24.06.2016.
 */
public class BaseBusinessRegistrationFragment extends BaseFragment {

    private ArrayList<Integer> categoriesSelected;
    private ArrayList<String> contactPhones;

    private ImageView avatarImageView;
    private LinearLayout avatarLayout;
    private RelativeLayout categoriesLayout;

    private RelativeLayout addPhoneLayout;
    private RecyclerView phonesRecyclerView;
    private PhonesRecyclerAdapter phonesRecyclerAdapter;

    public static BaseBusinessRegistrationFragment getFragment() {
        return new BaseBusinessRegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.business_registration_base_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        avatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarChooseClick();
            }
        });
        categoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryChooseClick();
            }
        });
        addPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPhoneClick();
            }
        });

        phonesRecyclerAdapter = new PhonesRecyclerAdapter();
        phonesRecyclerView.setHasFixedSize(true);
        phonesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phonesRecyclerView.setAdapter(phonesRecyclerAdapter);

        ItemTouchHelper.SimpleCallback
                simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                phonesRecyclerAdapter.removePhone(viewHolder.getAdapterPosition());
                phonesRecyclerView.setLayoutParams(lp);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(phonesRecyclerView);
    }

    @Override
    protected void bindViews() {
        avatarImageView = findById(R.id.avatar_business_registration_base_fragment);
        avatarLayout = findById(R.id.avatar_url_business_registration_base_fragment);
        categoriesLayout = findById(R.id.categories_choose_business_registration_base_fragment);
        phonesRecyclerView = findById(R.id.phones_recyclerview_registration_base_fragment);
        addPhoneLayout = findById(R.id.add_phone_button_registration_base_fragment);
    }

    public void onAddPhoneClick() {
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        phonesRecyclerAdapter.addPhone();
        phonesRecyclerView.setLayoutParams(lp);
    }

    public void onAvatarChooseClick() {
        startActivityForResult(new Gallery.Builder()
                .from(getContext())
                .multiSelectionMode(false)
                .build(), GALLERY_ACTIVITY_REQUEST);
    }

    public void onCategoryChooseClick() {
        startActivityForResult(new Intent(getActivity(), CategoriesActivity.class), CATEGORIES_ACTIVITY_REQUEST);
    }

    private void handleAvatarIntent(@NonNull Intent intent) {
        String path = intent.getStringExtra(PUT_CROPPED_IMAGE);

        Glide
                .with(this)
                .load(Names
                        .getInstance()
                        .getFilePath(path)
                        .toString())
                .bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), Dimens.dpToPx(4), 0))
                .into(avatarImageView);
    }

    private void handleCategoriesIntent(@NonNull Intent intent) {
        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;
        if (requestCode == GALLERY_ACTIVITY_REQUEST) {
            handleAvatarIntent(data);
            return;
        }
        if (requestCode == CATEGORIES_ACTIVITY_REQUEST) {
            handleCategoriesIntent(data);
            return;
        }
    }
}
