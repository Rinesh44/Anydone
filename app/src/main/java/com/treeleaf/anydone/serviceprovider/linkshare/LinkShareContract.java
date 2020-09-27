package com.treeleaf.anydone.serviceprovider.linkshare;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class LinkShareContract {
    public interface LinkShareView extends BaseView {

        void onLinkShareSuccess(String link);

        void onLinkShareFail(String msg);

    }

    public interface LinkSharePresenter extends Presenter<LinkShareContract.LinkShareView> {

        void getShareLink(String ticketId, String emailPhone);

    }
}
