package com.github.sasd97.upitter.ui.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnApplyLongListener;
import com.github.sasd97.upitter.events.OnEditImageChooseListener;
import com.github.sasd97.upitter.ui.adapters.FilterChooseRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.filters.GPUImageFilterTools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by Alex on 10.05.2016.
 */

public class FilterFragment extends BaseFragment
        implements FilterChooseRecyclerAdapter.OnFilterChooseListener,
        OnEditImageChooseListener {

    private static final String IMAGE_PATH = "IMAGE_PATH";

    private GPUImageView gpuImageView = null;
    private RecyclerView recyclerView = null;

    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private GPUImageFilterTools.FilterList filters = new GPUImageFilterTools.FilterList();

    private Integer filterPreviews[] = new Integer[] { R.drawable.filter_preview_default, R.drawable.filter_preview_1997, R.drawable.filter_preview_amaro, R.drawable.filter_preview_brannan,
            R.drawable.filter_preview_earlybird, R.drawable.filter_preview_hefe, R.drawable.filter_preview_hudson, R.drawable.filter_preview_inkwell, R.drawable.filter_preview_lomo, R.drawable.filter_preview_lorkelvin,
            R.drawable.filter_preview_nashville, R.drawable.filter_preview_rise, R.drawable.filter_preview_sierra, R.drawable.filter_preview_sutro, R.drawable.filter_preview_toaster, R.drawable.filter_preview_valencia,
            R.drawable.filter_preview_walden, R.drawable.filter_preview_xproll, R.drawable.filter_preview_contrast, R.drawable.filter_preview_brightness, R.drawable.filter_preview_sepia, R.drawable.filter_preview_vignette,
            R.drawable.filter_preview_tonecurve, R.drawable.filter_preview_lookup };

    public static FilterFragment getInstance(String path) {
        FilterFragment filterFragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_PATH, path);
        filterFragment.setArguments(bundle);
        return filterFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String path = getArguments().getString(IMAGE_PATH);

        gpuImageView = (GPUImageView) findViewById(R.id.image_filters);
        recyclerView = (RecyclerView) findViewById(R.id.filters_recycler_view);
        Log.d("HERE_!", path);

        Glide
                .with(this)
                .load(Names
                    .getInstance()
                    .getFilePath(path)
                    .toString())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        setImage(resource);

                    }
                });

        ArrayList<Integer> arrayList = new ArrayList<Integer>(Arrays.asList(filterPreviews));
        FilterChooseRecyclerAdapter filterChooseRecyclerAdapter = new FilterChooseRecyclerAdapter(
                getActivity(),
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.filter_list))),
                arrayList
                );



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        filterChooseRecyclerAdapter.setListener(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(filterChooseRecyclerAdapter);
        gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
        initData();
    }

    @Override
    protected void bindViews() {

    }

    public void setImage(Bitmap image) {
        float width = image.getWidth();
        float height = image.getHeight();
        float ratio = width / height;
        gpuImageView.setRatio(ratio);
        gpuImageView.setImage(image);
    }

    @Override
    public void onChoose(int position) {
        if (position == 0) {
            switchFilterTo(new GPUImageFilter());
        } else {
            GPUImageFilter filter = GPUImageFilterTools
                    .createFilterForType(getActivity(),
                            filters.filters.get(position));
            switchFilterTo(filter);
        }

        gpuImageView.requestRender();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData(){
        filters.addFilter("default", GPUImageFilterTools.FilterType.I_1977);
        filters.addFilter("1977", GPUImageFilterTools.FilterType.I_1977);
        filters.addFilter("Amaro", GPUImageFilterTools.FilterType.I_AMARO);
        filters.addFilter("Brannan", GPUImageFilterTools.FilterType.I_BRANNAN);
        filters.addFilter("Earlybird", GPUImageFilterTools.FilterType.I_EARLYBIRD);
        filters.addFilter("Hefe", GPUImageFilterTools.FilterType.I_HEFE);
        filters.addFilter("Hudson", GPUImageFilterTools.FilterType.I_HUDSON);
        filters.addFilter("Inkwell", GPUImageFilterTools.FilterType.I_INKWELL);
        filters.addFilter("Lomo", GPUImageFilterTools.FilterType.I_LOMO);
        filters.addFilter("LordKelvin", GPUImageFilterTools.FilterType.I_LORDKELVIN);
        filters.addFilter("Nashville", GPUImageFilterTools.FilterType.I_NASHVILLE);
        filters.addFilter("Rise", GPUImageFilterTools.FilterType.I_NASHVILLE);
        filters.addFilter("Sierra", GPUImageFilterTools.FilterType.I_SIERRA);
        filters.addFilter("sutro", GPUImageFilterTools.FilterType.I_SUTRO);
        filters.addFilter("Toaster", GPUImageFilterTools.FilterType.I_TOASTER);
        filters.addFilter("Valencia", GPUImageFilterTools.FilterType.I_VALENCIA);
        filters.addFilter("Walden", GPUImageFilterTools.FilterType.I_WALDEN);
        filters.addFilter("Xproll", GPUImageFilterTools.FilterType.I_XPROII);
        filters.addFilter("Contrast", GPUImageFilterTools.FilterType.CONTRAST);
        filters.addFilter("Brightness", GPUImageFilterTools.FilterType.BRIGHTNESS);
        filters.addFilter("Sepia", GPUImageFilterTools.FilterType.SEPIA);
        filters.addFilter("Vignette", GPUImageFilterTools.FilterType.VIGNETTE);
        filters.addFilter("ToneCurve", GPUImageFilterTools.FilterType.TONE_CURVE);
        filters.addFilter("Lookup (Amatorka)", GPUImageFilterTools.FilterType.LOOKUP_AMATORKA);
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(
                filter.getClass()))) {
            mFilter = filter;
            gpuImageView.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
        }
    }

    @Override
    public void save(final OnApplyLongListener listener) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "UPITTER_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        final File destination = new File(storageDir, "/LocLook/"+imageFileName);

        gpuImageView.saveToPictures("Upitter", imageFileName, new GPUImageView.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(Uri uri) {
                listener.onApplied(destination.getAbsolutePath());
            }
        });
    }
}
