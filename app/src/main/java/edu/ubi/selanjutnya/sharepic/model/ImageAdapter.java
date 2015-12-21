package edu.ubi.selanjutnya.sharepic.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

import edu.ubi.selanjutnya.sharepic.R;

/**
 * Created by Rifqi on 12/21/2015.
 */
public class ImageAdapter extends BaseAdapter {

    public static final String GRID = "grid";
    public static final String GALLERY = "gallery";

    private static LayoutInflater mInflater = null;
    private Activity mActivity;

    private String mMode = "";

    // TODO
    private Bitmap[] bitmaps;

    public ImageAdapter(Activity activity, String mode) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActivity = activity;
        mMode = mode;

        File file = activity.getExternalFilesDir(null);
        File[] files = file.listFiles();
        if (files != null) {
            bitmaps = new Bitmap[files.length];

            // TODO Assume all files in dir are images
            for (int i = 0; i < files.length; i++) {
                bitmaps[i] = decodeSampledBitmapFromFile(files[i], 300, 300);
            }
        }
        else {
            bitmaps = new Bitmap[0];
        }
    }

    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public Object getItem(int position) {
        return bitmaps[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mMode.equalsIgnoreCase(ImageAdapter.GRID)) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.each_image, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            // TODO
            imageView.setImageBitmap(bitmaps[position]);
        }
        else if (mMode.equalsIgnoreCase(ImageAdapter.GALLERY)) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.each_image_gallery, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            // TODO
            imageView.setImageBitmap(bitmaps[position]);
        }
        return convertView;
    }

    public static Bitmap decodeSampledBitmapFromFile(File file, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
