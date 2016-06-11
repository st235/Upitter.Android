package com.github.sasd97.upitter.utils;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.utils.comparators.CountryNameComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Alex on 09.06.2016.
 */
public class Countries extends AsyncTask<Void, Void, ArrayList<CountryModel>> {

    public interface OnLoadListener {
            void onLoad(ArrayList<CountryModel> list);
            void onError();
    }

    private static final String EMPTY = "";

    private static final String COUNTRIES_JSON = "countries.json";
    private static final String FIELD_COUNTRY_NAME = "name";
    private static final String FIELD_COUNTRY_NAME_OFFICIAL = "official";
    private static final String FIELD_COUNTRY_NAME_NATIVE = "native";
    private static final String FIELD_COUNTRY_CALLING_CODES = "callingCode";
    private static final String FIELD_COUNTRY_CODE = "cca2";
    private static final String FIELD_COUNTY_REGION = "region";
    private static ArrayList<CountryModel> countryList;

    private OnLoadListener listener;

    public static void obtainCountries(OnLoadListener listener) {
        Countries countries = new Countries(listener);
        countries.execute();
    }

    public static boolean isObtained() {
        return countryList != null;
    }

    public static ArrayList<CountryModel> getCountries() {
        return countryList;
    }

    private Countries(OnLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<CountryModel> doInBackground(Void... params) {
        try {

            ArrayList<CountryModel> result = new ArrayList<>();
            JSONArray array = new JSONArray(Assets.obtainJSONRepresentation(COUNTRIES_JSON));
            int arrayLength = array.length();

            for (int i = 0; i < arrayLength; i++) {

                JSONObject country = array.getJSONObject(i);
                JSONObject countryNames = country.getJSONObject(FIELD_COUNTRY_NAME);
                JSONObject nativeNames = countryNames.getJSONObject(FIELD_COUNTRY_NAME_NATIVE);
                String firstNativeName = nativeNames.keys().next();

                String OFFICIAL_NAME = countryNames.getString(FIELD_COUNTRY_NAME_OFFICIAL);
                String NATIVE_NAME = nativeNames.getJSONObject(firstNativeName).getString(FIELD_COUNTRY_NAME_OFFICIAL);
                String COUNTRY_CODE = country.getString(FIELD_COUNTRY_CODE);
                String COUNTRY_REGION = country.getString(FIELD_COUNTY_REGION);
                JSONArray COUNTRY_CALLING_CODES = country.getJSONArray(FIELD_COUNTRY_CALLING_CODES);

                if (OFFICIAL_NAME.equals(EMPTY)) continue;
                if (COUNTRY_CALLING_CODES.length() == 0) continue;

                if (COUNTRY_CALLING_CODES.length() == 1) {
                    result.add(new CountryModel
                            .Builder()
                            .name(OFFICIAL_NAME)
                            .nativeName(NATIVE_NAME)
                            .code(COUNTRY_CODE)
                            .region(COUNTRY_REGION)
                            .dialCode(COUNTRY_CALLING_CODES.getString(0))
                            .build());
                    continue;
                }

                for (String countryCode: ListUtils.toArray(String.class, COUNTRY_CALLING_CODES)) {
                    result.add(new CountryModel
                            .Builder()
                            .name(OFFICIAL_NAME)
                            .nativeName(NATIVE_NAME)
                            .code(COUNTRY_CODE)
                            .region(COUNTRY_REGION)
                            .dialCode(countryCode)
                            .build());
                }
            }

            Collections.sort(result, new CountryNameComparator());

            int headerId = -1;
            String lastHeader = "";

            for (CountryModel countryModel: result) {
                if (!TextUtils.equals(lastHeader, countryModel.getHeader())) {
                    lastHeader = countryModel.getHeader();
                    headerId++;
                }

                countryModel.setHeaderId(headerId);
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<CountryModel> countryModels) {
        super.onPostExecute(countryModels);

        if (countryModels == null || countryModels.size() == 0){
            listener.onError();
            return;
        }

        countryList = countryModels;
        listener.onLoad(countryModels);
    }
}
