package tasks;

import server.util;

public class Talk {
    public static String get(int type, int num) {
        try {
            if (type == 0) {
                return TEXTVIE[num][util.nextInt((TEXTVIE[num]).length)];
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return String.format("ERROR[%d:%d]", new Object[]{Integer.valueOf(type), Integer.valueOf(num)});
    }

    public static String getTask(int type, int num) {
        try {
            if (type == 0) {
                return TEXTVIETASK[num];
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return String.format("ERROR[%d:%d]", new Object[]{Integer.valueOf(type), Integer.valueOf(num)});
    }

    private static final String[][] TEXTVIE = new String[][]{{},
            {},
            {},
            {"Đi đường cần mang ít dược phẩm",
 "Không mang theo HP, Hp bên mình, con sẽ gặp nguy hiểm."},
            {"Thức ăn của ta là ngon nhất rồi!",
 "Hahaha, chắc ngươi đi đường cũng mệt rồi."},
            {"Hãy an tâm gia đồ cho ta nào!",
 "Trên người của ngươi toàn là những đồ có giá trị, Sao không cất bớt ở đây?",
 "Ta giữ đồ chưa hề để thất lạc bao giờ."},
            {"Nâng cấp trang bị: Uy tính, giá cả phải chăng"},
            {"Ngựa của ta rất khỏe, có thể chạy ngàn dặm"}};


    private static final String[] TEXTVIETASK = new String[]{
            "Việc đầu tiên con cần là đi nói chuyện với những người trong làng. Con đồng ý chứ?",
 "Chào con, ta là Kiriko, chuyên bán dược phẩm chữa trị vết thương",
 "Ta là Tabemono. Cửa hàng của ta bán rất nhiều thức ăn, giúp tăng cường sức khỏe",
 "Muốn nâng cấp trang bị? Hãy tìm Kenshito này.",
 "Chào con, ta đứng đây giúp đỡ mọi người trao đổi, lưu thông tiền với nhau.",
 "Ta là Kamakura, ta sẽ cất giữ đồ đạc cho ngươi.",
 "Ngươi muốn đến làng nào, hãy đến gặp ta. Ta sẽ chở đi",
 "Con nhớ ông Tabemono bán thức ăn chứ? Hãy đến gặp, ông ấy có vài câu hỏi kiểm tra con đấy.",
 "Nhanh lên nhé, hãy chọn Menu/Nhiệm vụ để biết mình cần nói chuyện với những ai.",
 "Haha, con đã gặp những người đó rồi chứ? Con cứ đi tìm hiểu quanh làng, khi nào thấy quen thuộc hãy đến gặp ta lần nữa.",
 "Ông Tabemono con đã từng nói chuyện 1 lần rồi, ông ta đứng đằng kia kìa.",
 "Con có muốn nhận làm nhiệm vụ này không",
 "Trả lời nhanh cho ta: ông Kiriko làm gì",
 "Con biết ta đứng đây làm gì không",
 "Công việc của Kamakura là gì",
 "Thế còn Kenshinto, ông ấy làm gì?",
 "Umayaki đứng trong làng làm gì?",
 "Giữ rương đồ",
 "Bán thuốc HP,MP",
 "Bán thức ăn",
 "Kéo xe qua các làng",
 "Người kéo xe",
 "Người bán thức ăn",
 "Nâng cấp trang bị",
 "Trả lời sai. Suy nghĩ thật kĩ rồi chọn lại. %s",
 "Haha, tốt, hãy quay về tìm trưởng làng, ta đã gửi ông ấy 1 món quà đặc biệt cho con.",
 "Con đã gặp ông Tabemono? Tốt lắm, đây là một thanh kiếm gỗ mà ông ấy tặng con. Kiểm tra trong Menu?Hành trang nhé!",
 "Rất tốt, Con đã có kiếm. Ta muốn con học cách sử dụng nó bằng cách đánh ngã 10 con bù nhìn rơm đằng kia.",
 "Con chú ý cần mở: Menu/Hành trang để trang bị vũ khí cho mình nhé.",
 "Đó chỉ là bù nhìn thôi. Không có gì phải tự hào. Cứ luyện tập đi, khi nào thấy có khả năng thì quay lại gặp ta.",
 "Tập đủ chưa vậy? Nếu đủ thì con hãy mua ít thức ăn, ra đánh bọn ốc sên và cóc xanh quấy phá mùa màng ngoài làng đi.",
 "Ta nhắc lại lần nữa: Hãy chọn Menu/Nhiệm vụ để biết cách làm như thế nào!",
 "Trưởng làng nhờ ta nói với con: Hãy sử dụng thức ăn trước khi ra khỏi làng.",
 "Tốt lắm, bay giờ con có thể ra khỏi làng làm nhiệm vụ rồi.",
 "Con làm tốt lắm, nhưng bọn cóc ấy vẫn còn quá nhiều. Lần sau gặp ta, ta sẽ hướng dẫn con tạo thêm trang bị cho mình.",
 "Con thu thập 1 ít lông nhím và da thỏ, ta sẽ may cho con 1 cái quần thật đẹp.",
 "Lưu ý là ta chỉ cho phép con làm việc này khi con đạt cấp 5, vì vậy hãy luyện tập trước đi",
 "Đủ vật liệu rồi. Tốt tốt, kiể, tra trong hành trang của con nhé! Đừng ngạc nhiên vì sao ta có thể may quá nhanh.",
 "Con đạt cấp 6 chưa? Nếu đã đạt thì mang về cho ta 15 bông thảo dược từ thác Kitajima về giúp ta nhé.",
 "Thác Kitajima khá xa đấy, hãy chọn Menu/Bản đồ để biết cách đi đến đó nhé.",
 "Ta cho con một ít bình HP và MP được làm từ thảo dược con mang về. Nếu muốn mua thêm, con có thể trở lại đây gặp ta",
 "Làng ta nằm giữa 3 ngôi trường lớn. Khi con đạt cấp 7, hãy đến khu vực quanh trường để tìm hiểu xem.",
 "Hãy mở Menu/bản đồ để biết đường đi. Tìm hiểu xem mình thích hợp ngôi trường nào nhất con nhé!",
 "Thế nào? Con đã biết các ngôi trường đó dậy gì chưa? Nếu biết thì nhanh đến gặp trưởng làng nhé!",
 "Sắp tới, con sẽ phải chọn một ngôi trường để theo học. Hãy gặp Tabemono để biết kiến thức cơ bản trước khi vào trường.",
 "Chúc con may mắn nhé!",
 "Tiền yên và vật phẩm khóa",
 "Tiền xu và vật không khóa phẩm khóa",
 "Tiền yên, xu, và vật phẩm không khóa",
 "Con thử trả lời nhé! Con có thể buôn bán trao đổi những loại vật phẩm nào?",
 "Có bao nhiêu trường học quanh đây?",
 "3 trường",
 "2 trường",
 "4 trường",
 "Có tổng cộng bao nhiêu lớp học?",
 "3 lớp",
 "6 lớp",
 "12 lớp",
 "Nội công bao gồm những lớp nào?",
 "Kiếm, phi tiêu, quạt",
 "Phi tiêu, quạt, cung",
 "Kunai, cung, đao",
 "Ngoại công bao gồm những lớp nào?",
 "Phi tiêu, đao, cung",
 "Quạt, kiếm, kunai",
 "Kiếm, kunai, đao",
 "Tốt, hãy tìm Kamakura để biết những kiến thức khác",
 "Chào. Cho ta biết con đang có bao nhiêu ô trống trong hành trang?",
 "10 ô",
 "20 ô",
 "30 ô",
 "Lưu tạo độ mặc định nhằm mục đích gì?",
 "Là nơi về khi bị thương",
 "Nơi xuất hiện khi đăng nhập",
 "Nơi đứng của người giữ rương",
 "Có mấy loại tiền tệ ở thế giới Ninja này?",
 "1 loại",
 "2 loại",
 "3 loại",
 "Tiền yên trong kiếm được bằng cách nào?",
 "Làm nhiệm vụ, tham gia hoạt động",
 "Nhặt được khi đánh quái",
 "Cả 2 trường hợp",
 "Loại tiền nào có thể chuyển đổi qua lại giữa các người chơi?",
 "Tiền yên",
 "Tiền xu",
 "Tiền lượng",
 "Rất tốt, tiếp theo hãy tìm Kenshito, ông ấy có một số câu hỏi cho con đấy!",
 "Con có biết khi nâng cấp trang bị thì cần những gì không?",
 "Đá + yên hoặc xu",
 "Đá",
 "Yên hoặc xu",
 "Tách trang bị sau khi nâng cấp sẽ nhận được gì?",
 "Xu + 100% Đá đã ép vào",
 "50% Đá đã ép vào",
 "Không được gì cả",
 "Trang bị được nâng cấp tối đa đến cấp mấy?",
 "Cấp 12",
 "Cấp 14",
 "Cấp 16",
 "Trang bị căn bản có mấy loại?",
 "1 loại",
 "2 loại",
 "3 loại",
 "Sau khi nâng cấp trang bị sẽ như thế nào?",
 "Không thay đổi",
 "Lên cấp và chỉ số cao hơn",
 "Trông đẹp hơn",
 "Con cũng đã biết khá nhiều rồi đấy. Hãy quay về gặp trưởng làng, ông ấy đang chờ con đó",
 "Chúc mừng con, ta đang liên hệ với các thầy hiệu trưởng. Khi đạt cấp 9, hãy đến gặp ta để nhận giấy giới thiệu.",
 "Con cứ đến gặp thầy, cô hiệu trưởng và bảo với họ do ta giới thiệu con đến đó",
 "Đi đường cẩn thận, sau khi nói chuyện xong với 3 thầy quay về đây gặp lại ta!",
 "Con cũng thấy rồi đó, ở trường của ta thuộc hệ hỏa, binh khí sử dụng là kiếm và phi tiêu!",
 "Tất cả mọi thứ đều đóng băng thì thật tuyệt, vũ khí ta thường dùng là kunai và cung!",
 "Nhanh và mạnh, đó là Phong, gió của ta... kết hợp với là đao và quạt, không gì có thể cản được!",
 "Tốt, bây giờ con đã có thể xin nhập học từ thầy, cô hiệu trưởng ở các trường. Nhưng trước khi ra khỏi làng...\nta muốn con cất giữ thật kỹ viên ngọc 4 sao này, đây là bảo vật gia truyền của làng ta...\nngoài nó ra vẫn còn thêm 6 viên nữa được cất giữ ở 6 ngôi làng bên cạnh, ta muốn con hãy thu thập cho đủ bộ...\nđể giải mã bí mật bên trong 7 viên ngọc. Các trưởng làng sẽ tặng nó cho con sau mỗi lần con giúp đỡ họ...\nCố gắng làm việc thật tốt con nhé. Hãy sử dụng khả di lệnh (Menu/Hành trang) ta tặng để đi cho nhanh!",
 ""
    };
}
