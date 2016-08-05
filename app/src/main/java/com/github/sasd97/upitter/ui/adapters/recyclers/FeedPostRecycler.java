package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.CollageLayoutManager;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.models.response.fileServer.MediaResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.services.query.FeedQueryService;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.ui.schemas.AlbumPreviewGallerySchema;
import com.github.sasd97.upitter.ui.schemas.MapPreviewSchema;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COORDINATES_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.LIST_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.MODE_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;

/**
 * Created by alexander on 08.07.16.
 */

public class FeedPostRecycler extends RecyclerView.Adapter<FeedPostRecycler.TapeViewHolder> {

    private static final String TAG = "TAPE RECYCLER ADAPTER";
    private static final int FIRST_POSITION = 0;

    private Context context;

    private CompanyModel company;
    private List<PostResponseModel> posts;

    public FeedPostRecycler(Context context, CompanyModel company) {
        posts = new ArrayList<>();
        this.context = context;
        this.company = company;
    }

    public class TapeViewHolder extends BaseViewHolder
        implements FeedQueryService.OnTapeQueryListener,
            FeedQuizVariantRecycler.OnItemClickListener,
            ImageCollageRecycler.OnImageClickListener,
            Toolbar.OnMenuItemClickListener {

        @BindView(R.id.post_toolbar) Toolbar postToolbar;

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

        @BindView(R.id.like_icon_post_single_view) ShineButton likeShineButton;
        @BindView(R.id.favorites_icon_post_single_view) ShineButton favoriteShineButton;

        @BindView(R.id.like_layout_post_single_view) LinearLayout likeLinearLayout;
        @BindView(R.id.comments_layout_post_single_view) LinearLayout commentLinearLayout;

        @BindView(R.id.quiz_variants_post_single_view) RecyclerView quizVariantsRecyclerView;
        @BindView(R.id.quiz_result_post_single_view) RecyclerView quizResultHorizontalChart;
        @BindView(R.id.post_images_post_single_view) RecyclerView imagesRecyclerView;

        private FeedQueryService queryService;
        private View.OnClickListener likeClick;
        private View.OnClickListener favoriteClick;

        public TapeViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            queryService = FeedQueryService.getService(this);
            postToolbar.setOnMenuItemClickListener(this);

            likeClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeShineButton.setChecked(true);
                    queryService.like(company.getAccessToken(),
                            posts.get(getAdapterPosition()).getId());
                }
            };

            favoriteClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryService.favorite(company.getAccessToken(),
                            posts.get(getAdapterPosition()).getId());
                }
            };

            likeShineButton.setOnClickListener(likeClick);
            likeLinearLayout.setOnClickListener(likeClick);
            favoriteShineButton.setOnClickListener(favoriteClick);

            quizResultHorizontalChart.setLayoutManager(new LinearLayoutManager(context));
            quizVariantsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            PostResponseModel post = posts.get(getAdapterPosition());

            switch (item.getItemId()) {
                case R.id.action_show_on_map:
                    Intent intent = new Intent(context, MapPreviewSchema.class);
                    intent.putExtra(COORDINATES_ATTACH, post.toAuthorOnMapModel());
                    context.startActivity(intent);
                    break;
            }

            return false;
        }

        @Override
        public void onLike(PostResponseModel post) {
            posts.set(getAdapterPosition(), post);
            likeShineButton.showAnim();
            likeShineButton.setImageResource(R.drawable.ic_feed_icon_like_active);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            likeAmountTextView.setText(post.getLikesAmount());
        }

        @Override
        public void onDislike(PostResponseModel post) {
            posts.set(getAdapterPosition(), post);
            likeShineButton.setImageResource(R.drawable.ic_feed_icon_like);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            likeAmountTextView.setText(post.getLikesAmount());
        }

        @Override
        public void onAddFavorites(PostResponseModel post) {
            posts.set(getAdapterPosition(), post);
            favoriteShineButton.showAnim();
            favoriteShineButton.setImageResource(R.drawable.ic_feed_icon_favorite_active);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        @Override
        public void onVote(PostResponseModel post) {
            quizResultHorizontalChart.setVisibility(View.VISIBLE);
            quizVariantsRecyclerView.setVisibility(View.GONE);
            posts.set(getAdapterPosition(), post);

            quizResultHorizontalChart.setAdapter(new FeedQuizVariantVotedRecycler(post.getQuiz(),
                    context.getString(R.string.voice_postfix),
                    posts.get(getAdapterPosition()).getVotersAmount()));
            quizResultHorizontalChart.setHasFixedSize(true);
        }

        @Override
        public void onRemoveFromFavorites(PostResponseModel post) {

        }

        @Override
        public void onError(ErrorModel error) {
            Log.d("TAPE_RECYCLER_ADAPTER", error.toString());
        }

        @Override
        public void onImageClick(int position) {
            PostResponseModel postResponseModel = posts.get(getAdapterPosition());
            Intent intent = new Intent(context, AlbumPreviewGallerySchema.class);
            intent.putStringArrayListExtra(LIST_ATTACH, ListUtils.mutateConcrete(postResponseModel.getMedia(), new ListUtils.OnListMutateListener<MediaResponseModel, String>() {
                @Override
                public String mutate(MediaResponseModel object) {
                    return object.getUrl();
                }
            }));
            intent.putExtra(POSITION_ATTACH, position);
            intent.putExtra(MODE_ATTACH, 1);
            context.startActivity(intent);
        }

        @Override
        public void onItemClick(int position) {
            queryService.vote(company.getAccessToken(),
                    posts.get(getAdapterPosition()).getId(),
                    position);
        }
    }

    @Override
    public TapeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_feed_post, parent, false);
        return new TapeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TapeViewHolder holder, int position) {
        PostResponseModel post = posts.get(position);
        CompanyResponseModel author = post.getCompany();

        obtainPostAuthor(holder, author);
        obtainPost(holder, post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAll(List<PostResponseModel> posts) {
        this.posts.addAll(posts);
        notifyItemInserted(this.posts.size());
    }

    private void obtainPostAuthor(TapeViewHolder holder, CompanyResponseModel author) {
        obtainAvatar(holder.userAvatarImageView, author);
        holder.userNameTextView.setText(author.getName());
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
                .with(context)
                .load(author.getLogoUrl())
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, Dimens.dpToPx(4), 0))
                .into(holder);
    }

    private void obtainPost(TapeViewHolder holder, PostResponseModel post) {
        obtainToolbar(holder.postToolbar, post.getCompany());

        obtainCategory(holder.categoryImageView, holder.categoryNameTextView, post.getCategoryId());
        obtainSubBar(holder.likeShineButton, holder.likeAmountTextView,
                holder.commentImageView, holder.commentsAmountTextView,
                holder.favoriteShineButton, post);
        obtainQuiz(holder, post);
        obtainCollage(holder, post);

        holder.offsetFromNow.setText(post.getDateFromNow());
        holder.postTitleTextView.setText(post.getTitle());
        holder.postDescriptionTextView.setText(post.getText());
        holder.watchesAmountTextView.setText(String.valueOf(post.getWatchesAmount()));
    }

    private void obtainToolbar(Toolbar toolbar, CompanyResponseModel author) {
        toolbar.getMenu().clear();

        if (company.getUId().equalsIgnoreCase(author.getId())) {
            toolbar.inflateMenu(R.menu.post_owner_single_view_menu);
            return;
        }

        toolbar.inflateMenu(R.menu.post_user_single_view_menu);
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
            likesTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else {
            likesImageHolder.setImageResource(R.drawable.ic_feed_icon_like);
            likesTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorCounter));
        }

        likesTextHolder.setText(post.getLikesAmount());

        if (post.getCommentsAmount() > 0) {
            commentsImageHolder.setImageResource(R.drawable.ic_feed_icon_comment_active);
            commentsTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else {
            commentsImageHolder.setImageResource(R.drawable.ic_feed_icon_comment);
            commentsTextHolder.setTextColor(ContextCompat.getColor(context, R.color.colorCounter));
        }

        commentsTextHolder.setText(String.valueOf(post.getCommentsAmount()));
    }

    private void obtainQuiz(TapeViewHolder holder, PostResponseModel post) {
        if (post.getQuiz() == null || post.getQuiz().size() == 0) {
            holder.quizResultHorizontalChart.setVisibility(View.GONE);
            holder.quizVariantsRecyclerView.setVisibility(View.GONE);
            return;
        }

        if (post.isVotedByMe()) {
            holder.quizResultHorizontalChart.setVisibility(View.VISIBLE);
            holder.quizVariantsRecyclerView.setVisibility(View.GONE);
            holder.quizResultHorizontalChart.setAdapter(new FeedQuizVariantVotedRecycler(post.getQuiz(),
                    context.getString(R.string.voice_postfix),
                    post.getVotersAmount()));
            holder.quizResultHorizontalChart.setHasFixedSize(true);
            return;
        }

        holder.quizResultHorizontalChart.setVisibility(View.GONE);
        holder.quizVariantsRecyclerView.setVisibility(View.VISIBLE);
        holder.quizVariantsRecyclerView.setAdapter(new FeedQuizVariantRecycler(post.getQuiz(), holder));
    }

    private void obtainCollage(final TapeViewHolder holder, PostResponseModel post) {
        if (post.getMedia() == null || post.getMedia().size() == 0) {
            holder.imagesRecyclerView.setVisibility(View.GONE);
            return;
        }

        CollageLayoutManager collageLayoutManager = new CollageLayoutManager(post.getMedia());
        ImageCollageRecycler adapter = new ImageCollageRecycler(context, post.getMedia());
        adapter.setOnItemClickListener(holder);

        holder.imagesRecyclerView.setVisibility(View.VISIBLE);
        holder.imagesRecyclerView.setLayoutManager(collageLayoutManager);
        holder.imagesRecyclerView.setAdapter(adapter);
    }

    public String getFirstPostId() {
        if (getItemCount() == 0) return "0";
        return posts.get(FIRST_POSITION).getId();
    }

    public String getLastPostId() {
        return posts.get(getItemCount() - 1).getId();
    }

    public void addAhead(List<PostResponseModel> newPosts) {
        posts.addAll(FIRST_POSITION, newPosts);
        notifyItemInserted(newPosts.size() - 1);
    }

    public void addBehind(List<PostResponseModel> newPosts) {
        posts.addAll(newPosts);
        notifyItemInserted(posts.size());
    }

    public void refresh() {
        notifyItemRangeRemoved(0, getItemCount());
        posts.clear();
    }
}
