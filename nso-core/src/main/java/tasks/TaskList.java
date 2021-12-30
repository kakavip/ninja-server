package tasks;

import static tasks.TaskTemplate.*;

public class TaskList {

        public static final TaskTemplate[] taskTemplates;

        static {
                taskTemplates = new TaskTemplate[] {
                                builder()
                                                .taskId(0)

                                                .name("Nhiệm vụ chào làng")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Nói chuyện với Kiriko",
                                                                "Nói chuyện với Tabemono",
                                                                "Nói chuyện với Kenshinto",
                                                                "Nói chuyện với Okanechan",
                                                                "Nói chuyện với Kamakura",
                                                                "Nói chuyện với Umayaki",
                                                                "Gặp Tajima báo cáo"

                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1, -1, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(1)

                                                .name(" NV kiến thức")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trả lời Tabemono",
                                                                "", "",
                                                                "", "",
                                                                "Gặp Tajima báo cáo"
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(2)

                                                .name(" NV lần đầu dùng kiếm")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Sử dụng vũ khí",
                                                                "Đánh ngã bù nhìn",
                                                                "Gặp Tajima báo cáo",
                                                })
                                                .counts(new int[] {
                                                                -1, 10, -1
                                                })

                                                .build(),
                                builder()
                                                .taskId(3)

                                                .name("NV Diệt trừ sên cóc")
                                                .detail("Ghi chú: thức ăn (ông Tabemono), ốc sên (Vách Ichidai), cóc xanh (Vách Ichidai)")
                                                .subNames(new String[] {
                                                                "Mua thức ăn cơm nấm",
                                                                "Sử dụng thức ăn",
                                                                "Đánh ốc sên",
                                                                "Đánh cóc xanh",
                                                                "Gặp Tajima báo cáo",
                                                })
                                                .counts(new int[] {
                                                                2, -1, 20, 10, -1
                                                })

                                                .build(),
                                builder()
                                                .taskId(4)
                                                .minLevel(5)
                                                .npcTalk("")

