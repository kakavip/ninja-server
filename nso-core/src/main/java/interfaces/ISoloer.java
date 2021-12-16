package interfaces;

public interface ISoloer {
    void setSolo(ISolo solo);

    void requestSolo(ISoloer soloer);


    void acceptSolo();

    void endSolo();
}
