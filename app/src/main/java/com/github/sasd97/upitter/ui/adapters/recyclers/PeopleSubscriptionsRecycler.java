package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.PlainCompanyPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.Dimens;
import com.github.sasd97.upitter.utils.Names;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Alexadner Dadukin on 20.09.2016.
 */

public class PeopleSubscriptionsRecycler extends RecyclerView.Adapter<PeopleSubscriptionsRecycler.PeopleSubscriptionsViewHolder> {

    private Context context;
    private List<PlainCompanyPointerModel> companies;

    public PeopleSubscriptionsRecycler() {
        this.companies = new ArrayList<>();
    }

    class PeopleSubscriptionsViewHolder extends BaseViewHolder {

        @BindView(R.id.company_logo_preview) ImageView companyLogoPreview;
        @BindView(R.id.company_text) TextView companyText;

        public PeopleSubscriptionsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    private void obtainCompanyLogo(PlainCompanyPointerModel company, ImageView holder) {
        if (company.getLogoUrl() == null) {
            String preview = Names.getNamePreview(company.getName());

            TextDrawable textDrawable = TextDrawable
                    .builder()
                    .buildRoundRect(preview,
                            ContextCompat.getColor(context, R.color.colorShadowDark),
                            Dimens.dpToPx(4));

            holder.setImageDrawable(textDrawable);
            return;
        }

        Glide
                .with(context)
                .load(company.getLogoUrl())
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, Dimens.drr(), 0))
                .into(holder);
    }


    @Override
    public PeopleSubscriptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_people_subscriptions, parent, false);
        return new PeopleSubscriptionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PeopleSubscriptionsViewHolder holder, int position) {
        PlainCompanyPointerModel company = companies.get(position);
        obtainCompanyLogo(company, holder.companyLogoPreview);
        holder.companyText.setText(company.getName());
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void addAll(Collection<PlainCompanyPointerModel> companies) {
        this.companies.addAll(companies);
        notifyItemInserted(getItemCount());
    }
}
