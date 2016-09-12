package com.github.sasd97.upitter.ui.results;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.sasd97.upitter.Upitter.getHolder;

public class SocialIconInstallationResult extends BaseActivity
        implements SocialIconsQueryService.OnSocialIconsListener {

    private UserModel userModel;
    private SocialIconsQueryService queryService;
    private SocialIconsRecycler socialIconsRecycler;

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
                        .icon(object.getIcon())
                        .link(object.getLink())
                        .title(object.getTitle())
                        .build();
            }
        });
        socialIconsRecycler.addAll(icons);
    }

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
    }
}
