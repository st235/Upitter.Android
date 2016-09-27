package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.CollageLayoutManager;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.ComplaintPointerModel;
import com.github.sasd97.upitter.models.response.pointers.ImagePointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.services.query.ComplainQueryService;
import com.github.sasd97.upitter.services.query.FeedQueryService;
import com.github.sasd97.upitter.ui.CompanyBCProfileActivity;
import com.github.sasd97.upitter.ui.CompanyBPProfileActivity;
import com.github.sasd97.upitter.ui.PostCreationActivity;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.ui.schemas.AlbumPreviewGallerySchema;
import com.github.sasd97.upitter.ui.schemas.MapPreviewSchema;
import com.github.sasd97.upitter.ui.schemas.PostPreviewSchema;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.DialogUtils;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.Palette;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COMPANY_ALIAS;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.COORDINATES_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.GALLERY_PREVIEW_SELECTION_MODE;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.LIST_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.MODE_ATTACH;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.POSITION_ATTACH;
import static com.github.sasd97.upitter.constants.PostCreateConstants.POST_ID;

/**
 * Created by alexander on 08.07.16.
 */

public class FeedPostRecycler extends RecyclerView.Adapter<BaseViewHolder> {

    private final int HEADER_VIEW = 0;
    private final int ITEM_VIEW = 1;

    private UserModel user;
    private Context context;
    private boolean isHeader = true;
    private List<PostPointerModel> posts;

    private int FIRST_POSITION = 1;

    public FeedPostRecycler(Context context, UserModel user) {
        posts = new ArrayList<>();
        this.context = context;
        this.user = user;
        this.isHeader = user != null;
        if (this.isHeader) posts.add(new PostPointerModel());
    }

    public FeedPostRecycler(Context context, UserModel user, boolean isHeader) {
        this.user = user;
        this.context = context;
        this.isHeader = isHeader && user != null;
        posts = new ArrayList<>();
        if (!isHeader) FIRST_POSITION = 0;
    }

    public class TapeViewHolder extends BaseViewHolder
        implements FeedQueryService.OnTapeQueryListener,
            FeedQuizVariantRecycler.OnItemClickListener,
            ComplainQueryService.ComplainListener,
            ImageCollageRecycler.OnImageClickListener,
            Toolbar.OnMenuItemClickListener,
            View.OnClickListener {

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
        @BindView(R.id.like_icon_post_single_view) ImageView likeImageButton;
        @BindView(R.id.favorites_icon_post_single_view) ImageView favoriteImageButton;

        @BindView(R.id.like_layout_post_single_view) LinearLayout likeLinearLayout;
        @BindView(R.id.comments_layout_post_single_view) LinearLayout commentLinearLayout;
        @BindView(R.id.favorites_layout_post_single_view) LinearLayout favoritesLinearLayout;
        @BindView(R.id.user_area_post_single_view) LinearLayout userAreaPostLinearLayout;

        @BindView(R.id.quiz_variants_post_single_view) RecyclerView quizVariantsRecyclerView;
        @BindView(R.id.quiz_result_post_single_view) RecyclerView quizResultHorizontalChart;
        @BindView(R.id.post_images_post_single_view) RecyclerView imagesRecyclerView;

        private FeedQueryService queryService;
        private ComplainQueryService complainQueryService;
        private View.OnClickListener likeClick;
        private View.OnClickListener favoriteClick;

        public TapeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void setupViews() {
            queryService = FeedQueryService.getService(this);
            complainQueryService= ComplainQueryService.getService(this);
            postToolbar.setOnMenuItemClickListener(this);
            userAreaPostLinearLayout.setOnClickListener(this);

            likeClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryService.like(user.getAccessToken(),
                            posts.get(getAdapterPosition()).getId());
                }
            };

            favoriteClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryService.favorite(user.getAccessToken(),
                            posts.get(getAdapterPosition()).getId());
                }
            };

            likeLinearLayout.setOnClickListener(likeClick);
            favoritesLinearLayout.setOnClickListener(favoriteClick);

            quizResultHorizontalChart.setLayoutManager(new LinearLayoutManager(context));
            quizVariantsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            PostPointerModel post = posts.get(getAdapterPosition());

            switch (item.getItemId()) {
                case R.id.action_show_on_map:
                    Intent intent = new Intent(context, MapPreviewSchema.class);
                    intent.putExtra(COORDINATES_ATTACH, post.toAuthorOnMapModel());
                    context.startActivity(intent);
                    break;
                case R.id.action_share:
                    ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
                    mShareActionProvider.setShareIntent(doShare());
                    break;
                case R.id.action_complain:
                    complainQueryService.obtainComplainList(user.getAccessToken());
            }

            return false;
        }

        private Intent doShare() {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_TEXT, "Put whatever you want");
            return intent;
        }

        @Override
        public void onObtainComplainList(final List<ComplaintPointerModel> complains) {
                     new MaterialDialog
                    .Builder(context)
                    .title("Complains")
                    .items(complains)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            ComplaintPointerModel complaintPointerModel = complains.get(which);
                            PostPointerModel postPointerModel = posts.get(getAdapterPosition());

                            complainQueryService.createComplain(user.getAccessToken(),
                                    postPointerModel.getId(),
                                    complaintPointerModel.getId()
                            );
                        }
                    })
                    .show();
        }

        @Override
        public void onComplainSuccess() {
            Toast.makeText(context, "Complain success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplainError() {
            Toast.makeText(context, "Complain error", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLike(PostPointerModel post) {
            posts.set(getAdapterPosition(), post);
            likeImageButton.setImageResource(R.drawable.ic_feed_icon_like_active);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            likeAmountTextView.setText(post.getLikesAmount());
        }

        @Override
        public void onDislike(PostPointerModel post) {
            posts.set(getAdapterPosition(), post);
            likeImageButton.setImageResource(R.drawable.ic_feed_icon_like);
            likeAmountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            likeAmountTextView.setText(post.getLikesAmount());
        }

        @Override
        public void onAddFavorites(PostPointerModel post) {
            posts.set(getAdapterPosition(), post);
            favoriteImageButton.setImageResource(R.drawable.ic_feed_icon_favorite_active);
        }

        @Override
        public void onVote(PostPointerModel post) {
            quizResultHorizontalChart.setVisibility(View.VISIBLE);
            quizVariantsRecyclerView.setVisibility(View.GONE);
            posts.set(getAdapterPosition(), post);

            quizResultHorizontalChart.setAdapter(new FeedQuizVariantVotedRecycler(post.getQuiz(),
                    context.getString(R.string.voice_postfix),
                    posts.get(getAdapterPosition()).getVotersAmount()));
            quizResultHorizontalChart.setHasFixedSize(true);
        }

        @Override
        public void onRemoveFromFavorites(PostPointerModel post) {
            posts.set(getAdapterPosition(), post);
            favoriteImageButton.setImageResource(R.drawable.ic_feed_icon_favorite);
        }

        @Override
        public void onError(ErrorModel error) {
            Log.d("TAPE_RECYCLER_ADAPTER", error.toString());
        }

        @Override
        public void onImageClick(int position) {
            PostPointerModel postPointerModel = posts.get(getAdapterPosition());
            Intent intent = new Intent(context, AlbumPreviewGallerySchema.class);
            intent.putStringArrayListExtra(LIST_ATTACH, ListUtils.mutateConcrete(postPointerModel.getImages(), new ListUtils.OnListMutateListener<ImagePointerModel, String>() {
                @Override
                public String mutate(ImagePointerModel object) {
                    return object.getOriginalUrl();
                }
            }));
            intent.putExtra(GALLERY_PREVIEW_SELECTION_MODE, true);
            intent.putExtra(POSITION_ATTACH, position);
            intent.putExtra(MODE_ATTACH, 1);
            context.startActivity(intent);
        }

        @Override
        public void onItemClick(int position) {
            queryService.vote(user.getAccessToken(),
                    posts.get(getAdapterPosition()).getId(),
                    position);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.user_area_post_single_view) {
                Class<?> target = user instanceof PeopleModel ? CompanyBPProfileActivity.class : CompanyBCProfileActivity.class;
                Intent intent = new Intent(context, target);
                intent.putExtra(COMPANY_ALIAS, posts.get(getAdapterPosition()).getCompany().getAlias());
                context.startActivity(intent);
                return;
            }

            Intent intent = new Intent(context, PostPreviewSchema.class);
            intent.putExtra(POST_ID, posts.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }

    public class TapeHeaderViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.avatar_row_feed_header) CircleImageView avatarUrl;
        @BindView(R.id.photo_row_feed_header) ImageView photoAttach;

        public TapeHeaderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void setupViews() {
            photoAttach.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.photo_row_feed_header:
                    break;
                default:
                    Intent intent = new Intent(context, PostCreationActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        switch (viewType) {
            case HEADER_VIEW:
                View header = LayoutInflater.from(context).inflate(R.layout.row_feed_header, parent, false);
                return new TapeHeaderViewHolder(header);
            case ITEM_VIEW:
                View item = LayoutInflater.from(context).inflate(R.layout.row_feed_post, parent, false);
                return new TapeViewHolder(item);
            default:
                View v = LayoutInflater.from(context).inflate(R.layout.row_feed_post, parent, false);
                return new TapeViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader && isHeaderView(position)) return HEADER_VIEW;
        return ITEM_VIEW;
    }

    private boolean isHeaderView(int position) {
        return user != null && position == 0;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (holder instanceof TapeViewHolder) obtainItem((TapeViewHolder) holder, position);
        else if (holder instanceof TapeHeaderViewHolder) obtainHeader((TapeHeaderViewHolder) holder);
    }

    private void obtainHeader(final TapeHeaderViewHolder holder) {
        obtainAvatar(holder.avatarUrl, user.getAvatarUrl(), Names.getNamePreview(user.getName()));
    }

    private void obtainItem(final TapeViewHolder holder, int position) {
        PostPointerModel post = posts.get(position);
        CompanyPointerModel author = post.getCompany();

        if (author != null) obtainPostAuthor(holder, author);
        obtainPost(holder, post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAll(List<PostPointerModel> posts) {
        this.posts.addAll(posts);
        notifyItemInserted(this.posts.size());
    }

    private void obtainPostAuthor(TapeViewHolder holder, CompanyPointerModel author) {
        obtainAvatar(holder.userAvatarImageView, author.getLogoUrl(), author.getPreview());
        holder.userNameTextView.setText(author.getName());
    }

    private void obtainAvatar(ImageView holder, String url, String title) {
        if (url == null) {
            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(55))
                    .height(Dimens.dpToPx(55))
                    .endConfig()
                    .buildRoundRect(title, Palette.getAvatarPalette(), Dimens.dpToPx(5));
            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(context)
                .load(url)
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, Dimens.dpToPx(4), 0))
                .into(holder);
    }

    private void obtainPost(TapeViewHolder holder, PostPointerModel post) {
        obtainToolbar(holder.postToolbar, post.getCompany());

        obtainCategory(holder.categoryImageView, holder.categoryNameTextView, post.getCategoryId());
        obtainSubBar(holder.likeImageButton, holder.likeAmountTextView,
                holder.commentImageView, holder.commentsAmountTextView,
                holder.favoriteImageButton, post);
        obtainQuiz(holder, post);
        obtainCollage(holder, post);

        holder.offsetFromNow.setText(post.getDateFromNow());
        holder.postTitleTextView.setText(post.getTitle());
        holder.postDescriptionTextView.setText(post.getText());
        holder.watchesAmountTextView.setText(String.valueOf(post.getWatchesAmount()));
    }

    private void obtainToolbar(Toolbar toolbar, CompanyPointerModel author) {
        toolbar.getMenu().clear();
        if (user == null) return;

        if (user.getUId().equalsIgnoreCase(author.getId())) {
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
                              ImageView favoritesImageHolder, PostPointerModel post) {

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

        if (post.isFavoriteByMe()) favoritesImageHolder.setImageResource(R.drawable.ic_feed_icon_favorite_active);
        else favoritesImageHolder.setImageResource(R.drawable.ic_feed_icon_favorite);
    }

    private void obtainQuiz(TapeViewHolder holder, PostPointerModel post) {
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

    private void obtainCollage(final TapeViewHolder holder, PostPointerModel post) {
        if (post.getImages() == null || post.getImages().size() == 0) {
            holder.imagesRecyclerView.setVisibility(View.GONE);
            return;
        }

        CollageLayoutManager collageLayoutManager = new CollageLayoutManager(post.getImages());
        ImageCollageRecycler adapter = new ImageCollageRecycler(context, post.getImages());
        adapter.setOnItemClickListener(holder);

        holder.imagesRecyclerView.setVisibility(View.VISIBLE);
        holder.imagesRecyclerView.setLayoutManager(collageLayoutManager);
        holder.imagesRecyclerView.setHasFixedSize(true);
        holder.imagesRecyclerView.setAdapter(adapter);
    }

    public String getFirstPostId() {
        if (getItemCount() == FIRST_POSITION) return "null";
        return posts.get(FIRST_POSITION).getId();
    }

    public String getLastPostId() {
        return posts.get(getItemCount() - 1).getId();
    }

    public void addAhead(List<PostPointerModel> newPosts) {
        posts.addAll(FIRST_POSITION, newPosts);
        notifyItemInserted(newPosts.size() - 1);
    }

    public void addBehind(List<PostPointerModel> newPosts) {
        posts.addAll(newPosts);
        notifyItemInserted(posts.size());
    }

    public void refresh() {
        notifyItemRangeRemoved(0, getItemCount());
        posts.clear();
        if (isHeader) posts.add(new PostPointerModel());
    }
}
