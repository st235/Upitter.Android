package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.SocialIconModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.SocialIconPointerModel;
import com.github.sasd97.upitter.services.query.SocialIconsQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.SocialIconsRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.ListUtils;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.COMPANY_SOCIAL_ICONS;

public class SocialIconInstallationResult extends BaseActivity
        implements SocialIconsQueryService.OnSocialIconsListener {

    private UserModel userModel;
    private SocialIconsQueryService queryService;
    private SocialIconsRecycler socialIconsRecycler;

    private List<SocialIconModel> socialIcons;

    @BindView(R.id.social_icons_recycler_view) RecyclerView socialIconsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_icons);
        ButterKnife.bind(this);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);

        userModel = getHolder().get();
        queryService = SocialIconsQueryService.getService(this);
        queryService.obtainIconsList(userModel.getAccessToken());

        socialIcons = getIntent().getParcelableArrayListExtra(COMPANY_SOCIAL_ICONS);

        socialIconsRecycler = new SocialIconsRecycler();
        socialIconsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        socialIconsRecyclerView.setAdapter(socialIconsRecycler);
    }

    @Override
    public void onObtainSocialIcons(List<SocialIconPointerModel> list) {
        List<SocialIconModel> icons = ListUtils.mutate(list, new ListUtils.OnListMutateListener<SocialIconPointerModel, SocialIconModel>() {
            @Override
            public SocialIconModel mutate(SocialIconPointerModel object) {
                return new SocialIconModel
                        .Builder()
                        .id(object.getId())
                        .icon(object.getIcon())
                        .link(object.getLink())
                        .title(object.getTitle())
                        .build();
            }
        });

        findMatch(icons, new LinkedList<>(socialIcons));
        socialIconsRecycler.addAll(icons);
    }

    private void findMatch(List<SocialIconModel> original, LinkedList<SocialIconModel> sub) {
        if (sub == null || sub.size() == 0) return;

        origin: for (SocialIconModel o: original) {
            for (SocialIconModel s : sub) {
                if (s.getId().equalsIgnoreCase(o.getId())) {
                    o.setLink(s.getLink());
                    sub.remove(s);
                    continue origin;
                }
            }
        }
    }

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
    }

    @OnClick(R.id.fab)
    public void onResult(View v) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(COMPANY_SOCIAL_ICONS, socialIconsRecycler.getList());
        setResult(RESULT_OK, intent);
        finish();
    }
}
