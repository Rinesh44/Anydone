package com.treeleaf.anydone.serviceprovider.paymentmethod;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.button.MaterialButton;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.CardAdapter;
import com.treeleaf.anydone.serviceprovider.addpaymentcard.AddCardActivity;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Card;
import com.treeleaf.anydone.serviceprovider.realm.repo.CardRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentMethodActivity extends MvpBaseActivity<PaymentMethodPresenterImpl>
        implements PaymentMethodContract.PaymentMethodView {
    public static final int ACTIVITY_CODE = 7896;
    @BindView(R.id.rv_payment_methods)
    RecyclerView rvPaymentMethod;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.ll_card_available_view)
    LinearLayout llCardAvailableView;
    @BindView(R.id.iv_no_cards_view)
    ImageView ivNoCardView;
    @BindView(R.id.btn_add_card)
    MaterialButton btnAddCard;
    @BindView(R.id.btn_card)
    MaterialButton btnCard;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private List<Card> cardList;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setToolbar();
        cardList = CardRepo.getInstance().getAllCards();
        if (CollectionUtils.isEmpty(cardList)) {
            presenter.getPaymentCards();
        } else {
            setUpCardRecyclerView();
        }

        ivBack.setOnClickListener(v -> onBackPressed());
    }

    private void setUpCardRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvPaymentMethod.setLayoutManager(mLayoutManager);

        cardList = CardRepo.getInstance().getAllCards();
        adapter = new CardAdapter(cardList, getContext());
        if (!CollectionUtils.isEmpty(cardList)) {
            llCardAvailableView.setVisibility(View.VISIBLE);
            ivNoCardView.setVisibility(View.GONE);
            btnAddCard.setVisibility(View.GONE);
            btnCard.setVisibility(View.VISIBLE);
            rvPaymentMethod.setAdapter(adapter);
        } else {
            llCardAvailableView.setVisibility(View.GONE);
            ivNoCardView.setVisibility(View.VISIBLE);
            btnAddCard.setVisibility(View.VISIBLE);
            btnCard.setVisibility(View.GONE);
        }

        if (adapter != null) {
            adapter.setOnPrimaryListener((id, pos) -> {
                try {
                    presenter.makeCardPrimary(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            adapter.setOnDeleteListener(this::showDeleteDialog);
        }
    }

    private void showDeleteDialog(String cardId, int pos) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.deleteCard(cardId, pos);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(cardId);
                    dialog.dismiss();
                });


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableStringBuilder str = new SpannableStringBuilder("Payment Methods");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @OnClick(R.id.btn_card)
    public void onAddCardClick() {
        startActivityForResult(new Intent(PaymentMethodActivity.this,
                AddCardActivity.class), ACTIVITY_CODE);
    }

    @OnClick(R.id.btn_add_card)
    public void onAddCardButtonClick() {
        startActivityForResult(new Intent(PaymentMethodActivity.this,
                AddCardActivity.class), ACTIVITY_CODE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_payment_method;
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    @Override
    public void getPaymentCardSuccess() {
        llCardAvailableView.setVisibility(View.VISIBLE);
        ivNoCardView.setVisibility(View.GONE);
        btnAddCard.setVisibility(View.GONE);
        setUpCardRecyclerView();
    }

    @Override
    public void getPaymentCardFail(String msg) {
        llCardAvailableView.setVisibility(View.GONE);
        ivNoCardView.setVisibility(View.VISIBLE);
        btnAddCard.setVisibility(View.VISIBLE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
     /*   Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();*/
    }

    @Override
    public void onMakeCardPrimarySuccess(String refId) {
        adapter.closeSwipeLayout(refId);
        CardRepo.getInstance().removeCardAsPrimary();
        CardRepo.getInstance().setCardAsPrimary(refId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMakeCardPrimaryFail(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, message);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, message, Banner.TOP, 2000).show();
    }

    @Override
    public void onCardDeleteSuccess(String refId, int pos) {
        adapter.deleteItem(refId, pos);
//        setUpCardRecyclerView();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCardDeleteFail(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, message);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, message, Banner.TOP, 2000).show();
    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, Constants.SERVER_ERROR);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, message, Banner.TOP, 2000).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_CODE && resultCode == 2) {
            if (data != null) {
                boolean cardAdded = data.getBooleanExtra("card_added", false);

                if (cardAdded) {
                    cardList = CardRepo.getInstance().getAllCards();
                    adapter.setData(cardList);
                }
            }
        }
    }
}