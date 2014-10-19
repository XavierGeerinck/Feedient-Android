package com.feedient.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.IconTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.feedient.core.R;
import com.feedient.core.interfaces.IDrawerPanelItemCallback;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.model.Panel;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PanelsAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private IDrawerPanelItemCallback callback;

    private final List<Panel> panels;

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    public static class ViewHolderItem {
        Panel panel;
        IconTextView txtPanelIcon;
        TextView txtPanelName;
        ImageButton txtRemoveIcon;
    }

    public PanelsAdapter(Context context) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.panels = new ArrayList<Panel>();
    }

    public PanelsAdapter(Context context, List<Panel> panels, IDrawerPanelItemCallback callback) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        this.panels = panels;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return panels.size();
    }

    @Override
    public Object getItem(int i) {
        return panels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list_provider_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();

            viewHolder.txtPanelIcon = (IconTextView)convertView.findViewById(R.id.txt_panel_icon);
            viewHolder.txtPanelName = (TextView)convertView.findViewById(R.id.txt_panel_name);
            viewHolder.txtRemoveIcon = (ImageButton)convertView.findViewById(R.id.img_btn_remove_panel);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        if (panels.size() > position) {
            viewHolder.panel = panels.get(position);
        }

        if (viewHolder.panel != null) {
            viewHolder.txtPanelName.setText(viewHolder.panel.getName());
            viewHolder.txtPanelIcon.setText("{fa-list}");

            Iconify.addIcons(viewHolder.txtPanelIcon);
        }

//        viewHolder.txtRemoveIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callback.onClickRemoveUserProvider(viewHolder.panel);
//            }
//        });

        return convertView;
    }
}
