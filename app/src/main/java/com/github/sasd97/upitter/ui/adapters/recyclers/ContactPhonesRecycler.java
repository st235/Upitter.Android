package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
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
import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 28.06.16.
 */

public class ContactPhonesRecycler extends RecyclerView.Adapter<ContactPhonesRecycler.PhonesViewHolder> {

    public interface OnContentChangedListener {
        void onChanged(List<String> content);
    }

    private final String EMPTY_PHONE_HOLDER = "";

    private List<String> phones;
    private OnContentChangedListener listener;

    public ContactPhonesRecycler() {
        phones = new ArrayList<>();
        phones.add(EMPTY_PHONE_HOLDER);
    }

    public ContactPhonesRecycler(List<String> phones) {
        this.phones = phones;
        if (phones.size() == 0) phones.add(EMPTY_PHONE_HOLDER);
    }

    public void setContentChangeListener(@NonNull OnContentChangedListener listener) {
        this.listener = listener;
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
            if (listener != null) listener.onChanged(phones);
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
        String phone = phones.get(position);
        if (!phone.equalsIgnoreCase(EMPTY_PHONE_HOLDER)) {
            holder.phoneEditText.setText(phone);
        }
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

    public List<String> getPhones() {
        return phones;
    }
}
