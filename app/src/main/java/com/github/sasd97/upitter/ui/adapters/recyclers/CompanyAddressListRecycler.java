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
 * Created by alexander on 28.06.16.
 */

public class CompanyAddressListRecycler extends RecyclerView.Adapter<CompanyAddressListRecycler.AddressViewHolder> {

    private List<CoordinatesModel> coordinates;

    public CompanyAddressListRecycler(@NonNull List<CoordinatesModel> coordinates) {
        this.coordinates = coordinates;
    }

    public class AddressViewHolder extends BaseViewHolder {

        @BindView(R.id.geocoder_textview_address_single_view) TextView geocoderTextView;

        public AddressViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
        }
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_company_address_list, parent, false);
        return new AddressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        CoordinatesModel coordinatesModel = coordinates.get(position);

        if (coordinatesModel.getAddress() != null)
            holder.geocoderTextView.setText(coordinatesModel.getAddress().getAddressLine(0));
    }

    @Override
    public int getItemCount() {
        return coordinates.size();
    }

    public void addItem(CoordinatesModel coordinatesModel) {
        coordinates.add(coordinatesModel);
        notifyItemInserted(coordinates.size());
    }

    public List<CoordinatesModel> getCoordinates() {
        return coordinates;
    }
}

