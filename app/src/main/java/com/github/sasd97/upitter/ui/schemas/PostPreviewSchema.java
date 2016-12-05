package com.github.sasd97.upitter.ui.schemas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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
import com.github.sasd97.upitter.models.response.pointers.CommentPointerModel;
import com.github.sasd97.upitter.models.response.pointers.CommentsPointerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.ImagePointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostsPointerModel;
import com.github.sasd97.upitter.services.query.CommentsQueryService;
import com.github.sasd97.upitter.services.query.FeedQueryService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedQuizVariantRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedQuizVariantVotedRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.ImageCollageRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.PostCommentsRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.GALLERY_PREVIEW_SELECTION_MODE;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.LIST_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.MODE_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;
import static com.github.sasd97.upitter.constants.PostCreateConstants.POST_ID;

public class PostPreviewSchema extends BaseActivity
        implements PostQueryService.OnPostListener,
        FeedQueryService.OnTapeQueryListener,
        CommentsQueryService.OnCommentListener,
        FeedQuizVariantRecycler.OnItemClickListener,
        ImageCollageRecycler.OnImageClickListener {

    private String postId;
    private UserModel user;
    private PostPointerModel post;
    private PostQueryService postQueryService;
    private FeedQueryService feedQueryService;
    private CommentsQueryService commentsQueryService;

    private PostCommentsRecycler recyclerAdapter;

    @BindView(R.id.user_name_post_single_view) TextView userNameTextView;
    @BindView(R.id.title_post_single_view) TextView postTitleTextView;
    @BindView(R.id.text_post_single_view) TextView postDescriptionTextView;
    @BindView(R.id.category_label_post_single_view) TextView categoryNameTextView;
    @BindView(R.id.like_counter_post_single_view) TextView likeAmountTextView;
    @BindView(R.id.comments_counter_post_single_view) TextView commentsAmountTextView;
    @BindView(R.id.offset_from_now_post_single_view) TextView offsetFromNow;
    @BindView(R.id.watch_counter_post_single_view) TextView watchesAmountTextView;

    @BindView(R.id.user_icon_post_single_view) ImageView userAvatarImageView;
    @BindView(R.id.category_preview_post_single_view) CircleImageView categoryImageView;
    @BindView(R.id.comments_icon_post_single_view) ImageView commentImageView;
    @BindView(R.id.like_icon_post_single_view) ImageView likeImageButton;
    @BindView(R.id.favorites_icon_post_single_view) ImageView favoriteImageButton;

    @BindView(R.id.like_layout_post_single_view) LinearLayout likeLinearLayout;
    @BindView(R.id.comments_layout_post_single_view) LinearLayout commentLinearLayout;

    @BindView(R.id.quiz_variants_post_single_view) RecyclerView quizVariantsRecyclerView;
    @BindView(R.id.quiz_result_post_single_view) RecyclerView quizResultHorizontalChart;
    @BindView(R.id.post_images_post_single_view) RecyclerView imagesRecyclerView;
    @BindView(R.id.favorites_layout_post_single_view) LinearLayout favoritesLinearLayout;

    @BindView(R.id.comment_conversation) RecyclerView commentConversation;
    @BindView(R.id.input_message) EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.1f));
        postQueryService = PostQueryService.getService(this);
        feedQueryService = FeedQueryService.getService(this);
        commentsQueryService = CommentsQueryService.getService(this);
        user = getHolder().get();
        postId = getIntent().getStringExtra(POST_ID);

        postQueryService.findPost(user.getAccessToken(), postId);
        postQueryService.watchPost(user.getAccessToken(), postId);

        recyclerAdapter = new PostCommentsRecycler();
        commentConversation.setLayoutManager(new LinearLayoutManager(this));
        commentConversation.setNestedScrollingEnabled(false);
        commentConversation.setAdapter(recyclerAdapter);


        View.OnClickListener likeClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedQueryService.like(user.getAccessToken(),
                        post.getId());
            }
        };

        View.OnClickListener favoriteClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedQueryService.favorite(user.getAccessToken(),
                        post.getId());
            }
        };

        likeLinearLayout.setOnClickListener(likeClick);
        favoritesLinearLayout.setOnClickListener(favoriteClick);

        quizResultHorizontalChart.setLayoutManager(new LinearLayoutManager(this));
        quizVariantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onFindPost(PostPointerModel post) {
        Logger.d(post.toString());
        this.post = post;
        CompanyPointerModel author = post.getCompany();

        obtainPostAuthor(author);
        obtainPost(post);

        commentsQueryService.obtainComments(user.getAccessToken(), post.getId());
    }

    //region post preparation
    @Override
    public void onLike(PostPointerModel post) {
        this.post = post;
        likeImageButton.setImageResource(R.drawable.ic_feed_icon_like_active);
        likeAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        likeAmountTextView.setText(post.getLikesAmount());
    }

    @Override
    public void onDislike(PostPointerModel post) {
        this.post = post;
        likeImageButton.setImageResource(R.drawable.ic_feed_icon_like);
        likeAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        likeAmountTextView.setText(post.getLikesAmount());
    }

    @Override
    public void onAddFavorites(PostPointerModel post) {
        this.post = post;
        favoriteImageButton.setImageResource(R.drawable.ic_feed_icon_favorite_active);
    }

    @Override
    public void onRemoveFromFavorites(PostPointerModel post) {
        this.post = post;
        favoriteImageButton.setImageResource(R.drawable.ic_feed_icon_favorite);
    }


    @Override
    public void onVote(PostPointerModel post) {
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
        intent.putStringArrayListExtra(LIST_ATTACH,
                ListUtils.mutateConcrete(post.getImages(), new ListUtils.OnListMutateListener<ImagePointerModel, String>() {
            @Override
            public String mutate(ImagePointerModel object) {
                return object.getOriginalUrl();
            }
        }));
        intent.putExtra(GALLERY_PREVIEW_SELECTION_MODE, true);
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
    public void onCreatePost() {

    }

    @Override
    public void onPostWatch(int amount) {
        watchesAmountTextView.setText(String.valueOf(amount));
    }

    @Override
    public void onPostObtained(PostsPointerModel posts) {

    }

    @Override
    public void onError(ErrorModel error) {
        Logger.d(error.toString());
    }


    private void obtainPostAuthor(CompanyPointerModel author) {
        obtainAvatar(userAvatarImageView, author);
        userNameTextView.setText(author.getName());
    }

    private void obtainAvatar(ImageView holder, CompanyPointerModel author) {
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

    private void obtainPost(PostPointerModel post) {
        obtainCategory(categoryImageView, categoryNameTextView, post.getCategoryId());
        obtainSubBar(likeImageButton, likeAmountTextView,
                commentImageView, commentsAmountTextView,
                favoriteImageButton, post);
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
                              ImageView favoritesImageHolder, PostPointerModel post) {

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

        if (post.isFavoriteByMe()) favoritesImageHolder.setImageResource(R.drawable.ic_feed_icon_favorite_active);
        else favoritesImageHolder.setImageResource(R.drawable.ic_feed_icon_favorite);
    }

    private void obtainQuiz(PostPointerModel post) {
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

    private void obtainCollage(PostPointerModel post) {
        if (post.getImages() == null || post.getImages().size() == 0) {
            imagesRecyclerView.setVisibility(View.GONE);
            return;
        }

        CollageLayoutManager collageLayoutManager = new CollageLayoutManager(post.getImages());
        ImageCollageRecycler adapter = new ImageCollageRecycler(this, post.getImages());
        adapter.setOnItemClickListener(this);

        imagesRecyclerView.setVisibility(View.VISIBLE);
        imagesRecyclerView.setLayoutManager(collageLayoutManager);
        imagesRecyclerView.setAdapter(adapter);
    }
    //endregion

    @Override
    public void onRemove(boolean isSuccess) {

    }

    @Override
    public void onEdit(CommentPointerModel comment) {

    }

    @Override
    public void onAdd(CommentPointerModel comment) {
        recyclerAdapter.addToEnd(comment);
        post.addCommentsAmount();

        if (post.getCommentsAmount() > 0) {
            commentImageView.setImageResource(R.drawable.ic_feed_icon_comment_active);
            commentsAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        else {
            commentImageView.setImageResource(R.drawable.ic_feed_icon_comment);
            commentsAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorCounter));
        }

        commentsAmountTextView.setText(String.valueOf(post.getCommentsAmount()));
    }

    @Override
    public void onObtain(CommentsPointerModel comments) {
        Logger.i(comments.toString());
        recyclerAdapter.addAll(comments.getComments());
    }

    @OnClick(R.id.send_message)
    public void sendComment(View v) {
        commentsQueryService.addComment(user.getAccessToken(),
                this.post.getId(),
                text.getText().toString());
        text.setText("");
    }
}
