package real;

import java.util.Date;

public class CardDCoin {
    public static int CARD_STATUS_FAILURE = -1;
    public static int CARD_STATUS_INITIAL = 0;
    public static int CARD_STATUS_IN_PROGRESS = 1;
    public static int CARD_STATUS_SUCCESS = 2;
    public static int CARD_STATUS_WRONG_VALUE = 3;
    public static int CARD_STATUS_DONE = 4;

    public int id;
    public String username;
    public String cardType;
    public int cardValue;
    public String cardCode;
    public String cardSeri;
    public int status;
    public Date releaseDate;

    public String getStatusString() {

        if (status == CARD_STATUS_FAILURE) {
            return "Lỗi";
        } else if (status == CARD_STATUS_INITIAL) {
            return "Khởi tạo";
        } else if (status == CARD_STATUS_IN_PROGRESS) {
            return "Đang xử lý";
        } else if (status == CARD_STATUS_SUCCESS) {
            return "Có thể nhận lượng";
        } else if (status == CARD_STATUS_WRONG_VALUE) {
            return "Lỗi mệnh giá";
        } else if (status == CARD_STATUS_DONE) {
            return "Hoàn thành";
        }

        return "Không xác định";
    }

    public static int[] getAllCardStatues() {
        return new int[] {
                CARD_STATUS_FAILURE,
                CARD_STATUS_INITIAL,
                CARD_STATUS_IN_PROGRESS,
                CARD_STATUS_SUCCESS,
                CARD_STATUS_WRONG_VALUE,
                CARD_STATUS_DONE
        };
    }
}
