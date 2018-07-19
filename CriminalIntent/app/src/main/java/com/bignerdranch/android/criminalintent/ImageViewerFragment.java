package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ImageViewerFragment extends DialogFragment {

    private static final String ARG_IMAGE = "image";

    private ImageView mImageView;

    public static ImageViewerFragment newInstance(File imageFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, imageFile);

        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File imageFile = (File) getArguments().getSerializable(ARG_IMAGE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image_viewer, null);

        mImageView = (ImageView) v.findViewById(R.id.image_viewer);
        if (imageFile == null || !imageFile.exists()) {
            mImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    imageFile.getPath(), getActivity()
            );
            mImageView.setImageBitmap(bitmap);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }
}

