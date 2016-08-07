package com.github.sasd97.upitter.ui.schemas;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.sackcentury.shinebuttonlib.ShineButton;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostPreviewSchema extends BaseActivity {

    @BindView(R.id.user_name_post_single_view)
    TextView userNameTextView;
    @BindView(R.id.title_post_single_view) TextView postTitleTextView;
    @BindView(R.id.text_post_single_view) TextView postDescriptionTextView;
    @BindView(R.id.category_label_post_single_view) TextView categoryNameTextView;
    @BindView(R.id.like_counter_post_single_view) TextView likeAmountTextView;
    @BindView(R.id.comments_counter_post_single_view) TextView commentsAmountTextView;
    @BindView(R.id.offset_from_now_post_single_view) TextView offsetFromNow;
    @BindView(R.id.watch_counter_post_single_view) TextView watchesAmountTextView;

    @BindView(R.id.user_icon_post_single_view)
    ImageView userAvatarImageView;
    @BindView(R.id.category_preview_post_single_view)
    CircleImageView categoryImageView;
    @BindView(R.id.comments_icon_post_single_view) ImageView commentImageView;

    @BindView(R.id.like_icon_post_single_view)
    ShineButton likeShineButton;
    @BindView(R.id.favorites_icon_post_single_view) ShineButton favoriteShineButton;

    @BindView(R.id.like_layout_post_single_view)
    LinearLayout likeLinearLayout;
    @BindView(R.id.comments_layout_post_single_view) LinearLayout commentLinearLayout;

    @BindView(R.id.quiz_variants_post_single_view)
    RecyclerView quizVariantsRecyclerView;
    @BindView(R.id.quiz_result_post_single_view) RecyclerView quizResultHorizontalChart;
    @BindView(R.id.post_images_post_single_view) RecyclerView imagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview);
        setToolbar(R.id.toolbar);
    }

    @Override
    protected void setupViews() {

    }
}
