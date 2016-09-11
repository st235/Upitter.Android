package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 28.06.16.
 */

public class ContactPhonesPreviewRecycler extends RecyclerView.Adapter<ContactPhonesPreviewRecycler.PhonesViewHolder> {

    private List<String> phones;

    public ContactPhonesPreviewRecycler(List<String> phones) {
        this.phones = phones;
    }

    public class PhonesViewHolder extends BaseViewHolder {

        @BindView(R.id.preview_phone) TextView phonePreview;

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
        View v = LayoutInflater.from(context).inflate(R.layout.row_contact_phones_preview, parent, false);
        return new PhonesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhonesViewHolder holder, int position) {
        String phone = phones.get(position);
        holder.phonePreview.setText(phone);
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }
}
