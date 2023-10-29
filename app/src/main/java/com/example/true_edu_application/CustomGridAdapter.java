package com.example.true_edu_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> packIDs;
    private ArrayList<String> packNames;

    public CustomGridAdapter(Context context, ArrayList<String> packIDs, ArrayList<String> packNames) {
        this.context = context;
        this.packIDs = packIDs;
        this.packNames = packNames;
    }

    @Override
    public int getCount() {
        return packIDs.size(); // Number of items in the grid
    }

    @Override
    public Object getItem(int position) {
        return null; // Not used
    }

    @Override
    public long getItemId(int position) {
        return position; // Unique ID for each item
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // Inflate a layout for each grid item (cell) if it doesn't exist
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        }

        // Get references to the TextViews in the grid item layout
//        TextView packIDTextView = convertView.findViewById(R.id.packIDTextView);
        TextView packNameTextView = convertView.findViewById(R.id.packNameTextView);

        // Set the text for each TextView
//        packIDTextView.setText("PackID: " + packIDs.get(position));
        packNameTextView.setText(packNames.get(position));

        return convertView;
    }
}
