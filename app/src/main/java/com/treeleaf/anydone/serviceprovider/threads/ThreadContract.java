package com.treeleaf.anydone.serviceprovider.threads;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ThreadContract {

    public interface ThreadView extends BaseView {

    }

    public interface ThreadPresenter extends Presenter<ThreadView> {

    }
}
