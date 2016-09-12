package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.ui.adapters.recyclers.ContactPhonesRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.SocialIconInstallationResult;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by alexander on 06.08.16.
 */

public class CompanyAdditionalSettingsFragment extends BaseFragment
    implements ContactPhonesRecycler.OnContentChangedListener {

    @BindView(R.id.phones_recyclerview_additional_settings) RecyclerView phonesRecyclerView;
    @BindView(R.id.company_site_edittext) EditText companySiteEdt;

    private CompanyModel companyModel;
    private ContactPhonesRecycler contactPhonesRecycler;

    public CompanyAdditionalSettingsFragment() {
        super(R.layout.fragment_company_additional_settings);
    }

    public static CompanyAdditionalSettingsFragment getFragment(CompanyModel companyModel) {
        CompanyAdditionalSettingsFragment fragment = new CompanyAdditionalSettingsFragment();
        fragment.setCompany(companyModel);
        return fragment;
    }

    public void setCompany(CompanyModel company) {
        companyModel = company;
    }

    @Override
    protected void setupViews() {
        companySiteEdt.setText(companyModel.getSite());
        companySiteEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                companyModel.setSite(editable.toString());
            }
        });

        contactPhonesRecycler = new ContactPhonesRecycler(companyModel.getContactPhones());
        contactPhonesRecycler.setContentChangeListener(this);
        phonesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phonesRecyclerView.setAdapter(contactPhonesRecycler);
    }

    @Override
    public void onChanged(List<String> content) {
        companyModel.setContactPhones(content);
    }

    @OnClick(R.id.add_phone_button_settings_additional_fragment)
    public void onAddPhoneClick(View v) {
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contactPhonesRecycler.addPhone();
        phonesRecyclerView.setLayoutParams(lp);
    }

    @OnClick(R.id.social_links_chooser_button)
    public void onSocialIconsClick(View v) {
        Intent intent = new Intent(getContext(), SocialIconInstallationResult.class);
        startActivity(intent);
    }
}
