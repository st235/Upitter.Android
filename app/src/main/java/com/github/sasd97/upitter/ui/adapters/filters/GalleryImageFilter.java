package com.github.sasd97.upitter.ui.adapters.filters;

import android.widget.Filter;

import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;
import com.github.sasd97.upitter.ui.adapters.GalleryRecyclerAdapter;
import com.github.sasd97.upitter.utils.Names;

import java.util.ArrayList;

/**
 * Created by Alex on 07.02.2016.
 */

public class GalleryImageFilter extends Filter {

    private GalleryRecyclerAdapter galleryRecyclerAdapter;

    private ArrayList<ImageSkeleton> originalList;
    private ArrayList<ImageSkeleton> filteredList = new ArrayList<>();

    public GalleryImageFilter(GalleryRecyclerAdapter adapter, ArrayList<ImageSkeleton> list) {
        super();

        galleryRecyclerAdapter = adapter;
        originalList = list;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        filteredList.clear();

        for(ImageSkeleton item: originalList)
            if (Names.inFolder(constraint.toString(), item.getPath())) filteredList.add(item);

        filterResults.values = filteredList;
        filterResults.count = filteredList.size();

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        galleryRecyclerAdapter.filter((ArrayList<ImageSkeleton>) results.values);
    }
}
