package com.anydone.desk.setting.currency;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

import java.io.UnsupportedEncodingException;

public class CurrencyContract {
    public interface CurrencyView extends BaseView {
        void onAddCurrencySuccess();

        void onAddCurrencyFail(String msg);
    }

    public interface CurrencyPresenter extends Presenter<CurrencyContract.CurrencyView> {
        void addCurrency(String token, String currency) throws UnsupportedEncodingException;
    }
}
