package com.treeleaf.anydone.serviceprovider.setting.currency;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

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
