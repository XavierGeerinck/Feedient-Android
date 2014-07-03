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

    public GridItemAdapter(Context context, List<GridItem> items) {
        this.context = context;
        this.items = items;
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
        GridItem item = items.get(position);
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.grid_item, parent, false);
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            TextView iconTextView = (TextView)view.findViewById(R.id.grid_item_icon);
            TextView title = (TextView)view.findViewById(R.id.grid_item_title);

            title.setText(item.getTitle());
            iconTextView.setText(item.getFaIconText());
            Iconify.addIcons(iconTextView);
        }

        return view;
    }
}