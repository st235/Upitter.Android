package com.github.sasd97.upitter.utils;

import android.util.Log;

import com.github.sasd97.upitter.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 29.07.16.
 */
public class Maps {

    private static final String TAG = "Maps Utils";

    private Maps() {}

    public static String obtainPathUrl(double srcLatitude,
                                       double srcLongitude,
                                       double destLatitude,
                                       double destLongitude) {
        StringBuilder url = new StringBuilder();

        url.append("https://maps.googleapis.com/maps/api/directions/json");
        url.append("?origin=");
        url.append(Double.toString(srcLatitude));
        url.append(",");
        url.append(Double.toString(srcLongitude));
        url.append("&destination=");
        url.append(Double.toString(destLatitude));
        url.append(",");
        url.append(Double.toString(destLongitude));
        url.append("&sensor=false&mode=driving&alternatives=true");

        Logger.d(url.toString());
        return url.toString();
    }

    public static void drawPath(GoogleMap map,
                         String jsonResponse) {
        Logger.json(jsonResponse);
        try {
            final JSONObject json = new JSONObject(jsonResponse);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Palette.obtainColor(R.color.colorPrimary))
                    .geodesic(true)
            );
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
