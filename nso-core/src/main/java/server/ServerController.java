package server;

import threading.Message;
import io.Session;

public abstract class ServerController
{
    public abstract void userLogin(final Session p0, final Message p1);
    
    public abstract boolean userLogout(final Session p0);
    
    public abstract void processGameMessage(final Session p0, final Message p1);
}
