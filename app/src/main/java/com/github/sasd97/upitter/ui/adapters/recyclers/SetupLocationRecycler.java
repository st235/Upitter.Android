package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 19.08.16.
 */
public class SetupLocationRecycler extends RecyclerView.Adapter<SetupLocationRecycler.SetupLocationViewHolder> {

    public interface OnCoordinateClickListener {
        void onCoordinateClick(int position);
    }

    private OnCoordinateClickListener listener;
    private List<CoordinatesModel> coordinates;

    public SetupLocationRecycler(@NonNull List<CoordinatesModel> coordinates,
                                 @NonNull OnCoordinateClickListener listener) {
        super();
        this.listener = listener;
        this.coordinates = coordinates;
    }

    public class SetupLocationViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.geocoder_textview_address_single_view) TextView geocoderTxv;

        public SetupLocationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void setupViews() {

        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onCoordinateClick(getAdapterPosition());
        }
    }

    @Override
    public SetupLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_company_address_list, parent, false);
        return new SetupLocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetupLocationViewHolder holder, int position) {
        holder.geocoderTxv.setText(coordinates.get(position).getAddressName());
    }

    @Override
    public int getItemCount() {
        return coordinates.size();
    }
}
