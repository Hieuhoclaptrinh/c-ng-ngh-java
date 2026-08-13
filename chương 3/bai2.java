import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Lab 3 - Bài 2: Tính tổng hai số.
 *
 * Yêu cầu:
 *   - nhập số thứ nhất, số thứ hai
 *   - nút "Tính tổng" hiển thị kết quả trên JLabel
 *   - nút "Làm mới" xóa dữ liệu và đưa con trỏ về ô nhập đầu tiên
 *   - nhập sai kiểu số thì báo lỗi bằng JOptionPane, không được ném exception ra ngoài
 *
 * Kiểm thử: a = 7.5, b = 2.5 -> Kết quả: 10.0
 *
 * Điểm đáng học: bắt NumberFormatException. Double.parseDouble("abc") ném
 * NumberFormatException; nếu không bắt, chương trình in stack trace và người dùng
 * không hiểu chuyện gì xảy ra.
 */
class Bai02TongHaiSo {

    static JFrame frame;
    static JTextField txtA;
    static JTextField txtB;
    static JLabel lblKetQua;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 2 - Tính tổng hai số");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // GridLayout: lưới 4 dòng x 2 cột, mọi ô đều bằng nhau
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        txtA = new JTextField();
        txtB = new JTextField();
        lblKetQua = new JLabel("Kết quả: ");

        JButton btnTinh = new JButton("Tính tổng");
        JButton btnLamMoi = new JButton("Làm mới");

        panel.add(new JLabel("Số thứ nhất:"));
        panel.add(txtA);
        panel.add(new JLabel("Số thứ hai:"));
        panel.add(txtB);
        panel.add(btnTinh);
        panel.add(btnLamMoi);
        panel.add(new JLabel(""));   // ô trống cho đủ lưới 4x2
        panel.add(lblKetQua);

        frame.add(panel);

        btnTinh.addActionListener(e -> tinhTong());
        btnLamMoi.addActionListener(e -> lamMoi());

        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void tinhTong() {
        Double a = docSo(txtA, "thứ nhất");
        if (a == null) return;           // ô a sai thì dừng luôn, không đọc tiếp ô b
        Double b = docSo(txtB, "thứ hai");
        if (b == null) return;

        // Locale.US để in dấu chấm thập phân; máy đang đặt locale vi_VN sẽ in dấu phẩy
        lblKetQua.setText(String.format(Locale.US, "Kết quả: %s", a + b));
    }

    static void lamMoi() {
        txtA.setText("");
        txtB.setText("");
        lblKetQua.setText("Kết quả: ");
        txtA.requestFocus();
    }

    /** Đọc một số thực từ ô nhập. Trả về null (và báo lỗi) nếu ô trống hoặc không phải số. */
    static Double docSo(JTextField txt, String tenO) {
        String s = txt.getText().trim().replace(',', '.'); // cho phép gõ cả dấu phẩy
        if (s.isEmpty()) {
            baoLoi("Bạn chưa nhập số " + tenO + ".", txt);
            return null;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            baoLoi("Số " + tenO + " không hợp lệ: \"" + txt.getText().trim() + "\"", txt);
            return null;
        }
    }

    static void baoLoi(String thongBao, JTextField oCanSua) {
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        oCanSua.requestFocus();
        oCanSua.selectAll();
    }
}
