package com.anydone.desk.linkshare;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class LinkShareContract {
    public interface LinkShareView extends BaseView {

        void onLinkShareSuccess(String link);

        void onLinkShareFail(String msg);

    }

    public interface LinkSharePresenter extends Presenter<LinkShareContract.LinkShareView> {

        void getShareLink(String ticketId, String emailPhone);

    }
}
