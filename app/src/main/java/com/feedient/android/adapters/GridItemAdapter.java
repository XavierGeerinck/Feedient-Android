package com.feedient.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.android.models.GridItem;
import com.joanzapata.android.iconify.Iconify;

import java.util.List;

public class GridItemAdapter extends BaseAdapter {
    private final List<GridItem> items;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public GridItemAdapter(Context context, List<GridItem> items) {
        this.context = context;
        this.items = items;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    static class ViewHolderItem {
        TextView iconTextView;
        TextView title;
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
        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.iconTextView = (TextView)convertView.findViewById(R.id.grid_item_icon);
            viewHolder.title = (TextView)convertView.findViewById(R.id.grid_item_title);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        GridItem item = items.get(position);

        if (item != null) {
            viewHolder.title.setText(item.getTitle());
            viewHolder.iconTextView.setText("{" + item.getProviderModel().getIcon() + "}");
            Iconify.addIcons(viewHolder.iconTextView);
        }

        return convertView;
    }
}