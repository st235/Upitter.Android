package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 28.06.16.
 */

public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder> {

    private List<CoordinatesModel> coordinates;

    public AddressRecyclerAdapter(@NonNull List<CoordinatesModel> coordinates) {
        this.coordinates = coordinates;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        private TextView geocoderTextView;

        public AddressViewHolder(View itemView) {
            super(itemView);
            geocoderTextView = (TextView) itemView.findViewById(R.id.geocoder_textview_address_single_view);
        }
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.address_single_view, parent, false);
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

