package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.FolderModel;
import com.github.sasd97.upitter.utils.Dimens;

import java.util.ArrayList;

/**
 * Created by Alex on 03.02.2016.
 */

public class GallerySpinnerAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<FolderModel> folders;

    public GallerySpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public GallerySpinnerAdapter(Context context, int resources, ArrayList<FolderModel> folders) {
        super(context, resources, folders);

        this.context = context;
        this.folders = folders;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = layoutInflater.inflate(R.layout.gallery_spinner_single_view, parent, false);

        FolderModel folderModel = folders.get(position);

        TextView folderName = (TextView) convertView.findViewById(R.id.folder_name);
        TextView folderAmount = (TextView) convertView.findViewById(R.id.folder_amount);
        ImageView folderPreview = (ImageView) convertView.findViewById(R.id.folder_preview);

        folderName.setText(folderModel.getName());
        folderAmount.setText(folderModel.getAmount().toString());

        Glide
                .with(context)
                .load(folderModel.getPreview())
                .centerCrop()
                .crossFade()
                .into(folderPreview);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = layoutInflater.inflate(R.layout.gallery_spinner_single_view, parent, false);

        FolderModel folderModel = folders.get(position);

        TextView folderName = (TextView) convertView.findViewById(R.id.folder_name);
        TextView folderAmount = (TextView) convertView.findViewById(R.id.folder_amount);
        ImageView folderPreview = (ImageView) convertView.findViewById(R.id.folder_preview);

        folderName.setText(folderModel.getName());
        folderAmount.setText(folderModel.getAmount().toString());
        Glide
                .with(context)
                .load(folderModel.getPreview())
                .centerCrop()
                .crossFade()
                .into(folderPreview);

        return convertView;
    }

    public FolderModel getFolder(int position) {
        return folders.get(position);
    }
}
