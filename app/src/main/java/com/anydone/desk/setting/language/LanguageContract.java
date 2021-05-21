package com.anydone.desk.setting.language;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class LanguageContract {
    public interface LanguageView extends BaseView {
        void onLanguageChangedSuccess(String language);

        void onLanguageChangeFail(String msg);
    }

    public interface LanguagePresenter extends Presenter<LanguageView> {
        void changeLanguage(String language);
    }
}
