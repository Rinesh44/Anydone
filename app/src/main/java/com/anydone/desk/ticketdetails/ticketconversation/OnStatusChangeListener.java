package com.anydone.desk.ticketdetails.ticketconversation;

public interface OnStatusChangeListener {
    void onTaskStarted();
    void onTaskResolved();
    void onTaskClosed();
    void onTaskReopened();
}
