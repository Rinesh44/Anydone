package com.anydone.desk.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mynameismidori.currencypicker.ExtendedCurrency;
import com.anydone.desk.R;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> implements
        Filterable {
    private static final String TAG = "CurrencyAdapter";
    private List<ExtendedCurrency> currencyList;
    private List<ExtendedCurrency> currencyListFiltered;
    private Context mContext;
    private OnItemClickListener listener;

    public CurrencyAdapter(List<ExtendedCurrency> currencyList, Context mContext) {
        this.currencyList = currencyList;
        this.mContext = mContext;
        this.currencyListFiltered = currencyList;
    }

    @NonNull
    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_currency_row, parent, false);
        return new CurrencyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
        ExtendedCurrency currencyResult = currencyListFiltered.get(position);

        holder.tvCode.setText(currencyResult.getCode());
        holder.tvName.setText(currencyResult.getName());
        Glide.with(mContext).load(currencyResult.getFlag()).into(holder.ivFlag);
    }

    @Override
    public int getItemCount() {
        return currencyListFiltered.size();
    }


    class CurrencyHolder extends RecyclerView.ViewHolder {
        private TextView tvCode;
        private TextView tvName;
        private ImageView ivFlag;

        CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvName = itemView.findViewById(R.id.tv_name);
            ivFlag = itemView.findViewById(R.id.iv_flag);
            RelativeLayout rlContainer = itemView.findViewById(R.id.rl_timezone_holder);

            rlContainer.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currencyListFiltered.get(position));
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(ExtendedCurrency currencyResult);
    }

    public void setOnItemClickListener(CurrencyAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    currencyListFiltered = currencyList;
                } else {
                    List<ExtendedCurrency> filteredList = new ArrayList<>();
                    for (ExtendedCurrency row : currencyList) {
                        if (row.getCode().toLowerCase().contains(charString.toLowerCase())
                                || row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    currencyListFiltered = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = currencyListFiltered;
                return filterResults;

            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                currencyListFiltered = (List<ExtendedCurrency>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
