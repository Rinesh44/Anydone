package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketactivitylog;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;

public class TicketActivityLogFragment extends BaseFragment<TicketActivityLogPresenterImpl>
        implements TicketActivityLogContract.TicketActivityLogView {
    @Override
    protected int getLayout() {
        return R.layout.layout_activity_log;
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
