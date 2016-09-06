package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.SubscriberPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alexander on 06.09.16.
 */
public class SubscribersRecycler extends RecyclerView.Adapter<SubscribersRecycler.SubscribersViewHolder> {

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
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_subscribers, parent, false);
        return new SubscribersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubscribersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
