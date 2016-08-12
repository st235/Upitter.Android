package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.ImageHolderRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.results.PostTypeSelectionResult;
import com.github.sasd97.upitter.ui.results.QuizCreationResult;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.DialogUtils;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.services.PostBuilderService;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.*;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.GALLERY_MULTI_SELECTED_PHOTOS_LIST;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.QUIZ_MULTI_SELECTION_LIST;

public class PostCreationActivity extends BaseActivity
    implements ImageHolderRecyclerAdapter.OnAmountChangeListener,
        MaterialDialog.ListCallbackSingleChoice,
        PostQueryService.OnPostListener,
        PostBuilderService.OnPostBuilderListener {

    private CompanyModel company;
    private PostBuilderService postBuilderService;
    private MaterialDialog progressDialog;
    private int whichCoordinatesSelected = -1;
    private ImageHolderRecyclerAdapter imageHolderRecyclerAdapter;

    private int imageCounter = 0;
    private int loadedImagesCounter = 0;

    @BindView(R.id.image_placeholder_recyclerview_publication) RecyclerView photosRecyclerView;

    @BindView(R.id.address_icon_create_post_activity) ImageView addressIconImageView;
    @BindView(R.id.quiz_icon_create_post_activity) ImageView quizIconImageView;
    @BindView(R.id.photos_image_view_publication) ImageView photoIconImageView;

    @BindView(R.id.address_text_create_post_activity) TextView addressTextView;
    @BindView(R.id.quiz_text_create_post_activity) TextView quizTextView;
    @BindView(R.id.photos_text_view_publication) TextView photoTextView;

    @BindView(R.id.post_title_create_post_activity) MaterialEditText postTitleEditText;
    @BindView(R.id.post_description_create_post_activity) MaterialEditText postTextEditText;

    @BindView(R.id.category_preview_create_post_activity) ImageView categoryPreviewImageView;
    @BindView(R.id.category_text_create_post_activity) TextView categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        company = (CompanyModel) getHolder().get();
        PostQueryService queryService = PostQueryService.getService(this);
        postBuilderService = PostBuilderService.getBuilder(this, queryService);
        postBuilderService.uid(company.getUId());
        progressDialog = DialogUtils.showProgressDialog(this);

        setCategory(Categories.getDefaultCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageHolderRecyclerAdapter = new ImageHolderRecyclerAdapter(this, new ArrayList<String>(), this);
        photosRecyclerView.setLayoutManager(linearLayoutManager);
        photosRecyclerView.setAdapter(imageHolderRecyclerAdapter);
    }

    public void onPhotosClick(View v) {
        Intent gallery = new Gallery
                .Builder()
                .from(this)
                .multiSelectionMode(true)
                .selectionMaxCounter(10)
                .build();
        startActivityForResult(gallery, RequestCodesConstants.GALLERY_ACTIVITY_REQUEST);
    }

    public void onQuizClick(View v) {
        Intent quizIntent = new Intent(this, QuizCreationResult.class);
        if (postBuilderService.getQuiz() != null)
            quizIntent.putStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST, postBuilderService.getQuiz());
        startActivityForResult(quizIntent, RequestCodesConstants.CREATE_QUIZ_REQUEST);
    }

    public void onAddressClick(View v) {
        final List<String> addresses = ListUtils.mutate(company.getCoordinates(), new ListUtils.OnListMutateListener<CoordinatesModel, String>() {
            @Override
            public String mutate(CoordinatesModel object) {
                return object.getAddressName();
            }
        });

        new MaterialDialog.Builder(this)
                .title(R.string.choose_address_create_post_activity)
                .iconRes(R.drawable.ic_icon_map)
                .items(addresses)
                .itemsCallbackSingleChoice(whichCoordinatesSelected, this)
                .show();
    }

    public void onCategoriesSelectClick(View v) {
        startActivityForResult(new Intent(this, PostTypeSelectionResult.class),
                RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST);
    }

    public void onSendClick(View v) {
        if (imageCounter != loadedImagesCounter) {
            Logger.i("Not enought photos attach");
            return;
        }

        postBuilderService
                .title(postTitleEditText.getText().toString().trim())
                .text(postTextEditText.getText().toString().trim())
                .build(company.getAccessToken());
    }

    @Override
    public void onLoad(String fid) {
        loadedImagesCounter++;
        postBuilderService.addPhoto(fid);
    }

    @Override
    public void onEmpty() {
        photosRecyclerView.setVisibility(View.GONE);
        highlightHandler(photoIconImageView, photoTextView, R.drawable.ic_icon_add_photo, R.color.colorPrimary);
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        postBuilderService.coordinates(company.getCoordinates().get(which));
        whichCoordinatesSelected = which;
        highlightHandler(addressIconImageView, addressTextView, R.drawable.ic_icon_map_active, R.color.colorAccent);
        return false;
    }

    @Override
    public void onPostObtained(PostsResponseModel posts) {

    }

    @Override
    public void onPostWatch(int amount) {

    }

    @Override
    public void onFindPost(PostResponseModel post) {

    }

    @Override
    public void onCreatePost() {
        progressDialog.dismiss();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onError(ErrorModel error) {
        Log.d("ERROR", error.toString());
    }

    public void highlightHandler(ImageView imageView, TextView textView, int drawable, int color) {
        imageView.setImageDrawable(ContextCompat.getDrawable(this, drawable));
        textView.setTextColor(ContextCompat.getColor(this, color));
    }

    private void setCategory(CategoryModel category) {
        Drawable preview = ContextCompat.getDrawable(this, category.getIntImage());
        categoryPreviewImageView.setImageDrawable(preview);
        categoryTextView.setText(category.getTitle());
        postBuilderService.category(category);
    }

    @Override
    public void onPrepare() {
        progressDialog.show();
    }

    @Override
    public void onBuild() {

    }

    @Override
    public void onValidationError(List<PostBuilderService.Missing> list) {
        final String newLine = "\n";
        final String content = getString(R.string.miss_content_create_post_activity);
        final StringBuilder builder = new StringBuilder(content).append(newLine);

        for (PostBuilderService.Missing missing: list) {
            switch (missing) {
                case MISSING_LOCATION:
                    builder.append(getString(R.string.miss_location_create_post_activity)).append(newLine);
                    break;
                case SHORT_TITLE:
                    builder.append(getString(R.string.short_title_create_post_activity)).append(newLine);
                    break;
                case SHORT_DESCRIPTION:
                    builder.append(getString(R.string.short_description_post_activity)).append(newLine);
                    break;
            }
        }

        Snackbar
                .make(getRootView(), R.string.miss_snackbar_create_post_activity, Snackbar.LENGTH_LONG)
                .setAction(R.string.miss_snackbar_action_create_post_activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog
                                .Builder(PostCreationActivity.this)
                                .title(R.string.miss_title_create_post_activity)
                                .content(builder.toString())
                                .show();
                    }
                })
                .show();
    }

    @Override
    public void onPublicationError() {

    }

    @Override
    public void onPrepareError() {
        Snackbar.make(getRootView(),
                "Images doesnot download on server",
                Snackbar.LENGTH_SHORT)
                .show();
    }

    private void handleImages(Intent data) {
        ArrayList<String> selectedPhotos = data.getStringArrayListExtra(GALLERY_MULTI_SELECTED_PHOTOS_LIST);

        imageCounter = selectedPhotos.size();
        loadedImagesCounter = 0;
        imageHolderRecyclerAdapter.reload(selectedPhotos);
        highlightHandler(photoIconImageView, photoTextView, R.drawable.ic_icon_add_photo_active, R.color.colorAccent);
        photosRecyclerView.setVisibility(View.VISIBLE);
    }

    private void handleQuiz(Intent data) {
        postBuilderService.quiz(data.getStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST));
        highlightHandler(quizIconImageView, quizTextView, R.drawable.ic_icon_quiz_active, R.color.colorAccent);
    }

    private void handleCategories(Intent data) {
        setCategory((CategoryModel) data.getParcelableExtra(IntentKeysConstants.CATEGORIES_ATTACH));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == RequestCodesConstants.GALLERY_ACTIVITY_REQUEST) {
            handleImages(data);
            return;
        }

        if (requestCode == RequestCodesConstants.CREATE_QUIZ_REQUEST) {
            handleQuiz(data);
            return;
        }

        if (requestCode == RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST) {
            handleCategories(data);
            return;
        }
    }
}
