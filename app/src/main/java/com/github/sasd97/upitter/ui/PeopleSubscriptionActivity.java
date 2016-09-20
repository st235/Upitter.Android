package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.models.response.pointers.PlainCompanyPointerModel;
import com.github.sasd97.upitter.services.query.UserSubscriptionsQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.PeopleSubscriptionsRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import static com.github.sasd97.upitter.Upitter.getHolder;

public class PeopleSubscriptionActivity extends BaseActivity
        implements UserSubscriptionsQueryService.OnSubscriptionsListener {

    private PeopleModel people;
    private UserSubscriptionsQueryService queryService;
    private PeopleSubscriptionsRecycler peopleSubscriptionsRecycler;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @BindView(R.id.subscription_list) RecyclerView subscriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_subscription);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        queryService = UserSubscriptionsQueryService.getService(this);
        people = ((PeopleHolder) getHolder()).get();

        peopleSubscriptionsRecycler = new PeopleSubscriptionsRecycler();
        subscriptionList.setLayoutManager(linearLayoutManager);
        subscriptionList.setAdapter(peopleSubscriptionsRecycler);

        queryService.obtainSubscriptions(people.getAccessToken());
    }

    @Override
    public void onSubscriptionsObtained(List<PlainCompanyPointerModel> subscriptions) {
        peopleSubscriptionsRecycler.addAll(subscriptions);
    }

    @Override
    public void onError(ErrorModel error) {

    }
}
