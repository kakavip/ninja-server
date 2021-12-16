package io;

import threading.Message;

public interface ISessionHandler
{
    void onMessage(final Session p0, final Message p1);
    
    void onConnectionFail(final Session p0);
    
    void onDisconnected(final Session p0);
    
    void onConnectOK(final Session p0);
}
