package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.SocialIconPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 18.08.16.
 */

public class SocialIconsPreviewRecycler extends RecyclerView.Adapter<SocialIconsPreviewRecycler.SocialIconsPreviewViewHolder> {

    private Context context;
    private List<SocialIconPointerModel> socialIcons;

    public SocialIconsPreviewRecycler(@NonNull List<SocialIconPointerModel> socialIcons) {
        this.socialIcons = socialIcons;
    }

    public class SocialIconsPreviewViewHolder extends BaseViewHolder {

        @BindView(R.id.social_link_preview) ImageView socialIcon;

        public SocialIconsPreviewViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
        }
    }

    @Override
    public void onBindViewHolder(SocialIconsPreviewViewHolder holder, int position) {
        SocialIconPointerModel socialIcon = socialIcons.get(position);
        if (!socialIcon.isIcon()) return;

        Glide
                .with(context)
                .load(socialIcon.getIcon())
                .centerCrop()
                .into(holder.socialIcon);
    }

    @Override
    public SocialIconsPreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_social_icons_preview, parent, false);
        return new SocialIconsPreviewViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return socialIcons.size();
    }
}
