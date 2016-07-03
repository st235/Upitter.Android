package com.github.sasd97.upitter.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.github.sasd97.upitter.models.CoordinatesModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 03.07.2016.
 */
public class GeocoderService extends AsyncTask<CoordinatesModel, Void, CoordinatesModel> {

    public interface OnAddressListener {
        void onAddressReady(CoordinatesModel coordinatesModel);
        void onAddressFail();
    }

    private Geocoder geocoder;
    private OnAddressListener listener;

    private GeocoderService(Context context, OnAddressListener listener) {
        this.geocoder = new Geocoder(context, Locale.ENGLISH);
        this.listener = listener;
    }

    public static void find(Context context, CoordinatesModel coordinatesModel, OnAddressListener listener) {
        GeocoderService service = new GeocoderService(context, listener);
        service.execute(coordinatesModel);
    }

    @Override
    protected CoordinatesModel doInBackground(CoordinatesModel... coordinatesModels) {
        CoordinatesModel location = coordinatesModels[0];

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses == null || addresses.size() == 0) return null;

            return new CoordinatesModel
                    .Builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .address(addresses.get(0))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(CoordinatesModel coordinatesModel) {
        super.onPostExecute(coordinatesModel);

        if (coordinatesModel == null) listener.onAddressFail();
        listener.onAddressReady(coordinatesModel);
    }
}
