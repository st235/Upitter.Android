package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CategoryModel;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.utils.Categories;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Palette;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by alexander on 08.07.16.
 */
public class TapeRecyclerAdapter extends RecyclerView.Adapter<TapeRecyclerAdapter.TapeViewHolder> {

    private Context context;
    private List<PostResponseModel> posts;

    public TapeRecyclerAdapter(Context context) {
        posts = new ArrayList<>();
    }

    public class TapeViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private TextView postTitleTextView;
        private TextView postDescriptionTextView;
        private TextView categoryNameTextView;
        private TextView likeAmountTextView;
        private TextView commentsAmountTextView;

        private CircleImageView userAvatarImageView;
        private CircleImageView categoryImageView;
        private ImageView likeImageView;
        private ImageView commentImageView;
        private ShineButton favoriteImageView;

        private LinearLayout likeLinearLayout;
        private LinearLayout commentLinearLayout;
        private LinearLayout favoriteLinearLayout;

        public TapeViewHolder(View itemView) {
            super(itemView);

            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_post_single_view);
            postTitleTextView = (TextView) itemView.findViewById(R.id.title_post_single_view);
            postDescriptionTextView = (TextView) itemView.findViewById(R.id.text_post_single_view);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.category_label_post_single_view);
            likeAmountTextView = (TextView) itemView.findViewById(R.id.like_counter_post_single_view);
            commentsAmountTextView = (TextView) itemView.findViewById(R.id.comments_counter_post_single_view);

            userAvatarImageView = (CircleImageView) itemView.findViewById(R.id.user_icon_post_single_view);
            categoryImageView = (CircleImageView) itemView.findViewById(R.id.category_preview_post_single_view);
            likeImageView = (ImageView) itemView.findViewById(R.id.like_icon_post_single_view);
            commentImageView = (ImageView) itemView.findViewById(R.id.comments_icon_post_single_view);
            favoriteImageView = (ShineButton) itemView.findViewById(R.id.favorites_icon_post_single_view);

            likeLinearLayout = (LinearLayout) itemView.findViewById(R.id.like_layout_post_single_view);
            commentLinearLayout = (LinearLayout) itemView.findViewById(R.id.comments_layout_post_single_view);
            favoriteLinearLayout = (LinearLayout) itemView.findViewById(R.id.favorite_layout_post_single_view);
        }
    }

    @Override
    public TapeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.post_single_view, parent, false);
        return new TapeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TapeViewHolder holder, int position) {
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

    private void obtainAvatar(CircleImageView holder, CompanyResponseModel author) {
        if (author.getLogoUrl() == null) {
            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(50))
                    .height(Dimens.dpToPx(50))
                    .endConfig()
                    .buildRound(author.getPreview(), Palette.getAvatarPalette());
            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(context)
                .load(author.getLogoUrl())
                .into(holder);
    }

    private void obtainPost(TapeViewHolder holder, PostResponseModel post) {
        obtainCategory(holder.categoryImageView, holder.categoryNameTextView, post.getCategoryId());

        holder.postTitleTextView.setText(post.getTitle());
        holder.postDescriptionTextView.setText(post.getText());
    }

    private void obtainCategory(CircleImageView imageHolder, TextView textHolder, String cid) {
        CategoryModel currentCategory = Categories.find(cid);
        textHolder.setText(currentCategory.getTitle());
        imageHolder.setImageResource(currentCategory.getDrawable());
    }

    private void obtainSubBar(ImageView likesImageHolder, TextView likesTextHolder,
                              ImageView commentsImageHolder, TextView commentsTextHolder,
                              ImageView favoritesImageHolder, PostResponseModel post) {

    }
}
