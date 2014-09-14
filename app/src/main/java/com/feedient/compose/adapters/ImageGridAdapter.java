package com.feedient.compose.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.feedient.core.helpers.ImageLoaderHelper;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.layout.SquaredImageView;
import com.feedient.core.models.json.UserProvider;

import com.feedient.R;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<String> items;

    public ImageGridAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = ImageLoaderHelper.getCameraImages(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView)convertView;

        if (view == null) {
            view = new SquaredImageView(context);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        String url = items.get(position);

        Picasso.with(context)
                .load(new File(url))
                .placeholder(R.drawable.placeholder)
                .fit()
                .into(view);

        return view;
    }
}