                                                .name("NV Vật liệu tạo giáp")
                                                .detail("Ghi chú: sau khi đạt cấp 5, đánh nhím đá sẽ rớt ra lông nhím (Đồi Fumimen), thỏ xám sẽ rớt ra da thỏ (Vách Ichidai)")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 5",
                                                                "Nhặt lông nhím",
                                                                "Nhặt da thỏ",
                                                                "Báo cáo với trưởng làng",
                                                })
                                                .counts(new int[] {
                                                                -1, 2, 2, -1
                                                })

                                                .build(),
                                builder()
                                                .taskId(5)
                                                .minLevel(7)
                                                .npcTalk("Hái thuốc cứu người là 1 nghĩa cử cao đẹp con có muốn làm không ?")

                                                .name("NV Hái thuốc cứu người")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 7",
                                                                "Nhặt bông thảo dược",
                                                                "Tìm Kiriko báo cáo",
                                                })
                                                .counts(new int[] {
                                                                -1, 15, -1
                                                })

                                                .build(),

                                builder()
                                                .taskId(6)
                                                .minLevel(8)
                                                .npcTalk("Chuyến đi lần này hơi khó khăn ta hi vọng con có thể làm tốt và quay về nếu chết có người khiêng con về nên cứ yên tâm")

                                                .name("NV Khám phá xa làng")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 8",
                                                                "Đến khu luyện tập",
                                                                "Đến khu rừng già",
                                                                "Đến cánh đồng Fuki",
                                                                "Gặp Umayki báo cáo",
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1
                                                })

                                                .build(),
                                builder()
                                                .taskId(7)

                                                .name("NV trắc nghiệm khiến thức")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trả lời Tabemono",
                                                                "",
                                                                "",
                                                                "",
                                                                "",
                                                                "Trả lời Kamakura",
                                                                "",
                                                                "",
                                                                "",
                                                                "",
                                                                "",
                                                                "Trả lời Kenshinto",
                                                                "",
                                                                "",
                                                                "",
                                                                "",
                                                                "Gặp trưởng làng báo cáo"
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                                                -1, -1, -1
                                                })

                                                .build(),

                                builder()
                                                .taskId(8)
                                                .minLevel(9)
                                                .npcTalk("Hãy đi đến các trường và nói chuyện với hiệu trưởng các trường")

                                                .name("Tìm hiểu 3 trường")
                                                .detail("Gặp hiệu trưởng các trường để hỏi về tình hình học tập của các học sinh ở đó")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 9",
                                                                "Tìm hiểu trường Hirosaki",
                                                                "Tìm hiểu trường Ookaza",
                                                                "Tìm hiểu trường Haruna",
                                                                "Gặp trưởng làng báo cáo"
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1
                                                })

                                                .build(),
                                builder()
                                                .taskId(9)
                                                .minLevel(10)
                                                .npcTalk("Con hãy tăng tiềm nang và kĩ năng cho mình nhé")

                                                .name("NV cộng điểm")
                                                .detail("Nâng cao sức mạnh")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 10",
                                                                "Cộng tiềm năng",
                                                                "Cộng kĩ năng",
                                                                "Gặp hiệu trưởng báo cáo"
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 5000 },
                                                                { LUONG_ID, 200 }
                                                })

                                                .build(),
                                builder()
                                                .taskId(10)
                                                .minLevel(10)
                                                .npcTalk("Đây là nhiệm vụ đầu tiên của con")

                                                .name("NV bài học đầu tiên")
                                                .detail("Ghi chú: rùa vàng (Ký túc xá Haruna), nhện đốm (Rừng đào Sakura), quỷ 1 mắt(Hồ Stuki)")
                                                .subNames(new String[] {
                                                                "Đánh 50 con rùa vàng",
                                                                "Đánh 30 con nhện đốm",
                                                                "Đánh 20 con quỷ 1 mắt",
                                                                "Báo cáo với hiệu trưởng"
                                                })
                                                .counts(new int[] {
                                                                50, 30, 20, -1
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 50000 },
                                                                { LUONG_ID, 200 }
                                                })
                                                .build(),
                                builder()
                                                .taskId(11)
                                                .npcTalk("Gần mực thì đen gần đèn thì sáng, nên con hãy chọn bạn mà chơi")
                                                .minLevel(11)

                                                .name("NV Bạn bè giao hữu")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 11",
                                                                "Giao lưu bạn hữu",
                                                                "Báo cáo sư phụ"
                                                })
                                                .bypass(true)
                                                .counts(new int[] {
                                                                -1, 1, -1,
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 500000 },
                                                                { LUONG_ID, 200 }
                                                })
                                                .build(),
                                builder()
                                                .taskId(12)
                                                .minLevel(12)
                                                .npcTalk("Đồ dùng giúp con tăng khả năng tấn công phòng thủ đồng thời giúp con đẹp hơn")
                                                .name("NV nâng cấp trang bị")
                                                .detail("Ghi chú: đồ có thể mua ở các shop, đá đánh quái để nhặt, gặp Kenshinto để đập đồ")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 12",
                                                                "Nâng cấp vũ khí",
                                                                "Nâng cấp trang sức",
                                                                "Nâng cấp y phục",
                                                                "Báo cáo sư phụ"
                                                })
                                                .rewards(new int[][] {
                                                                { LUONG_ID, 150 }
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(13)
                                                .minLevel(14)
                                                .npcTalk("Thách đấu với thầy cô để biết được thực lực của con tới đâu mà cố gắng luyện tập")

                                                .name("NV thách đấu")
                                                .detail("Ghi chú: Gặp các thầy cô các trường giao lưu võ thuật")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 14",
                                                                "Thách đấu thầy Ookamesama",
                                                                "Thách đấu thầy Kazeto",
                                                                "Thách đấu cô Toyotomi",
                                                                "Báo cáo Katana"
                                                })
                                                .bypass(true)
                                                .rewards(new int[][] {
                                                                { EXP_ID, 500_000 },
                                                                { LUONG_ID, 150 },
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(14)
                                                .minLevel(19)
                                                .npcTalk("Ta làm trang sức để bán nhưng lại hết nguyên liêu con có thể tìm giúp ta không ?")
                                                .mobs(new int[][] {
                                                                {},
                                                                {},
                                                                { 15, 0 },
                                                                {}
                                                })
                                                .itemsPick(new int[] { -1, 212, 213, -1 })
                                                .name("NV Thu thập nguyên liệu")
                                                .detail("Ghi chú: khoáng thạch thường có ở trong Hang Aka, vỏ Ốc đá ở Cánh đồng Hiya.")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 16",
                                                                "Tìm khoáng thạch",
                                                                "Nhặt vỏ Ốc Đá",
                                                                "Mang về cho cô Ameji"
                                                }).rewards(new int[][] {
                                                                { EXP_ID, 500_000 },
                                                                { LUONG_ID, 150 },
                                                })
                                                .counts(new int[] {
                                                                -1, 40, 60, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(15)
                                                .minLevel(19)
                                                .npcTalk("Ở đây ta có vài lá thư hãy giúp ta mang đến cho các tiền bối nhé")
                                                .receiveItems(new int[][] {
                                                                { 214, 3 }
                                                })
                                                .name("NV giao thư")
                                                .detail("")
                                                .npcMenu(new String[] {
                                                                "Nhận nhiệm vụ",
                                                                "Giao thư",
                                                                "Giao thư",
                                                                "Giao thư",
                                                                "Hoàn thành"
                                                })
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 19",
                                                                "Giao thư đến Hashimoto",
                                                                "Giao thư đến Fujiwara",
                                                                "Giao thư đến Nao",
                                                                "Báo cáo cô Furoyawa",
                                                }).rewards(new int[][] {
                                                                { EXP_ID, 500_000 },
                                                                { LUONG_ID, 150 },
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1, -1, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(16)
                                                .minLevel(22)
                                                .npcTalk("Để trở thành một nhẫn giả thành công con phải trải qua quá trình rèn luyện mài dũa con hãy đi luyện tập đi để được khoẻ như ta")
                                                .name("NV rèn luyện thể lực")
                                                .detail("")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 23, 0 },
                                                                { 24, 0 },
                                                                {},
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 500_000 },
                                                                { LUONG_ID, 150 },
                                                })
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 22",
                                                                "Giết Bò cạp đá",
                                                                "Giết Rắn lục",
                                                                "Báo cáo cho Nao biết",
                                                })
                                                .counts(new int[] {
                                                                -1, 250, 250, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(17)
                                                .minLevel(25)
                                                .npcTalk("Ta có đứa cháu do nó mãi ham chơi mà quên mất lối về con có thể giúp già này đi tìm nó về để già vổ mông nó không ?")
                                                .npcMenu(new String[] {
                                                                "",
                                                                "Dẫn em về với",
                                                                "Hoàn thành nhiệm vụ"
                                                })
                                                .name("NV Đưa Jaian trở về")
                                                .detail("")
                                                .bypass(true)
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 25",
                                                                "Giúp đỡ Jaian trở về",
                                                                "Báo tin với Rei",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1,
                                                                -1,
                                                                -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(18)
                                                .minLevel(25)
                                                .npcTalk("Sau 10 roi mà nó đã không chịu nổi rồi con có thể giúp ta tìm thuốc cứu nó không ?")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 26, 0 },
                                                                { 27, 0 },
                                                                {},

                                                })
                                                .name("NV nhặt nguyên liệu làm thuốc")
                                                .detail("")
                                                .subNames(new String[] {
                                                                // KO RO
                                                                "Trình độ đạt cấp 26",
                                                                "Thu thập Hầu tửu",
                                                                "Xác Châu chấu",
                                                                "Báo tin với Rei",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .itemsPick(new int[] {
                                                                -1,
                                                                216,
                                                                217,
                                                                -1
                                                })
                                                .counts(new int[] {
                                                                -1, 150, 150, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(19)
                                                .minLevel(26)
                                                .npcTalk("Thuốc kia con mang về chưa đủ làng ta nước nhiều nhưng toàn nước mặn không thích hợp làm thuốc bôi vào vết thương rất là rát nên con hay lấy 1 ít nước ngọt về giúp ta nhé")
                                                .receiveItems(new int[][] {
                                                                { 219, 100 },
                                                                {},
                                                                {}
                                                })
                                                .itemsPick(new int[] {
                                                                -1, 220, -1
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .name("NV lấy nước rừng sâu")
                                                .detail("")
                                                .subNames(new String[] {
                                                                // KO RO
                                                                "Trình độ đạt cấp 26",
                                                                "Lấy nước thánh",
                                                                "Báo tin với Rei",
                                                })
                                                .counts(new int[] {
                                                                -1, 100, -1
                                                })
                                                .bypass(true)
                                                .build(),
                                // DONE
                                builder()
                                                .taskId(20)
                                                .minLevel(28)
                                                .npcTalk("Ta muốn phạt thằng Jain đi chẻ củi tội đi chơi mà quên đường về mà khổ nổi con heo nó cướp mất cái rìu rồi con hãy giúp ta tìm về để phạt thằng Jain nhé")
                                                .name("NV tìm lại chiếc rìu")
                                                .npcMenu(new String[] {
                                                                "",
                                                                "Vào hang",
                                                                "Hoàn thành nhiệm vụ"
                                                })
                                                .rewards(new int[][] {
                                                                { 223, 1 },
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .mobs(new int[][] {
                                                                {},
                                                                { 69, 0 },
                                                                {}
                                                })
                                                .itemsPick(new int[] {
                                                                -1,
                                                                221,
                                                                -1
                                                })
                                                .detail("Đánh con lợn rừng để tìm lại chiếc rìu gia truyền của bả Rei")
                                                .subNames(new String[] {
                                                                // KO RO
                                                                "Trình độ đạt cấp 28",
                                                                "Tìm lại chiếc rìu",
                                                                "Báo tin với Rei",
                                                })
                                                .counts(new int[] {
                                                                -1, 1, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(21)
                                                .minLevel(30)
                                                .npcTalk("Để có thể hoàn thành tốt nhiệm vụ lần này con phải khoẻ hơn nữa")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 30, 0 },
                                                                { 31, 0 },
                                                                {}
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .name("NV Rèn luyện sức mạnh")
                                                .detail("")
                                                .subNames(new String[] {
                                                                // KO RO
                                                                "Trình độ đạt cấp 30",
                                                                "Đánh quỷ băng",
                                                                "Đánh quỷ hoa",
                                                                "Báo tin với Matsurugi",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1, 100, 100, -1,
                                                })
                                                .build(),

                                builder()
                                                .taskId(22)
                                                .minLevel(32)
                                                .mobs(new int[][] {
                                                                {},
                                                                { 33, 0 },
                                                                {}
                                                })
                                                .npcTalk("Khi đi vào rừng ta có đánh rơi khoảng 100 cái chìa khoá nhỏ để đúc thành 1 cái chìa khoá lớn mở kho báu ta phải tìm đủ số lượng chìa khoá")
                                                .itemsPick(new int[] {
                                                                -1,
                                                                230,
                                                                -1
                                                })
                                                .name("NV nhặt chìa khoá cơ quan")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 32",
                                                                "Nhặt chìa khoá cơ quan",
                                                                "Báo tin với Matsurugi",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1, 100, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(23)
                                                .minLevel(35)
                                                .npcTalk("Đây ta giao cho con chìa khoá để đi tìm tấm bản đồ về để tìm kho báu nhé")
                                                .receiveItems(new int[][] {
                                                                { 230, 1 }
                                                })
                                                .itemsPick(new int[] {
                                                                -1,
                                                                232,
                                                                -1
                                                })
                                                .name("NV nhặt lại tấm địa đồ")
                                                .detail("")
                                                .subNames(new String[] {
                                                                // KO RO
                                                                "Trình độ đạt cấp 35",
                                                                "Nhặt lại tấm địa đồ",
                                                                "Báo tin với Matsurugi",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1, 1, -1
                                                })
                                                .bypass(true)
                                                .build(),
                                builder()
                                                .taskId(24)
                                                .minLevel(36)
                                                .receiveItems(new int[][] {
                                                                { 234, 1 }
                                                })
                                                .rewards(new int[][] {
                                                                { 224, 1 },
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },

                                                })
                                                .npcTalk("Sao con không giữ tấm bảng đồ để đi tìm báu vật, thôi lỡ rồi ta giao lại cho con để đi tìm báu vật nhé, cứ đi theo tấm bản đồ là tìm được, "
                                                                +
                                                                "còn khó quá lên youtube coi nhiều ông tìm được lắm")

                                                .name("NV tìm báu vật")
                                                .detail("Sử đụng địa đồ để đi tìm báu vật")
                                                .subNames(new String[] {
                                                                // KO RO
                                                                "Trình độ đạt cấp 36",
                                                                "Tìm báu vật",
                                                                "Báo tin với Matsurugi",
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1
                                                })
                                                .build(),
                                // DONE
                                builder()
                                                .taskId(25)
                                                .minLevel(37)
                                                .mobs(new int[][] {
                                                                { -1, 0 },
                                                                { 37, 0 },
                                                                { 35, 0 },
                                                                { -1, 0 },
                                                })
                                                .npcTalk("Nâng cao thể lực để hoàn thành nhiệm vụ lần này cho tốt con nhé")
                                                .name("NV rèn luyện sức khoẻ")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 37",
                                                                "Đánh bại Rết tinh",
                                                                "Đánh bại cua biển",
                                                                "Mang về cho Kirin",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },

                                                })
                                                .counts(new int[] {
                                                                -1, 250, 250, -1
                                                })
                                                .build(),
                                // DONE
                                builder()
                                                .taskId(26)
                                                .minLevel(38)
                                                .npcTalk("Trời dạo này hơi nóng, con có thể giúp ta tìm một ít tinh thể băng về làm nước đá uống không")
                                                .itemsPick(new int[] {
                                                                -1, 236, -1
                                                })
                                                .name("NV nhặt tinh thể băng")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 38",
                                                                "Nhặt tinh thể băng",
                                                                "Mang về cho Kirin",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1, 100, -1
                                                })
                                                .bypass(true)
                                                .build(),

                                builder()
                                                .taskId(27)
                                                .minLevel(39)
                                                .npcTalk("Dạo này xương khớp ta hơi nhức," +
                                                                " ta nghe đồn là xác dơi lửa có thể bào chế " +
                                                                "được phương thuốc có thể giúp ta nhanh khỏi bệnh")

                                                .name("NV nhặt xác dơi lửa")
                                                .detail("")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 41, 0 },
                                                                {}
                                                })
                                                .itemsPick(new int[] {
                                                                -1, 238, 239
                                                })

                                                .rewards(new int[][] {
                                                                { 224, 1 },
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 39",
                                                                "Nhặt xác dơi lửa",
                                                                "Mang về cho Kirin",
                                                })
                                                .counts(new int[] {
                                                                -1, 100, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(28)
                                                .minLevel(41)
                                                .npcTalk("Dạo này mùa màng thất thu, sau một thời gian tìm hiểu ta phát hiện"
                                                                +
                                                                " ra cây cối bị Ốc ma và chuột hoang phá hoại con có thể giúp ta tiêu diệt chúng không ?")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 42, 0 },
                                                                { 43, 0 },
                                                                {}
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 8_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .name("NV kiên trì diệt ác")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 41",
                                                                "Tiêu diệt Ốc ma",
                                                                "Tiêu diệt Chuột hoang",
                                                                "Báo cáo với Soba",
                                                })
                                                .counts(new int[] {
                                                                -1, 500, 500, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(29)
                                                .minLevel(43)
                                                .npcTalk("Dù ma tinh anh rất khôn lanh thường ẩn nấp trong các khu rừng, "
                                                                +
                                                                "thịt chúng nhậu thì bá chấy con có thể săn vài con về chúng ta làm mồi không ?")
                                                .mobs(new int[][] {
                                                                {}, { 44, 1 }, {}
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 80_000_000 },
                                                                { LUONG_ID, 300 },
                                                })
                                                .name("NV Giết Tinh Anh")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 43 ",
                                                                "Tiêu diệt Dù ma Tinh Anh",
                                                                "Báo cáo với Soba",
                                                })
                                                .counts(new int[] {
                                                                -1, 10, -1
                                                })
                                                .build(),
                                builder()
                                                .taskId(30)
                                                .minLevel(44)
                                                .npcTalk("Nhẫn nại nhẫn nại.... là một đức tính cần thiết của một Ninja")
                                                .name("NV tuần hoàn")
                                                .mobs(new int[][] {
                                                                {},
                                                                {},
                                                                { -1, 3 },
                                                                {}
                                                })
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 44 ",
                                                                "Hoàn thành nhiệm vụ mỗi ngày",
                                                                "Hoàn thành nhiệm vụ bắt tà thú",
                                                                "Báo cáo với Soba",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 80_000_000 },
                                                                { LUONG_ID, 500 },
                                                })
                                                .counts(new int[] {
                                                                -1, 10, 1, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(31)
                                                .minLevel(45)
                                                .npcTalk("Khá lắm trình độ của con là tốt hơn rồi, con hãy giúp dân làng tích trữ lương thực cho mùa đông nhé")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 47, 0 },
                                                                { 48, 0 },
                                                                {}
                                                })
                                                .itemsPick(new int[] {
                                                                -1,
                                                                264,
                                                                265,
                                                                -1
                                                })
                                                .name("NV dự trữ lương thực")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 45",
                                                                "Bắt Cá hổ",
                                                                "Bắt Rắn tía",
                                                                "Báo cáo với Soba",
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 80_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1, 200, 200, -1
                                                })
                                                .build(),

                                builder()
                                                .taskId(32)
                                                .minLevel(49)
                                                .npcTalk("Ta đã đánh rơi món đồ dưới ao có có thể đi nhặt lên giúp ta không")
                                                .itemsPick(new int[] {
                                                                -1,
                                                                267,
                                                                -1
                                                })
                                                .receiveItems(new int[][] {
                                                                {},
                                                                { 266, 1 },
                                                                {}
                                                })
                                                .rewards(new int[][] {
                                                                { 225, 1 },
                                                                { EXP_ID, 80_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .name("NV rèn luyện ý chí")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 49",
                                                                "Tìm lại vật phẩm",
                                                                "Báo cáo với Soba",
                                                })
                                                .counts(new int[] {
                                                                -1, -1, -1,
                                                })
                                                .build(),

                                builder()
                                                .taskId(34)
                                                .minLevel(51)
                                                .npcTalk("Làng ta ma bao vây, chúng nó quấy phá làm làng ta không thể nào yên bình con có thể giúp ta diệt trừ chúng không ?")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 51, 0 },
                                                                { 52, 0 },
                                                                {}
                                                })
                                                .name("NV Diệt ma")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 51",
                                                                "Tiêu diệt ma da mẹ",
                                                                "Tiêu diệt ma da con",
                                                                "Báo cáo Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 250, 250, -1
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 80_000_000 },
                                                                { LUONG_ID, 200 },

                                                })
                                                .build(),

                                builder()
                                                .taskId(35)
                                                .minLevel(53)
                                                .npcTalk("Ta muốn chiêu đãi con đặc sản làng ta nhưng mà nguyên liệu lại hết. Ta thì chân đang đau, con có thể giúp ta hái 1 ít nấm về chế biến món ăn không ?")
                                                .itemsPick(new int[] {
                                                                -1, 347, -1

                                                })
                                                .name("NV Hái Nấm")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 53",
                                                                "Thu thập nấm",
                                                                "Báo cáo với Guriin"
                                                })
                                                .rewards(new int[][] {
                                                                { 226, 1 },
                                                                { EXP_ID, 80_000_000 },
                                                                { LUONG_ID, 200 },
                                                })
                                                .counts(new int[] {
                                                                -1, 99, -1,
                                                })
                                                .minLevel(53)
                                                .build(),

                                builder()
                                                .taskId(36)
                                                .minLevel(55)
                                                .npcTalk("Ngôi làng chuẩn bị đóng một đợt hạn hán dài nguồn nước quanh làng đã sắp không trụ nổi con có thể giúp dân làng múc nước tích trữ không")

                                                .name("NV Giúp đỡ dân làng")
                                                .detail("Đến đỉnh Ichidai để múc nước")
                                                .receiveItems(new int[][] {
                                                                {},
                                                                { 219, 150 },
                                                                {}
                                                })
                                                .itemsPick(new int[] {
                                                                -1,
                                                                220,
                                                                -1,
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 300 },
                                                })
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 55",
                                                                "Múc nước",
                                                                "Báo cáo với Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 150, -1,
                                                })
                                                .minLevel(55)
                                                .build(),

                                builder()
                                                .taskId(37)
                                                .minLevel(57)
                                                .npcTalk("Ban đêm có những hồn ma lãng vãng quanh làng hù trẻ em và người già rất nhiều, không ít những người yếu bóng vía trở thành hồn ma do bị hù con có thể giúp ta thu thập hồn mà của họ về để ta làm lễ cho họ không")
                                                .itemsPick(new int[] {
                                                                -1,
                                                                348,
                                                                -1
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 300 },
                                                })
                                                .name("NV Thu thập oan hồn")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 57",
                                                                "Thu thập Oan hồn",
                                                                "Báo cáo với Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 100, -1,
                                                })
                                                .minLevel(57)
                                                .build(),

                                builder()
                                                .taskId(38)
                                                .minLevel(59)
                                                .npcTalk("Ta có thử thách nho nhỏ dành cho con đây")
                                                .mobs(new int[][] {
                                                                {},
                                                                { 58, 1 },
                                                                { 59, 2 },
                                                                {}
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 300 },
                                                })
                                                .name("NV Thử thách của Guriin")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 59",
                                                                "Tiêu diệt Tinh anh bọ Xanh",
                                                                "Tiêu diệt Thủ lĩnh Sên",
                                                                "Báo cáo với Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 15, 10, -1,
                                                })
                                                .build(),

                                builder()
                                                .taskId(39)
                                                .minLevel(61)
                                                .npcTalk("Sắp tới sinh nhật cô con gái bé bỏng của ta ta muốn tạo bất ngờ cho nó. Tuy nhiên ta để ý thấy ngươi hay nhìn trộm con gái của ta."
                                                                +
                                                                " Mà thôi, ta thấy nhà người cũng đẹp trai có tài nên lần này ta cho người đi kiếm nguyên liệu làm lòng đèn tổ chức sinh nhật cho con ta. "
                                                                +
                                                                "Nếu con muốn thành con rễ của ta thì hãy chấp nhận thử thách này ?")
                                                .rewards(new int[][] {
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 300 },
                                                })
                                                .name("NV Thắp sáng bản làng")
                                                .detail("")
                                                .itemsPick(new int[] {
                                                                -1,
                                                                349,
                                                                350,
                                                                -1
                                                })
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 61",
                                                                "Nhặt lồng đèn",
                                                                "Nhặt cánh bướm",
                                                                "Báo cáo với Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 100, 150, -1,
                                                })
                                                .mobs(new int[][] {
                                                                {},
                                                                { 60, 0 },
                                                                { 61, 0 },
                                                                {}
                                                })
                                                .minLevel(61)
                                                .build(),

                                builder()
                                                .taskId(40)
                                                .minLevel(63)
                                                .npcTalk("Sống gió phủ đời trai tương lai nhờ nhà vợ à câu này không đúng với nhà ta lắm. Nhà ta áp dụng câu không làm mà đòi ăn thì chỉ có ăn ... à mà thôi "
                                                                +
                                                                " chắc câu này quá nổi tiếng rồi. Nên con hãy siêng năng lao động để sau này có thể tự lo cho bản thân gia đình vợ con.... blah blah")
                                                .name("NV Hoạt động hằng ngày")
                                                .detail("")
                                                .mobs(new int[][] {
                                                                {},
                                                                { -1, 3 },
                                                                {},
                                                                {},
                                                                {}
                                                })
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 63",
                                                                "Hoàn thành nhiệm vụ tà thú",
                                                                "Hoàn thành hang động",
                                                                "Hoàn thành nhiệm vụ hằng ngày",
                                                                "Báo cáo với Guriin"
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 500 },
                                                })
                                                .counts(new int[] {
                                                                -1, 10, 1, 100, -1
                                                })
                                                .minLevel(63)
                                                .build(),

                                builder()
                                                .taskId(41)
                                                .minLevel(65)
                                                .npcTalk("Sau khi cố gắng lao động mà không đủ ăn thì thử đen đỏ thử xem, hồi còn giàu ta cũng hay chơi mấy đồ yêu này lắm")
                                                .name("NV Thử tài may mắn")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 65",
                                                                "Sử dụng rương hang động",
                                                                "Sử dụng rương thất thú bảo",
                                                                "Báo cáo với Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 50, 10, -1
                                                })
                                                .mobs(new int[][] {
                                                                {},
                                                                {},
                                                                { 0, 0 },
                                                                {}
                                                })
                                                .itemsPick(new int[] {
                                                                -1,
                                                                -1,
                                                                288,
                                                                -1
                                                })
                                                .rewards(new int[][] {
                                                                { 227, 1 },
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 500 },
                                                })
                                                .minLevel(65)
                                                .build(),
                                builder()
                                                .taskId(42)
                                                .minLevel(67)
                                                .npcTalk("Bệnh thành tích là không tốt nhưng không có thành tích thì không ai nể thôi cố gắng lấy thành tích về đi bố vợ tưởng lai hứa sẽ gả con gái cho")
                                                .name("NV Chiến trường")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 67",
                                                                "Đạt 2000 điểm tích luỹ phe Bạch giả",
                                                                "Đạt 2000 điểm tích luỹ phe Hắc giả",
                                                                "Báo cáo với Guriin"
                                                })
                                                .counts(new int[] {
                                                                -1, 2000, 2000, -1,
                                                })
                                                .rewards(new int[][] {
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 500 },
                                                })
                                                .minLevel(67)
                                                .build(),

                                builder()
                                                .taskId(43)
                                                .minLevel(69)
                                                .npcTalk("Ta hứa là đây là nhiệm vụ cuối cùng ráng làm đi ta sẽ thưởng to")

                                                .name("NV Thách đấu lôi đài")
                                                .detail("")
                                                .subNames(new String[] {
                                                                "Trình độ đạt cấp 69",
                                                                "Chiến thắng lôi đài",
                                                                "Báo cáo với Guriin"
                                                })
                                                .rewards(new int[][] {
                                                                { 228, 1 },
                                                                { EXP_ID, 100_000_000 },
                                                                { LUONG_ID, 500 },
                                                })
                                                .counts(new int[] {
                                                                -1, 50, -1
                                                })
                                                .minLevel(69)
                                                .build(),
                };
        }
}
