package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.content.Intent;
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
import com.github.sasd97.upitter.holders.UserHolder;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.CommentPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PlainUserPointerModel;
import com.github.sasd97.upitter.ui.CompanyBCProfileActivity;
import com.github.sasd97.upitter.ui.CompanyBPProfileActivity;
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

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COMPANY_ALIAS;

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

    public class PostCommentsViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.avatar_comments_view) CircleImageView avatarCommentsImageView;
        @BindView(R.id.comment_comments_view) TextView commentCommentsTextView;
        @BindView(R.id.user_name_comments_view) TextView userTextCommentsTextView;
        @BindView(R.id.comment_created_date) TextView commentCreatedDate;


        public PostCommentsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void setupViews() {}

        @Override
        public void onClick(View view) {
            PlainUserPointerModel user = comments.get(getAdapterPosition()).getAuthor();
            if (Long.valueOf(user.getId()) < 0) {
                Class<?> target = UserHolder.getUserType() == UserModel.UserType.People.getValue() ? CompanyBPProfileActivity.class : CompanyBCProfileActivity.class;
                Intent intent = new Intent(context, target);
                intent.putExtra(COMPANY_ALIAS, user.getAlias());
                context.startActivity(intent);
                return;
            }
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
        holder.commentCreatedDate.setText(comment.getCreationDate());
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

    public void addToEnd(CommentPointerModel comment) {
        this.comments.add(comment);
        notifyItemInserted(getItemCount() - 1);
    }

    public CommentPointerModel getLast() {
        return comments.get(getItemCount() - 1);
    }

    public void addAll(Collection<CommentPointerModel> collection) {
        this.comments.addAll(collection);
        notifyItemRangeInserted(getItemCount() - 1, collection.size());
    }
}
