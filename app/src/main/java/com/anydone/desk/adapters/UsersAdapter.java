package com.anydone.desk.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.anydone.desk.R;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.repo.CustomerRepo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    private static final String TAG = "UserAdapter";
    private List<Customer> customerList;
    private Context mContext;
    //    private OnItemClickListener listener;
    private long mLastClickTime = 0;

    public UsersAdapter(List<Customer> customerList, Context mContext) {
        this.customerList = customerList;
        this.mContext = mContext;
    }

    public void setData(List<Customer> customerList) {
        this.customerList = customerList;
        notifyDataSetChanged();
    }

    public void updateThread(String customerId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Customer updatedCustomer = CustomerRepo.getInstance().getCustomerById(customerId);
            int index = customerList.indexOf(updatedCustomer);
            customerList.set(index, updatedCustomer);
            notifyItemChanged(index);
        });
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_row,
                parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        Customer customer = customerList.get(position);

        if (customer.getProfilePic() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext).load(customer.getProfilePic())
                    .apply(options).into(holder.civCustomer);
        }

        holder.tvCustomerName.setText(customer.getFullName());
        if (!customer.getEmail().isEmpty()) {
            holder.tvEmail.setVisibility(View.VISIBLE);
            holder.tvEmail.setText(customer.getEmail());
        } else holder.tvEmail.setVisibility(View.GONE);

        if (!customer.getPhone().isEmpty()) {
            holder.tvPhone.setVisibility(View.VISIBLE);
            holder.tvPhone.setText(customer.getPhone());
        } else holder.tvPhone.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return customerList.size();
    }


    class UserHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private CircleImageView civCustomer;
        private TextView tvEmail;
        private TextView tvPhone;
        private RelativeLayout container;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            civCustomer = itemView.findViewById(R.id.civ_customer);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            container = itemView.findViewById(R.id.rl_holder);

    /*        container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(customerList.get(position));
                }
            });*/
        }
    }


/*    public interface OnItemClickListener {
        void onItemClick(Thread thread);

    }

    public void setOnItemClickListener(UsersAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }*/

}
