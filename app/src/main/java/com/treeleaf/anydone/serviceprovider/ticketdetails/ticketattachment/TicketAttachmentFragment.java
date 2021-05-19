package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketattachment;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;

public class TicketAttachmentFragment extends BaseFragment<TicketAttachmentPresenterImpl> implements
        TicketAttachmentContract.TicketAttachmentView {

    @Override
    protected int getLayout() {
        return R.layout.fragment_attachments;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {

    }
}

