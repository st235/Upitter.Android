package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.ui.adapters.ImageHolderRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.results.PostCategoriesChooseActivity;
import com.github.sasd97.upitter.ui.results.QuizActivity;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Gallery;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;
import java.util.List;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.GALLERY_MULTI_SELECTED_PHOTOS_LIST;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.QUIZ_MULTI_SELECTION_LIST;
import static com.github.sasd97.upitter.Upitter.*;

public class CreatePostActivity extends BaseActivity
    implements ImageHolderRecyclerAdapter.OnAmountChangeListener {

    private ArrayList<String> selectedQuiz;
    private ArrayList<String> selectedPhotos;


    private RecyclerView photosRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageHolderRecyclerAdapter imageHolderRecyclerAdapter;

    private ImageView addressIconImageView;
    private ImageView quizIconImageView;
    private ImageView photoIconImageView;

    private TextView addressTextView;
    private TextView quizTextView;
    private TextView photoTextView;

    private LinearLayout categoryLinearLayout;
    private ImageView categoryPreviewImageView;
    private TextView categoryTextView;

    public CreatePostActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_activity);
        setToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
        setCategory(Categories.getDefaultCategory());

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageHolderRecyclerAdapter = new ImageHolderRecyclerAdapter(this, new ArrayList<String>(), this);
        photosRecyclerView.setLayoutManager(linearLayoutManager);
        photosRecyclerView.setAdapter(imageHolderRecyclerAdapter);
    }

    @Override
    protected void bindViews() {
        photosRecyclerView = findById(R.id.image_placeholder_recyclerview_publication);
        addressIconImageView = findById(R.id.address_icon_create_post_activity);
        quizIconImageView = findById(R.id.quiz_icon_create_post_activity);
        photoIconImageView = findById(R.id.photos_image_view_publication);
        addressTextView = findById(R.id.address_text_create_post_activity);
        quizTextView = findById(R.id.quiz_text_create_post_activity);
        photoTextView = findById(R.id.photos_text_view_publication);
        categoryLinearLayout = findById(R.id.category_layout_create_post_activity);
        categoryPreviewImageView = findById(R.id.category_preview_create_post_activity);
        categoryTextView = findById(R.id.category_text_create_post_activity);
    }

    public void onPhotosClick(View v) {
        Intent gallery = new Gallery
                .Builder()
                .from(this)
                .multiSelectionMode(true)
                .selectionMaxCounter(5)
                .build();
        startActivityForResult(gallery, RequestCodesConstants.GALLERY_ACTIVITY_REQUEST);
    }

    public void onQuizClick(View v) {
        Intent quizIntent = new Intent(this, QuizActivity.class);
        if (selectedQuiz != null) quizIntent.putStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST, selectedQuiz);
        startActivityForResult(quizIntent, RequestCodesConstants.CREATE_QUIZ_REQUEST);
    }

    public void onAddressClick(View v) {
        CompanyModel companyModel = (CompanyModel) getHolder().get();
        List<String> result = ListUtils.mutate(companyModel.getCoordinates(), new ListUtils.OnListMutateListener<CoordinatesModel, String>() {
            @Override
            public String mutate(CoordinatesModel object) {
                return object.getAddress().getAddressLine(0);
            }
        });

        new MaterialDialog.Builder(this)
                .title("Alex")
                .items(result)
                .show();
    }

    public void onCategoriesSelectClick(View v) {
        startActivityForResult(new Intent(this, PostCategoriesChooseActivity.class),
                RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST);
    }

    @Override
    public void onEmpty() {
        photosRecyclerView.setVisibility(View.GONE);
        highlightHandler(photoIconImageView, photoTextView, R.drawable.ic_icon_add_photo, R.color.colorPrimary);
    }

    public void highlightHandler(ImageView imageView,
                                 TextView textView,
                                 int drawable,
                                 int color) {
        imageView.setImageDrawable(ContextCompat.getDrawable(this, drawable));
        textView.setTextColor(ContextCompat.getColor(this, color));
    }

    private void setCategory(CategoryModel category) {
        Drawable preview = ContextCompat.getDrawable(this, category.getIntImage());
        categoryPreviewImageView.setImageDrawable(preview);
        categoryTextView.setText(category.getTitle());
    }

    private void handleImages(Intent data) {
        selectedPhotos = data.getStringArrayListExtra(GALLERY_MULTI_SELECTED_PHOTOS_LIST);
        imageHolderRecyclerAdapter.addAll(selectedPhotos);

        highlightHandler(photoIconImageView, photoTextView, R.drawable.ic_icon_add_photo_active, R.color.colorAccent);
        photosRecyclerView.setVisibility(View.VISIBLE);
    }

    private void handleQuiz(Intent data) {
        selectedQuiz = data.getStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST);
        highlightHandler(quizIconImageView, quizTextView, R.drawable.ic_icon_quiz_active, R.color.colorAccent);
    }

    private void handleCategories(Intent data) {
        CategoryModel categoryModel = data.getParcelableExtra(IntentKeysConstants.CATEGORIES_ATTACH);
        setCategory(categoryModel);
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
