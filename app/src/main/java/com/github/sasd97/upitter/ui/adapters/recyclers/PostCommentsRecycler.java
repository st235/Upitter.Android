package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.CommentPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PlainUserPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by alexander on 04.09.16.
 */

public class PostCommentsRecycler extends RecyclerView.Adapter<PostCommentsRecycler.PostCommentsViewHolder> {

    private final int FIRST_POSITION = 0;

    private Context context;
    private List<CommentPointerModel> comments;

    public PostCommentsRecycler() {
        this.comments = new ArrayList<>();
    }

    public PostCommentsRecycler(@NonNull List<CommentPointerModel> comments) {
        this.comments = comments;
    }

    public class PostCommentsViewHolder extends BaseViewHolder {

        @BindView(R.id.avatar_comments_view) CircleImageView avatarCommentsImageView;
        @BindView(R.id.comment_comments_view) TextView commentCommentsTextView;
        @BindView(R.id.user_name_comments_view) TextView userTextCommentsTextView;


        public PostCommentsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public PostCommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        View v = LayoutInflater.from(context).inflate(R.layout.row_post_comments, parent, false);
        return new PostCommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostCommentsViewHolder holder, int position) {
        CommentPointerModel comment = comments.get(position);
        PlainUserPointerModel user = comment.getAuthor();

        holder.commentCommentsTextView.setText(comment.getText());
        holder.userTextCommentsTextView.setText(user.getName());
        obtainCompanyLogo(holder.avatarCommentsImageView, user.getName(), user.getLogoUrl());
    }

    private void obtainCompanyLogo(ImageView holder, String name, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(name);

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(55))
                    .height(Dimens.dpToPx(55))
                    .endConfig()
                    .buildRoundRect(preview,
                            ContextCompat.getColor(context, R.color.colorShadowDark),
                            Dimens.drr());

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(context)
                .load(logoUrl)
                .into(holder);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void add(CommentPointerModel comment) {
        this.comments.add(FIRST_POSITION, comment);
        notifyItemInserted(FIRST_POSITION);
    }

    public void addAll(Collection<CommentPointerModel> collection) {
        this.comments.addAll(collection);
        notifyItemRangeInserted(getItemCount() - 1, collection.size());
    }
}
