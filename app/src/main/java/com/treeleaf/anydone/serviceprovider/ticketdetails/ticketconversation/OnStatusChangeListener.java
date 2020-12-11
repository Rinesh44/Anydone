package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation;

public interface OnStatusChangeListener {
    void onTaskStarted();
    void onTaskResolved();
    void onTaskClosed();
    void onTaskReopened();
}
