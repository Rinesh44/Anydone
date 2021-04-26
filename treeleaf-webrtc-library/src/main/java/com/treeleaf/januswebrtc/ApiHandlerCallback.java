package com.treeleaf.januswebrtc;

public interface ApiHandlerCallback {

    void removeTransaction(String transaction);

    void onReceivePollEvent(String result);

    void updateProgressMessage(String message);

    void restError(String message);

}
