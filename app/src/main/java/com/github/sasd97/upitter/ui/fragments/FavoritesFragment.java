package com.github.sasd97.upitter.ui.fragments;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.behaviors.OnEndlessRecyclerViewScrollListener;
import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.services.query.FavoritesQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedPostRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;

/**
 * Created by alexander on 08.07.16.
 */

public class FavoritesFragment extends BaseFragment implements FavoritesQueryService.OnFavoritesObtainListener {

    @BindView(R.id.recycler_favorites_fragment) RecyclerView recyclerView;

    private FavoritesQueryService queryService;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private FeedPostRecycler feedPostRecycler;

    private UserModel userModel;

    public FavoritesFragment() {
        super(R.layout.fragment_favorites);
    }

    public static FavoritesFragment getFragment() {
        return new FavoritesFragment();
    }

    @Override
    protected void setupViews() {
        userModel = getHolder().get();
        queryService = FavoritesQueryService.getService(this);
        queryService.obtainFavorites(userModel.getAccessToken());

        feedPostRecycler = new FeedPostRecycler(getContext(), userModel, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedPostRecycler);

        recyclerView.addOnScrollListener(new OnEndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Logger.i("Infinity scroll detected");
                Logger.i(String.valueOf(feedPostRecycler.getItemCount()));
                if (feedPostRecycler.getItemCount() < 20) return;

                queryService.obtainOldFavorites(
                        userModel.getAccessToken(),
                        feedPostRecycler.getLastPostId());
            }
        });
    }

    @Override
    public void onFavoritesObtained(List<PostPointerModel> favorites) {
        feedPostRecycler.addAll(favorites);
    }

    @Override
    public void onOldFavoritesObtained(List<PostPointerModel> favorites) {
        feedPostRecycler.addBehind(favorites);
    }

    @Override
    public void onError(ErrorModel error) {

    }
}
