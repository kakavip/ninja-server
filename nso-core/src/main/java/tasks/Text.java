package tasks;

public class Text {
    public static String get(int type, int num) {
        try {
            if (type == 0) {
                return TEXTVIE[num];
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return String.format("ERROR[%d:%d]", new Object[]{Integer.valueOf(type), Integer.valueOf(num)});
    }

    private static final String[] TEXTVIE = new String[]{
            "Thông tin tài khoản hoặc mật khẩu không chính xác",  // 0
            "Tài khoản này hiện đang bị khóa. Liên hệ Admin để biết thêm thông tin", // 1
            "Có người đăng nhập vào tài khoản cảu bạn", // 2
            "Bạn đang đăng nhập tại một thiết bị khác, hãy thử đăng nhập lại sau vài phút", // 3
            "Chỉ có thể tạo tối đa %d nhân vật!", // 4
            "Tên nhân vật chỉ đồng ý các ký tự a-z,0-9 và chiều dài từ 5 đến 15 ký tự", // 5
            "Tên nhân vật đã tồn tại", // 6
            "Vui lòng chờ sau % giây", // 7
            "Bạn không có đủ lượng trên người.", // 8
            "Khu vực quá tải, vui lòng quay lại sau.", // 9
            "Nhận", // 10
            "Không", // 11
            "Hoàn thành nhiệm vụ", // 12
            "Việc đầu tiên con cần là đi nói chuyện với những người trong làng. Con đồng ý chứ?", // 13
            "Chào con, ta là Kiriko, chuyên bán dược phẩm chữa trị vết thương", // 14
            "Hành trang không đủ chỗ trống", // 15
            "Lỗi vật phẩm", //16
            "Số lượng không hợp lệ", // 17
            "Phải làm xong nhiệm vụ lần đầu dùng kiếm mới có thể bán vật phẩm", // 18
            "Hiện tại người chơi đã offline", // 19
            "%s đang đứng nhìn bạn", // 20
            "Bạn cần phải tháo trang bị thú cưới đang sử dụng", // 21
            "Bạn cần có thú cưới để sử dụng", // 22
            "Cần phải tháo hết trang bị thú cưới ra trước", // 23
            "Bản đồ hiện tại đang quái tải vui lòng quay lại sau giây lát", // 24
            "Vật phẩm của người khác", // 25
            "Không đủ Yên", // 26
            "Không đủ xu", // 27
            "Không đủ lượng", // 28
            "Phải làm xong nhiệm vụ diệt trừ cóc mới có thể mua vật phẩm", // 29
            "Bạn đang có thức ăn cao cấp hơn.", // 30
            "Bạn đã có yêu cầu tỷ thí từ trước. Vui lòng chờ cho đến khi yêu cầu tỷ thí được kết thúc", // 31
            "Vũ khí không thích hợp", // 32
            "Trình độ của bạn chưa đạt yêu cầu", // 33
            "Giới tính không phù hợp", // 34
            "Bạn chưa có vũ khí. Xin vào Menu/Bản Thân/Trang bị để kiểm tra", // 35
            "Rương đồ không đủ chỗ trống", // 36
            "Bạn chỉ được phép cất thêm %d xu", // 39
            "Bạn chỉ được phép rút thêm %d xu", // 40
            "Tốt lắm, ngươi đã chọn nơi này làm nơi trở về khi bị trọng thương", // 41
            "Hiện tại người chơi này không online.", // 42
            "%s đã có tên trong danh sách bạn bè hoặc thù địch.", // 43
            "Đã tối đa danh sách bạn bè", // 44
            "Khoảng cách quá xa",//45
            "Bạn đã gởi yêu cầu giao dịch. Sau 30 giây nữa mới được gửi tiếp", // 46
            "Điểm hiếu chiến quá cao không thể thay đổi trang thái.", // 47
            "Không thể chiến đấu ở khu vực này", // 48
            "Điểm hiếu chiến quá cao không thể cừu sát người khác.", //49
            "Bạn đang cừu sát người khác không thể cùng lúc cừu sát nhiều người", // 50
            "Điểm hiếu chiến quá cao không thể dùng vật phẩm này.", // 51
            "Cần 1 lượng để hồi sinh tại chỗ.", // 52
            "Mã quà tặng", // 53
            "Mã quà tặng không hợp lệ", // 54
            "Không thể cất vật phẩm nhiệm vụ", // 55
            "HP đã đầy", //56
            "MP đã đầy", //57
            "Chưa có thông tin", // 58
            "%s xu", // 59
            "Đã khóa đặt cược", // 60
            "Số xu tối đa có thể đặt cược là %s xu", // 61
            "Số xu tối thiểu phải là %s xu",// 62
            "Số xu tối thiểu phải là %s xu mới có thể tham gia vòng vip", // 63
            "Không đủ xu để đặt cược", // 64
            "Chúc mừng %s đã chiến thắng %s xu trong trò chơi Vòng xoay may mắn",// 65
            "Người vừa chiến thắng:\n%s %s\nSố xu thắng: %s xu\nSố xu tham gia: %s xu",// 66
            "Số xu tham gia đã quá tải", // 67
            "Vòng xoay vip",// 68
            "Vòng xoay thường",// 69
            "Cần có phiếu may mắn.",// 70
            "- Giá trị nhập xu thấp nhất của mỗi người là 1.000.000\n- Giá trị nhập xu cao nhất của mỗi người là 50.000.000\n- Giá trị còn lại sau mỗi lần đặt phải còn ít nhất 10.000.000\n- Mỗi 2 phút là bắt đầu vòng quay một lần.\n- Khi có người bắt đầu nhập xu thì mới bắt đầu đếm ngược thời gian.\n- Còn 10 giây cuối sẽ bắt đầu khóa cho gửi xu.\n- Người chiến thắng sẽ nhận hết tổng số tiền tất cả người chơi khác đặt sau khi đã trừ thuế.\n- Khi người chơi ít hơn 10, thuế sẽ bằng số người chơi -1.\n- Người chơi nhiều hơn 10 người thế sẽ là 10%.", // 71
            "- Giá trị nhập xu thấp nhất của mỗi người là 10.000\n- Giá trị nhập xu cao nhất của mỗi người là 100.000\n- Mỗi 2 phút là bắt đầu vòng quay một lần.\n- Khi có người bắt đầu nhập xu thì mới bắt đầu đếm ngược thời gian.\n- Còn 10 giây cuối sẽ bắt đầu khóa cho gửi xu.\n- Người chiến thắng sẽ nhận hết tổng số tiền tất cả người chơi khác đặt sau khi đã trừ thuế.\n- Khi người chơi ít hơn 10, thuế sẽ bằng số người chơi -1.\n- Người chơi nhiều hơn 10 người thế sẽ là 10%.", // 72
            "Thông tin",  // 73
            "Luật chơi", // 74
            "Bạn nhận được %d yên", // 75
            "%s tham gia thẻ bài bí mật nhận được %s", // 76
            "%s tham gia thẻ bài bí mật nhận được %d %s", // 77
            "Bỏ Trang bị và Đá vào trong khung để nâng cấp. Khi nâng cấp cẩn thận thì cần phải có lượng.", // 78
            "Chỉ được chọn Đá và Bảo hiểm để nâng cấp", // 79
            "Bảo hiểm không phù hợp cho cấp của trang bị", // 80
            "Tỉ lệ thành công từ 40% trở lên thì mới được luyện.", // 81
            "Chỉ được chuyển hóa trang bị cùng loại và cùng cấp trở lên.", // 82
            "Cần có vật phẩm Hoán Chuyển theo đúng cấp trang bị.", // 83
            "Bạn đã sử dụng túi này rồi. Mỗi người chỉ được sử dụng 1 lần.", // 84
            "Cần sử dụng %s mới có thể dùng %s.", // 85
            "Bạn chưa thể đi đến khu vực này. Hãy hoàn thành nhiệm vụ trước.", // 86
            "Con vẫn chưa đủ điều kiện để vào lớp (trình độ từ cấp 10 và làm xong nhiệm vụ tìm hiểu trường", // 87
            "Con đã vào lớp từ trước rồi mà!", // 88
            "Con hãy gỡ bỏ tạp niệm bằng cách cất vũ khí đang sử dụng vào hành trang trước rồi hãy vào lớp học của ta.", // 89
            "Chào mừng con đến với trường Haruna. Con hãy sử dụng vũ khí và đọc sách võ công mà ta tặng (mở Menu/Bản thân/Hành trang) để bước đầu chuẩn bị cho việc học tập", // 90
            "Bạn đã học kĩ năng này rồi", // 91
            "Môn phái không phù hợp", // 92
            "Không được tăng quá điểm tối đa", // 93
            "Trình độ của bạn chưa đủ để nâng cấp", // 94
            "Không đủ điểm để nâng cấp", // 95
            "Khổng thể nâng cấp kĩ năng này", // 96
            "Hành trang con phải tối thiểu có %d ô.", // 97
            "Đây là vật phẩm có giá trị, không thể bán.", // 98
            "Không thể bán trang bị đã nâng cấp", // 99
            "Lời mời tổ đội đã được gởi đi, đang chờ đối phương chấp nhận", // 100
            "Bạn đang trong này.", // 101
            "Bạn đang trong nhóm khác, không thể xin gia nhập.", //102
            "Bạn đã gởi yêu cầu xin vào nhóm rồi. Xin đừng gởi liên tục.", //103
            "Bạn không phải là đội trưởng.",//104
            "Nhóm đối phương đã đầy",//105
            "Hiện tại người chơi này không online.",//106
            "Đối phương đang ở trong nhóm khác.",//107
            "%s đang là đồng đội của bạn.",//108
            "Nhóm đã đầy",//109
            "Bạn đang trong nhóm khác, không thể chấp nhận vào nhóm này.", //110
            "%s đã được lên làm nhóm trưởng.", //111
            "%s đã rời khỏi nhóm.", //112
            "%s đã bị trục xuất khỏi nhóm.", //113
            "Bạn đã bị trục xuất khỏi nhóm.", //114
            "Nhóm không còn tồn tại.", //115
            "Số nhóm trong khu vực này đã đạt tối đa.", //116
            "Nhóm đã được khóa.", //117
            "Khu vực này đã đầy.", //118
            "" // 118

    };
}
