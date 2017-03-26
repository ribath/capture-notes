package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by jhansi on 29/03/15.
 */
public class ResultFragment extends Fragment {

    private View view;
    private ImageView scannedImageView;
    private Button doneButton;
    private Bitmap original;
    private Button originalButton;
    private Button MagicColorButton;
    private Button grayModeButton;
    private Button bwButton;
    private Bitmap transformed;
    String TAG1 = "ResultFragment";

    public ResultFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //////
        transformed = original;
        //////
        Bitmap bitmap1 = getBitmap();
        //////
        Intent data = new Intent();
        Bitmap bitmap2 = transformed;
        if (bitmap2 == null) {
            bitmap2 = original;
        }
        Uri uri = Utils.getUri(getActivity(), bitmap2);
        data.putExtra(ScanConstants.SCANNED_RESULT, uri);
        getActivity().setResult(Activity.RESULT_OK, data);
        original.recycle();
        System.gc();
        getActivity().finish();
    }


    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            original = Utils.getBitmap(getActivity(), uri);
            getActivity().getContentResolver().delete(uri, null, null);
            return original;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUri() {
        Uri uri = getArguments().getParcelable(ScanConstants.SCANNED_RESULT);
        return uri;
    }

    private class BWButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = ((ScanActivity) getActivity()).getBWBitmap(original);
            scannedImageView.setImageBitmap(transformed);
        }
    }

    private class MagicColorButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = ((ScanActivity) getActivity()).getMagicColorBitmap(original);
            scannedImageView.setImageBitmap(transformed);
        }
    }

    private class OriginalButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i(TAG1, "OriginalButtonClickListener called");
            transformed = original;
            scannedImageView.setImageBitmap(original);
        }
    }

    private class GrayButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            transformed = ((ScanActivity) getActivity()).getGrayBitmap(original);
            scannedImageView.setImageBitmap(transformed);
        }
    }

}