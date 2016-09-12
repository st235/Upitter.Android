package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.SocialIconModel;
import com.github.sasd97.upitter.models.response.pointers.SocialIconPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alexander on 18.08.16.
 */
public class SocialIconsRecycler extends RecyclerView.Adapter<SocialIconsRecycler.SocialIconsViewHolder> {

    private Context context;
    private List<SocialIconModel> socialIcons;

    public SocialIconsRecycler() {
        this.socialIcons = new ArrayList<>();
    }

    public SocialIconsRecycler(@NonNull List<SocialIconModel> socialIcons) {
        this.socialIcons = socialIcons;
    }

    public class SocialIconsViewHolder extends BaseViewHolder {

        @BindView(R.id.social_icon) CircleImageView socialIcon;
        @BindView(R.id.social_link) MaterialEditText socialLink;

        public SocialIconsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public void onBindViewHolder(SocialIconsViewHolder holder, int position) {
        SocialIconModel socialIcon = socialIcons.get(position);

        holder.socialLink.setHint(socialIcon.getTitle());

        if (!socialIcon.isIcon()) return;

        Glide
                .with(context)
                .load(socialIcon.getIcon())
                .centerCrop()
                .into(holder.socialIcon);
    }

    @Override
    public SocialIconsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_social_icons, parent, false);
        return new SocialIconsViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return socialIcons.size();
    }

    public void addAll(Collection<SocialIconModel> collection) {
        socialIcons.addAll(collection);
        notifyItemRangeInserted(socialIcons.size(), socialIcons.size() + collection.size());
    }
}
