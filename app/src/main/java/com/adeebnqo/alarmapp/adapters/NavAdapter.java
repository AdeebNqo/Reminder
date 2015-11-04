package com.adeebnqo.alarmapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.models.NavigationItem;

/**
 * Created by adeeb on 5/24/15.
 */
public class NavAdapter extends ArrayAdapter {

    private final Context context;
    private List<NavigationItem> values;

    public NavAdapter(Context context, int resource ,List<NavigationItem> values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.nav_item, parent, false);

        NavigationItem item = values.get(position);

        ImageView img = (ImageView) rowView.findViewById(R.id.nav_item_icon);
        TextView title = (TextView) rowView.findViewById(R.id.nav_item_title);

        img.setImageDrawable(context.getResources().getDrawable(item.getDrawable()));
        title.setText(item.getTitle());

        return rowView;
    }
}
