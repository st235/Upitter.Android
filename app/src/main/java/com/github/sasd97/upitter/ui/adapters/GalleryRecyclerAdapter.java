package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.NumerableCheckView;
import com.github.sasd97.upitter.events.OnGalleryInteractionListener;
import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;
import com.github.sasd97.upitter.ui.adapters.filters.GalleryImageFilter;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.mutators.PhotoMutator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 31.01.2016.
 */

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ImageChooserViewHolder>
                                        implements Filterable {

    private Context context;

    private boolean isMultiSelectionMode;

    private int selectItemCounter = 0;
    private int maxSelectItemsAmount = 3;

    private ArrayList<ImageSkeleton> multiSelectedItems;

    private ArrayList<ImageSkeleton> imagesPathsOriginals;
    private static ArrayList<ImageSkeleton> imagesPathsFiltered;

    private static OnGalleryInteractionListener onGalleryInteractionListener;

    public GalleryRecyclerAdapter(Context context, ArrayList<ImageSkeleton> imagesPaths, boolean isMultiSelectionMode, int maxSelectItemsAmount) {
        this.context = context;

        this.isMultiSelectionMode = isMultiSelectionMode;
        this.maxSelectItemsAmount = maxSelectItemsAmount;

        this.imagesPathsOriginals = imagesPaths;

        if (isMultiSelectionMode) multiSelectedItems = new ArrayList<>(maxSelectItemsAmount);
        imagesPathsFiltered = (ArrayList<ImageSkeleton>) imagesPaths.clone();
    }

    public <T extends AppCompatActivity & OnGalleryInteractionListener> void setOnImageChooserListener(T context) {
        onGalleryInteractionListener = context;
    }

    public void setOnImageChooserListener(OnGalleryInteractionListener listener) {
        onGalleryInteractionListener = listener;
    }

    public class ImageChooserViewHolder extends RecyclerView.ViewHolder
                                                implements View.OnClickListener {

        private ImageView imagePreview;
        private NumerableCheckView multiSelectCheckBox;

        public ImageChooserViewHolder(View itemView) {
            super(itemView);

            imagePreview = (ImageView) itemView.findViewById(R.id.image_preview_gallery_thumbnail_single_view);
            multiSelectCheckBox = (NumerableCheckView) itemView.findViewById(R.id.multi_select_checkbox_gallery_thumbnail_single_view);

            if (!isMultiSelectionMode) {
                itemView.setOnClickListener(this);
                multiSelectCheckBox.setVisibility(View.GONE);
                return;
            }

            multiSelectCheckBox.setClickable(true);
            multiSelectCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isMultiSelectionMode) {
                boolean checkState = !imagesPathsFiltered.get(getAdapterPosition()).isChecked();
                if (checkState && (selectItemCounter >= maxSelectItemsAmount || selectItemCounter < 0)) return; //overflow
                final int currentPosition = getAdapterPosition();
                final ImageSkeleton currentItem = imagesPathsFiltered.get(currentPosition);

                if (checkState) {
                    changeState(currentItem, true, selectItemCounter + 1);
                    currentItem.setPosition(currentPosition);
                    multiSelectedItems.add(selectItemCounter, currentItem);
                    selectItemCounter++;
                }
                else {
                    selectItemCounter--;
                    multiSelectedItems.remove(currentItem.getSelectPosition() - 1);
                    changeState(currentItem, false, selectItemCounter);

                    int counter = 1;
                    for (ImageSkeleton items: multiSelectedItems) {
                        items.setSelectPosition(counter);
                        notifyItemChanged(items.getPosition());
                        counter++;
                    }
                }

                onGalleryInteractionListener.onMultiChoose(currentPosition, selectItemCounter, currentItem);
                return;
            }

            if (onGalleryInteractionListener != null) {
                onGalleryInteractionListener.onSingleChoose(getAdapterPosition(), imagesPathsFiltered.get(getAdapterPosition()));
            }
        }

        private void changeState(ImageSkeleton item, boolean state, int position) {
            item.setSelectPosition(position);
            item.setCheck(state);
            multiSelectCheckBox.setPosition(position);
            multiSelectCheckBox.setChecked(state);
        }
    }

    @Override
    public ImageChooserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail_single_view, parent, false);
        return new ImageChooserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageChooserViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(Names
                        .getInstance()
                        .getFilePath(imagesPathsFiltered.get(position).getPath())
                        .toString())
                .centerCrop()
                .into(holder.imagePreview);

        holder.multiSelectCheckBox.setPosition(imagesPathsFiltered.get(position).getSelectPosition());
        holder.multiSelectCheckBox.setChecked(imagesPathsFiltered.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return imagesPathsFiltered.size();
    }

    public void addAll(List<ImageSkeleton> list) {
        imagesPathsOriginals.addAll(list);
        notifyItemInserted(imagesPathsOriginals.size());
    }

    @Override
    public Filter getFilter() {
        return new GalleryImageFilter(this, imagesPathsOriginals);
    }

    public void filterData(ArrayList<ImageSkeleton> filteredData) {
        imagesPathsFiltered.clear();
        imagesPathsFiltered = filteredData;
        notifyDataSetChanged();
    }

    public ArrayList<String> getFiltered() {
        return PhotoMutator.backMutate(imagesPathsFiltered);
    }

    public ArrayList<ImageSkeleton> getFilteredPhotos() {
        return imagesPathsFiltered;
    }

}
