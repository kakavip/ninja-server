package interfaces;

import real.Body;


public interface ISolo extends Expireable {


    void showTiThi();

    void endSolo();

    UpdateEvent getRunnable();

    void setBody(Body body1, Body body2);

    void start();

}
