package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.ui.adapters.ImageHolderRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.results.PostCategoriesChooseActivity;
import com.github.sasd97.upitter.ui.results.QuizActivity;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.PostBuilder;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.GALLERY_MULTI_SELECTED_PHOTOS_LIST;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.QUIZ_MULTI_SELECTION_LIST;
import static com.github.sasd97.upitter.Upitter.*;

public class PostCreationActivity extends BaseActivity
    implements ImageHolderRecyclerAdapter.OnAmountChangeListener,
        MaterialDialog.ListCallbackSingleChoice,
        PostQueryService.OnPostListener,
        PostBuilder.OnPostBuilderListener {

    private CompanyModel company;
    private PostBuilder postBuilder;

    private ImageHolderRecyclerAdapter imageHolderRecyclerAdapter;
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

    private int whichCoordinatesSelected = -1;

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
        postBuilder = PostBuilder.getBuilder(this, queryService);

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
        Intent quizIntent = new Intent(this, QuizActivity.class);
        if (postBuilder.getQuiz() != null)
            quizIntent.putStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST, postBuilder.getQuiz());
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
        startActivityForResult(new Intent(this, PostCategoriesChooseActivity.class),
                RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST);
    }

    public void onSendClick(View v) {
        postBuilder
                .title(postTitleEditText.getText().toString().trim())
                .text(postTextEditText.getText().toString().trim())
                .build(company.getAccessToken());
    }

    @Override
    public void onEmpty() {
        photosRecyclerView.setVisibility(View.GONE);
        highlightHandler(photoIconImageView, photoTextView, R.drawable.ic_icon_add_photo, R.color.colorPrimary);
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        postBuilder.coordinates(company.getCoordinates().get(which));
        whichCoordinatesSelected = which;
        highlightHandler(addressIconImageView, addressTextView, R.drawable.ic_icon_map_active, R.color.colorAccent);
        return false;
    }

    @Override
    public void onPostObtained(PostsResponseModel posts) {

    }

    @Override
    public void onCreatePost() {
        finish();
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
        postBuilder.category(category);
    }

    @Override
    public void onBuild() {

    }

    @Override
    public void onPublicationError() {

    }

    @Override
    public void onPrepareError() {

    }

    private void handleImages(Intent data) {
        ArrayList<String> selectedPhotos = data.getStringArrayListExtra(GALLERY_MULTI_SELECTED_PHOTOS_LIST);
        postBuilder.rawPhotos(selectedPhotos);
        imageHolderRecyclerAdapter.addAll(selectedPhotos);

        highlightHandler(photoIconImageView, photoTextView, R.drawable.ic_icon_add_photo_active, R.color.colorAccent);
        photosRecyclerView.setVisibility(View.VISIBLE);
    }

    private void handleQuiz(Intent data) {
        postBuilder.quiz(data.getStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST));
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
