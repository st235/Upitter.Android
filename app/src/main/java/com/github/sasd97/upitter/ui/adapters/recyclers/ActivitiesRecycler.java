package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.ActivityPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 28.06.16.
 */

public class ActivitiesRecycler extends RecyclerView.Adapter<ActivitiesRecycler.PhonesViewHolder> {

    private List<ActivityPointerModel> activities;

    public ActivitiesRecycler(List<ActivityPointerModel> activities) {
        this.activities = activities;
    }

    public class PhonesViewHolder extends BaseViewHolder {

        @BindView(R.id.preview_activity) TextView activityPreview;

        public PhonesViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public PhonesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_activity, parent, false);
        return new PhonesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhonesViewHolder holder, int position) {
        ActivityPointerModel phone = activities.get(position);
        holder.activityPreview.setText(phone.getTitle());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }
}
