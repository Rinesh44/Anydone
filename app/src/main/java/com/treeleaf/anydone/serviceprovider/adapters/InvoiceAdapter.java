package com.treeleaf.anydone.serviceprovider.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Invoice;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.text.DecimalFormat;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceHolder> {
    private static final String TAG = "LinkedTicketAdapter";
    private List<Invoice> invoiceList;
    private Context mContext;
    private InvoiceAdapter.OnItemClickListener listener;

    public InvoiceAdapter(List<Invoice> invoiceList, Context mContext) {
        this.invoiceList = invoiceList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public InvoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_row, parent,
                        false);
        return new InvoiceHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InvoiceHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);

        String date = GlobalUtils.getDateLong(invoice.getCreatedAt());
        holder.invoiceId.setText(String.valueOf(invoice.getInvoiceId()));
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        holder.amount.setText("$ " + decimalFormat.format(invoice.getAmount()));
        String time = GlobalUtils.getTimeExcludeMillis(invoice.getCreatedAt());

        StringBuilder createdDateBuilder = new StringBuilder();
        createdDateBuilder.append(date);
        createdDateBuilder.append(" " + time);
        holder.tvDate.setText(createdDateBuilder);

    }

    @Override
    public int getItemCount() {
        if (invoiceList != null) {
            return invoiceList.size();
        } else return 0;
    }

    class InvoiceHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private RelativeLayout rlInvoiceHolder;
        private TextView invoiceId;
        private TextView amount;

        InvoiceHolder(@NonNull View itemView) {
            super(itemView);
            rlInvoiceHolder = itemView.findViewById(R.id.rl_invoice_holder);
            tvDate = itemView.findViewById(R.id.tv_created_at);
            invoiceId = itemView.findViewById(R.id.tv_invoice_id);
            amount = itemView.findViewById(R.id.tv_amount);

            if (rlInvoiceHolder != null) {
                rlInvoiceHolder.setOnClickListener(view -> {
                    int position = getAdapterPosition();

                    GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(invoiceList.get(position));
                    }
                });
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Invoice invoice);
    }

    public void setOnItemClickListener(InvoiceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
