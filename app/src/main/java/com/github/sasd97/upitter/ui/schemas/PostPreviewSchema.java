package com.github.sasd97.upitter.ui.schemas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.CollageLayoutManager;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.models.response.fileServer.MediaResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.query.FeedQueryService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedQuizVariantRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedQuizVariantVotedRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.ImageCollageRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.sackcentury.shinebuttonlib.ShineButton;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.LIST_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.MODE_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;
import static com.github.sasd97.upitter.constants.PostCreateConstants.POST_ID;

public class PostPreviewSchema extends BaseActivity
        implements PostQueryService.OnPostListener,
        FeedQueryService.OnTapeQueryListener,
        FeedQuizVariantRecycler.OnItemClickListener,
        ImageCollageRecycler.OnImageClickListener {

    private String postId;
    private UserModel user;
    private PostResponseModel post;
    private PostQueryService postQueryService;
    private FeedQueryService feedQueryService;

    @BindView(R.id.user_name_post_single_view) TextView userNameTextView;
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
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.1f));
    }

    @Override
    protected void setupViews() {
        postQueryService = PostQueryService.getService(this);
        feedQueryService = FeedQueryService.getService(this);
        user = getHolder().get();
        postId = getIntent().getStringExtra(POST_ID);
        
        postQueryService.findPost(user.getAccessToken(), postId);
    }

    @Override
    public void onFindPost(PostResponseModel post) {
        Logger.d(post.toString());
        CompanyResponseModel author = post.getCompany();

        obtainPostAuthor(author);
        obtainPost(post);
    }

    @Override
    public void onLike(PostResponseModel post) {
        this.post = post;
        likeShineButton.showAnim();
        likeShineButton.setImageResource(R.drawable.ic_feed_icon_like_active);
        likeAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        likeAmountTextView.setText(post.getLikesAmount());
    }

    @Override
    public void onDislike(PostResponseModel post) {
        this.post = post;
        likeShineButton.setImageResource(R.drawable.ic_feed_icon_like);
        likeAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        likeAmountTextView.setText(post.getLikesAmount());
    }

    @Override
    public void onAddFavorites(PostResponseModel post) {
        this.post = post;
        favoriteShineButton.showAnim();
        favoriteShineButton.setImageResource(R.drawable.ic_feed_icon_favorite_active);
        likeAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @Override
    public void onVote(PostResponseModel post) {
        quizResultHorizontalChart.setVisibility(View.VISIBLE);
        quizVariantsRecyclerView.setVisibility(View.GONE);
        this.post = post;

        quizResultHorizontalChart.setAdapter(new FeedQuizVariantVotedRecycler(post.getQuiz(),
                getString(R.string.voice_postfix),
                post.getVotersAmount()));
        quizResultHorizontalChart.setHasFixedSize(true);
    }

    @Override
    public void onImageClick(int position) {
        Intent intent = new Intent(this, AlbumPreviewGallerySchema.class);
        intent.putStringArrayListExtra(LIST_ATTACH, ListUtils.mutateConcrete(post.getMedia(), new ListUtils.OnListMutateListener<MediaResponseModel, String>() {
            @Override
            public String mutate(MediaResponseModel object) {
                return object.getUrl();
            }
        }));
        intent.putExtra(POSITION_ATTACH, position);
        intent.putExtra(MODE_ATTACH, 1);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        feedQueryService.vote(user.getAccessToken(),
               post.getId(),
                position);
    }

    @Override
    public void onRemoveFromFavorites(PostResponseModel post) {

    }

    @Override
    public void onCreatePost() {

    }

    @Override
    public void onPostWatch(int amount) {

    }

    @Override
    public void onPostObtained(PostsResponseModel posts) {

    }

    @Override
    public void onError(ErrorModel error) {
        Logger.d(error.toString());
    }


    private void obtainPostAuthor(CompanyResponseModel author) {
        obtainAvatar(userAvatarImageView, author);
        userNameTextView.setText(author.getName());
    }

    private void obtainAvatar(ImageView holder, CompanyResponseModel author) {
        if (author.getLogoUrl() == null) {
            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(28))
                    .height(Dimens.dpToPx(28))
                    .endConfig()
                    .buildRoundRect(author.getPreview(), Palette.getAvatarPalette(), Dimens.dpToPx(5));
            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(this)
                .load(author.getLogoUrl())
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, Dimens.dpToPx(4), 0))
                .into(holder);
    }

    private void obtainPost(PostResponseModel post) {
        obtainCategory(categoryImageView, categoryNameTextView, post.getCategoryId());
        obtainSubBar(likeShineButton, likeAmountTextView,
                commentImageView, commentsAmountTextView,
                favoriteShineButton, post);
        obtainQuiz(post);
        obtainCollage(post);

        offsetFromNow.setText(post.getDateFromNow());
        postTitleTextView.setText(post.getTitle());
        postDescriptionTextView.setText(post.getText());
        watchesAmountTextView.setText(String.valueOf(post.getWatchesAmount()));
    }

    private void obtainCategory(CircleImageView imageHolder, TextView textHolder, String cid) {
        CategoryModel currentCategory = Categories.find(cid);
        textHolder.setText(currentCategory.getTitle());
        imageHolder.setImageResource(currentCategory.getDrawable());
    }

    private void obtainSubBar(ImageView likesImageHolder, TextView likesTextHolder,
                              ImageView commentsImageHolder, TextView commentsTextHolder,
                              ImageView favoritesImageHolder, PostResponseModel post) {

        if (post.isLikedByMe()) {
            likesImageHolder.setImageResource(R.drawable.ic_feed_icon_like_active);
            likesTextHolder.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        else {
            likesImageHolder.setImageResource(R.drawable.ic_feed_icon_like);
            likesTextHolder.setTextColor(ContextCompat.getColor(this, R.color.colorCounter));
        }

        likesTextHolder.setText(post.getLikesAmount());

        if (post.getCommentsAmount() > 0) {
            commentsImageHolder.setImageResource(R.drawable.ic_feed_icon_comment_active);
            commentsTextHolder.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        else {
            commentsImageHolder.setImageResource(R.drawable.ic_feed_icon_comment);
            commentsTextHolder.setTextColor(ContextCompat.getColor(this, R.color.colorCounter));
        }

        commentsTextHolder.setText(String.valueOf(post.getCommentsAmount()));
    }

    private void obtainQuiz(PostResponseModel post) {
        if (post.getQuiz() == null || post.getQuiz().size() == 0) {
            quizResultHorizontalChart.setVisibility(View.GONE);
            quizVariantsRecyclerView.setVisibility(View.GONE);
            return;
        }

        if (post.isVotedByMe()) {
            quizResultHorizontalChart.setVisibility(View.VISIBLE);
            quizVariantsRecyclerView.setVisibility(View.GONE);
            quizResultHorizontalChart.setAdapter(new FeedQuizVariantVotedRecycler(post.getQuiz(),
                    this.getString(R.string.voice_postfix),
                    post.getVotersAmount()));
            quizResultHorizontalChart.setHasFixedSize(true);
            return;
        }

        quizResultHorizontalChart.setVisibility(View.GONE);
        quizVariantsRecyclerView.setVisibility(View.VISIBLE);
        quizVariantsRecyclerView.setAdapter(new FeedQuizVariantRecycler(post.getQuiz(), this));
    }

    private void obtainCollage(PostResponseModel post) {
        if (post.getMedia() == null || post.getMedia().size() == 0) {
            imagesRecyclerView.setVisibility(View.GONE);
            return;
        }

        CollageLayoutManager collageLayoutManager = new CollageLayoutManager(post.getMedia());
        ImageCollageRecycler adapter = new ImageCollageRecycler(this, post.getMedia());
        adapter.setOnItemClickListener(this);

        imagesRecyclerView.setVisibility(View.VISIBLE);
        imagesRecyclerView.setLayoutManager(collageLayoutManager);
        imagesRecyclerView.setAdapter(adapter);
    }
}
