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
import com.github.sasd97.upitter.models.response.pointers.SubscriberPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.Palette;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by alexander on 06.09.16.
 */
public class SubscribersRecycler extends RecyclerView.Adapter<SubscribersRecycler.SubscribersViewHolder> {

    private Context context;
    private List<SubscriberPointerModel> subscribers;

    public SubscribersRecycler(@NonNull List<SubscriberPointerModel> subscribers) {
        this.subscribers = subscribers;
    }

    public class SubscribersViewHolder extends BaseViewHolder {

        @BindView(R.id.subscriber_avatar) CircleImageView subscriberAvatar;
        @BindView(R.id.subscriber_name) TextView subscriberName;

        public SubscribersViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public SubscribersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_subscribers, parent, false);
        return new SubscribersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubscribersViewHolder holder, int position) {
        SubscriberPointerModel subscriber = subscribers.get(position);
        holder.subscriberName.setText(subscriber.getNick());
        obtainCompanyLogo(holder.subscriberAvatar, subscriber.getNick(), subscriber.getLogoUrl());
    }

    @Override
    public int getItemCount() {
        return subscribers.size();
    }

    private void obtainCompanyLogo(ImageView holder, String name, String logoUrl) {
        if (logoUrl == null) {
            String preview = Names.getNamePreview(name);

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .width(Dimens.dpToPx(50))
                    .height(Dimens.dpToPx(50))
                    .endConfig()
                    .buildRound(preview, Palette.getAvatarPalette());

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(context)
                .load(logoUrl)
                .into(holder);
    }
}
