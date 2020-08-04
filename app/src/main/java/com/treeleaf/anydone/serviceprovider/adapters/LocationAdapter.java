package com.treeleaf.anydone.serviceprovider.adapters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.realm.repo.LocationRepo;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private static final String TAG = "LocationAdapter";
    private List<Location> locationList;
    private onPrimaryListener primaryListener;
    private onDeleteListener deleteListener;
    private onClickListener clickListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public LocationAdapter(List<Location> locationList) {
        this.locationList = locationList;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setData(List<Location> locationList) {
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_row,
                parent, false);
        return new LocationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Location location = locationList.get(position);

        viewBinderHelper.bind(holder.swipeRevealLayout, location.getId());

        holder.tvLocationName.setText(location.getLocationName());
        holder.tvLocationType.setText(location.getLocationType());
        if (location.isDefault()) holder.ivDefault.setVisibility(View.VISIBLE);
        else holder.ivDefault.setVisibility(View.GONE);

        holder.ibPrimary.setOnClickListener(view -> {
            if (primaryListener != null) {
                primaryListener.onPrimaryClicked(location.getId(), locationList.indexOf(location));
            }
        });

        holder.ibDelete.setOnClickListener(view -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClicked(location.getId(), locationList.indexOf(location));
            }
        });

        holder.rlLocationHolder.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClicked(location.getLocationName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


    class LocationHolder extends RecyclerView.ViewHolder {
        private TextView tvLocationName;
        private TextView tvLocationType;
        private ImageView ivDefault;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageButton ibPrimary, ibDelete;
        private RelativeLayout rlLocationHolder;

        LocationHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tv_location_name);
            tvLocationType = itemView.findViewById(R.id.tv_location_type);
            ivDefault = itemView.findViewById(R.id.iv_default);
            swipeRevealLayout = itemView.findViewById(R.id.srl_location);
            ibDelete = itemView.findViewById(R.id.ib_delete);
            ibPrimary = itemView.findViewById(R.id.ib_primary);
            rlLocationHolder = itemView.findViewById(R.id.rl_location_holder);
        }
    }

    public interface onPrimaryListener {
        void onPrimaryClicked(String id, int pos);
    }

    public interface onDeleteListener {
        void onDeleteClicked(String id, int pos);
    }

    public interface onClickListener {
        void onClicked(String location);
    }

    public void setOnPrimaryListener(LocationAdapter.onPrimaryListener editListener) {
        this.primaryListener = editListener;
    }

    public void setOnDeleteListener(LocationAdapter.onDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setOnClickedListener(LocationAdapter.onClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void closeSwipeLayout(String layoutId) {
        viewBinderHelper.closeLayout(layoutId);
    }

    public void openSwipeLayout(String layoutId) {
        viewBinderHelper.openLayout(layoutId);
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    public void deleteItem(String id, int pos) {
        removeFromDb(id);
        locationList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, locationList.size());
    }

    private void removeFromDb(String id) {
        LocationRepo.getInstance().deleteLocationById(id);
    }

}
