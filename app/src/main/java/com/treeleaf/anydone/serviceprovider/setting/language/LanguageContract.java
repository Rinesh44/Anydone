package com.treeleaf.anydone.serviceprovider.setting.language;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class LanguageContract {
    public interface LanguageView extends BaseView {
        void onLanguageChangedSuccess(String language);

        void onLanguageChangeFail(String msg);
    }

    public interface LanguagePresenter extends Presenter<LanguageView> {
        void changeLanguage(String language);
    }
}
