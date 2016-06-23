package com.github.sasd97.upitter.utils.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.utils.Countries;
import com.github.sasd97.upitter.utils.ListUtils;

/**
 * Created by alexander on 21.06.16.
 */
public class DialCodeWatcher implements TextWatcher {

    public interface OnCountryReadyListener {
        void onCountryReady(CountryModel country);
        void onNotCountry();
    }

    private int cursorComplement;
    private boolean editedFlag = false;
    private boolean backspacingFlag = false;

    private static final String PLUS_SIGN = "+";
    private static final String EMPTY_SIGN = "";
    private static final String DECIMAL_REGEX = "[^\\d]";

    private EditText editText;
    private OnCountryReadyListener listener;

    public DialCodeWatcher(EditText editText, OnCountryReadyListener listener) {
        this.listener = listener;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        cursorComplement = charSequence.length() - editText.getSelectionStart();

        if (count > after) backspacingFlag = true;
        else backspacingFlag = false;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        final String text = editable.toString();

        CountryModel countryModel = ListUtils.select(Countries.getCountries(), new ListUtils.OnSelectListener<CountryModel>() {
            @Override
            public boolean isSelectable(CountryModel other) {
                return other.getDialCode().equals(text.replaceAll(DECIMAL_REGEX, ""));
            }
        });

        if (countryModel != null) listener.onCountryReady(countryModel);
        else listener.onNotCountry();

        if (editedFlag) {
            editedFlag = false;
            return;
        }

        if (!backspacingFlag) {
            editedFlag = true;

            if (text.equalsIgnoreCase(PLUS_SIGN)) {
                editable.replace(0, 1, "");
                return;
            }

            if (!text.contains(PLUS_SIGN))
                editable.insert(0, PLUS_SIGN);

            editText.setSelection(editText.getText().length() - cursorComplement);
        }
    }
}
