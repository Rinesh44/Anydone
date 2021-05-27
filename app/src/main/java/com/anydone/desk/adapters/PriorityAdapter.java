package com.anydone.desk.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anydone.desk.R;
import com.anydone.desk.model.Priority;
import com.anydone.desk.utils.GlobalUtils;

import java.util.List;

public class PriorityAdapter extends ArrayAdapter<Priority> {

    LayoutInflater flater;
    private static final String TAG = "PriorityAdapter";

    public PriorityAdapter(Activity context, int resouceId, List<Priority> list) {
        super(context, resouceId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        Priority rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.layout_proirity, null, false);

            holder.txtTitle = rowview.findViewById(R.id.tv_priority);
            holder.imageView = rowview.findViewById(R.id.iv_icon);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }

        holder.imageView.setVisibility(View.GONE);
        if (rowItem.getIcon() != -1) {
            holder.imageView.setImageResource(rowItem.getIcon());
            holder.imageView.setVisibility(View.VISIBLE);
        } /*else {
            holder.imageView.setVisibility(View.GONE);
        }*/

        if (rowItem.getIcon() == 0) {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.txtTitle.setText(rowItem.getValue());

        return rowview;
    }

    private class viewHolder {
        TextView txtTitle;
        ImageView imageView;
    }
}