package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.NotificationPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.Palette;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alexander on 06.09.16.
 */
public class NotificationsRecycler extends RecyclerView.Adapter<NotificationsRecycler.NotificationsViewHolder> {

    private Context context;
    private List<NotificationPointerModel> notifications;

    public NotificationsRecycler(@NonNull List<NotificationPointerModel> notifications) {
        this.notifications = notifications;
    }

    public class NotificationsViewHolder extends BaseViewHolder {

        @BindView(R.id.notification_avatar) CircleImageView notificationAvatar;
        @BindView(R.id.notification_name) TextView notificationName;

        public NotificationsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false);
        return new NotificationsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        NotificationPointerModel notification = notifications.get(position);
        holder.notificationName.setText(notification.getText());
        obtainCompanyLogo(holder.notificationAvatar, notification.getAuthor().getName(), notification.getAuthor().getAvatar());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
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
