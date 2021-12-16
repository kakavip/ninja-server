package interfaces;

public interface IGlobalBattler {

    void changeTypePk(short typePk);

    void upPoint(int point);

    void resetPoint();

    int getPoint();

    void enterChienTruong(byte type);

    void notifyMessage(String message);

    short getPhe();

}
