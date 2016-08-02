package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by alexander on 28.06.16.
 */

public class ContactPhonesRecycler extends RecyclerView.Adapter<ContactPhonesRecycler.PhonesViewHolder> {

    private final String EMPTY_PHONE_HOLDER = "";
    private ArrayList<String> phones;

    public ContactPhonesRecycler() {
        phones = new ArrayList<>();
        phones.add(EMPTY_PHONE_HOLDER);
    }

    public class PhonesViewHolder extends BaseViewHolder implements TextWatcher {

        @BindView(R.id.edittext_phone_single_view) MaterialEditText phoneEditText;

        public PhonesViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            phoneEditText.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            phones.set(getAdapterPosition(), editable.toString());
        }
    }

    @Override
    public PhonesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_contact_phones, parent, false);
        return new PhonesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhonesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public void addPhone() {
        phones.add(EMPTY_PHONE_HOLDER);
        notifyItemInserted(phones.size());
    }

    public void removePhone(int position) {
        phones.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getPhones() {
        return phones;
    }
}
