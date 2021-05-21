package com.anydone.desk.adapters;

import android.content.Context;
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
import com.anydone.desk.R;
import com.anydone.desk.realm.model.Card;
import com.anydone.desk.realm.repo.CardRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
    private static final String TAG = "BrowseServiceAdapter";
    private List<Card> cardList;
    private Context mContext;
    private OnItemClickListener listener;
    private OnDeleteListener deleteListener;
    private OnPrimaryListener primaryListener;
    private ArrayList<String> cardPatterns = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CardAdapter(List<Card> cardList, Context mContext) {
        this.cardList = cardList;
        this.mContext = mContext;
        viewBinderHelper.setOpenOnlyOne(true);
        createCardPatterns();
    }

    public void setData(List<Card> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent,
                false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        Card card = cardList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout, card.getCardId());
        setCardType(card, holder.cardImage);

        GlobalUtils.showLog(TAG, "card number check: " + card.getCardNumber());

        if (card.getCardNumber().length() > 4) {
            String[] splitCardNo = card.getCardNumber().split("\\s+");
            StringBuilder cardNo = new StringBuilder();
            cardNo.append("**** **** **** ");
            cardNo.append(splitCardNo[3]);
            holder.tvCardNumber.setText(cardNo);
        } else {
            StringBuilder cardNo = new StringBuilder();
            cardNo.append("**** **** **** ");
            cardNo.append(card.getCardNumber());
            holder.tvCardNumber.setText(cardNo);
        }

        long cardExpiryDate = card.getExpiryDate();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cardExpiryDate);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        int month = (cal.get(Calendar.MONTH)) + 1;

        StringBuilder expiryDate = new StringBuilder("Expires on ");
        expiryDate.append(month);
        expiryDate.append("/");
        expiryDate.append(year);
        holder.tvExpiryDate.setText(expiryDate);

        if (card.isPrimary()) {
            holder.tvPrimary.setVisibility(View.VISIBLE);
        } else holder.tvPrimary.setVisibility(View.GONE);

        holder.ibPrimary.setOnClickListener(v -> {
            if (primaryListener != null) {
                primaryListener.onPrimaryClicked(card.getCardId(), cardList.indexOf(card));
            }
        });

        holder.ibDelete.setOnClickListener(view -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClicked(card.getCardId(), cardList.indexOf(card));
            }
        });
    }

    private void setCardType(Card card, ImageView cardImage) {
        GlobalUtils.showLog(TAG, "getcardType: " + card.getCardType());
        switch (card.getCardType().toUpperCase()) {
            case "VISA":
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_visacard_icon));
                break;

            case "MASTERCARD":
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_mastercard_icon));
                break;

            case "AMERICAN EXPRESS":
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_american_icon));
                break;

            case "DINERS CLUB":
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_diners_icon));
                break;

            case "DISCOVER":
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_discover_icon));
                break;

            case "JCB":
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_jcb_icon));
                break;

            default:
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_visacard_icon));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    class CardHolder extends RecyclerView.ViewHolder {
        private TextView tvCardNumber;
        private TextView tvExpiryDate;
        private ImageView cardImage;
        private RelativeLayout container;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageButton ibPrimary, ibDelete;
        private TextView tvPrimary;

        CardHolder(@NonNull View itemView) {
            super(itemView);
            tvCardNumber = itemView.findViewById(R.id.tv_card_no);
            tvExpiryDate = itemView.findViewById(R.id.tv_expiry_date);
            cardImage = itemView.findViewById(R.id.iv_card_type);
            container = itemView.findViewById(R.id.rl_container);
            tvPrimary = itemView.findViewById(R.id.tv_primary);
            ibDelete = itemView.findViewById(R.id.ib_delete);
            ibPrimary = itemView.findViewById(R.id.ib_primary);
            swipeRevealLayout = itemView.findViewById(R.id.srl_card);

            container.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(cardList.get(position));
                }
            });


        }
    }

    public interface OnPrimaryListener {
        void onPrimaryClicked(String id, int pos);
    }

    public interface OnDeleteListener {
        void onDeleteClicked(String id, int pos);
    }

    public interface OnItemClickListener {
        void onItemClick(Card card);
    }

    public void setOnPrimaryListener(CardAdapter.OnPrimaryListener primaryListener) {
        this.primaryListener = primaryListener;
    }

    public void setOnDeleteListener(CardAdapter.OnDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setOnItemClickListener(CardAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void closeSwipeLayout(String layoutId) {
        viewBinderHelper.closeLayout(layoutId);
    }

    public void deleteItem(String id, int pos) {
        removeFromDb(id);
        cardList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, cardList.size());
    }

    private void removeFromDb(String id) {
        CardRepo.getInstance().deleteCardById(id);
    }


    private void detectCardType(String ccNum, ImageView cardImage) {
        for (String p : cardPatterns) {
            if (ccNum.matches(p)) {
                addCardDrawable(p, cardImage);
                break;
            } else {
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_visacard_icon));
            }
        }
    }

    private void addCardDrawable(String p, ImageView cardImage) {
        switch (p) {
            case Constants.VISA:
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_visacard_icon));
                break;

            case Constants.MASTERCARD:
                cardImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_mastercard_icon));
                break;

            default:
                break;
        }
    }

    private void createCardPatterns() {
        String ptVisa = Constants.VISA;
        cardPatterns.add(ptVisa);
        String ptMasterCard = Constants.MASTERCARD;
        cardPatterns.add(ptMasterCard);
        String ptAmeExp = Constants.AMERICAN_EXPRESS;
        cardPatterns.add(ptAmeExp);
        String ptDinClb = Constants.DINERS_CLUB;
        cardPatterns.add(ptDinClb);
        String ptDiscover = Constants.DISCOVER;
        cardPatterns.add(ptDiscover);
        String ptJcb = Constants.JCB;
        cardPatterns.add(ptJcb);
    }
}

