package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Card;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
    private static final String TAG = "BrowseServiceAdapter";
    private List<Card> cardList;
    private Context mContext;
    private OnItemClickListener listener;
    private ArrayList<String> cardPatterns = new ArrayList<>();

    public CardAdapter(List<Card> cardList, Context mContext) {
        this.cardList = cardList;
        this.mContext = mContext;
        createCardPatterns();
    }

    public void setData(List<Card> cardList) {
        this.cardList = cardList;
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
        detectCardType(card.getCardNumber(), holder.cardImage);
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

        CardHolder(@NonNull View itemView) {
            super(itemView);
            tvCardNumber = itemView.findViewById(R.id.tv_card_no);
            tvExpiryDate = itemView.findViewById(R.id.tv_expiry_date);
            cardImage = itemView.findViewById(R.id.iv_card_type);
            container = itemView.findViewById(R.id.rl_container);

            container.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
//                    listener.onItemClick(cardList.get(position));
                    if (getAdapterPosition() == 0) {
                        listener.onItemClick(true);
                    } else {
                        listener.onItemClick(false);
                    }
                }

            });


        }
    }


    public interface OnItemClickListener {
        void onItemClick(boolean isVisa);
    }

    public void setOnItemClickListener(CardAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    private void detectCardType(String ccNum, ImageView cardImage) {
        for (String p : cardPatterns) {
            if (ccNum.matches(p)) {
                addCardDrawable(p, cardImage);
                break;
            } else {
                cardImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_visacard_icon));
            }
        }
    }

    private void addCardDrawable(String p, ImageView cardImage) {
        switch (p) {
            case Constants.VISA:
                cardImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_visacard_icon));
                break;

            case Constants.MASTERCARD:
                cardImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mastercard_icon));
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

