import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Lab 3 - Bài 7: Máy tính mini. (bài tự làm, không có gợi ý code)
 *
 * Yêu cầu:
 *   - nhập hai số và các nút: Cộng, Trừ, Nhân, Chia, Clear
 *   - kết quả hiển thị trên JTextField không cho sửa
 *   - xử lý lỗi nhập sai định dạng số và lỗi chia cho 0
 *   - mở rộng: JTextArea hiển thị lịch sử các phép tính đã thực hiện
 *
 * Điểm đáng học:
 *   - bốn nút phép tính dùng CHUNG một hàm tinhToan(char), chỉ khác tham số toán tử;
 *     viết bốn listener giống hệt nhau là thừa
 *   - với số thực, a / 0 trong Java KHÔNG ném exception mà trả về Infinity hoặc NaN,
 *     nên phải tự kiểm tra mẫu số bằng 0 trước khi chia
 */
class Bai07MayTinhMini {

    static final double EPS = 1e-9;

    static JFrame frame;
    static JTextField txtA;
    static JTextField txtB;
    static JTextField txtKetQua;
    static JTextArea txtLichSu;
    static int soPhepTinh = 0;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 7 - Máy tính mini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        frame.add(taoPanelTren(), BorderLayout.NORTH);
        frame.add(taoPanelLichSu(), BorderLayout.CENTER);

        frame.setSize(470, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static JPanel taoPanelTren() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        txtA = new JTextField();
        txtB = new JTextField();
        txtKetQua = new JTextField();
        txtKetQua.setEditable(false);                 // ô kết quả chỉ để xem
        txtKetQua.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel panelNhap = new JPanel(new GridLayout(3, 2, 8, 8));
        panelNhap.add(new JLabel("Số thứ nhất:"));
        panelNhap.add(txtA);
        panelNhap.add(new JLabel("Số thứ hai:"));
        panelNhap.add(txtB);
        panelNhap.add(new JLabel("Kết quả:"));
        panelNhap.add(txtKetQua);

        JButton btnCong = new JButton("Cộng (+)");
        JButton btnTru = new JButton("Trừ (-)");
        JButton btnNhan = new JButton("Nhân (×)");
        JButton btnChia = new JButton("Chia (÷)");
        JButton btnClear = new JButton("Clear");

        // Cùng một hàm xử lý, khác nhau ở toán tử truyền vào
        btnCong.addActionListener(e -> tinhToan('+'));
        btnTru.addActionListener(e -> tinhToan('-'));
        btnNhan.addActionListener(e -> tinhToan('*'));
        btnChia.addActionListener(e -> tinhToan('/'));
        btnClear.addActionListener(e -> xoaTat());

        JPanel panelNut = new JPanel(new GridLayout(1, 5, 6, 6));
        panelNut.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelNut.add(btnCong);
        panelNut.add(btnTru);
        panelNut.add(btnNhan);
        panelNut.add(btnChia);
        panelNut.add(btnClear);

        panel.add(panelNhap, BorderLayout.CENTER);
        panel.add(panelNut, BorderLayout.SOUTH);
        return panel;
    }

    static JScrollPane taoPanelLichSu() {
        txtLichSu = new JTextArea(10, 40);
        txtLichSu.setEditable(false);
        txtLichSu.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtLichSu.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scroll = new JScrollPane(txtLichSu);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 12, 12, 12),
                BorderFactory.createTitledBorder("Lịch sử các phép tính")));
        return scroll;
    }

    static void tinhToan(char toanTu) {
        Double a = docSo(txtA, "thứ nhất");
        if (a == null) return;
        Double b = docSo(txtB, "thứ hai");
        if (b == null) return;

        double ketQua;
        switch (toanTu) {
            case '+' -> ketQua = a + b;
            case '-' -> ketQua = a - b;
            case '*' -> ketQua = a * b;
            case '/' -> {
                // Số thực chia 0 cho ra Infinity/NaN chứ không ném exception, phải tự chặn
                if (Math.abs(b) < EPS) {
                    txtKetQua.setText("Lỗi: chia cho 0");
                    JOptionPane.showMessageDialog(frame,
                            "Không thể chia cho 0. Hãy nhập số thứ hai khác 0.",
                            "Lỗi phép chia", JOptionPane.ERROR_MESSAGE);
                    txtB.requestFocus();
                    txtB.selectAll();
                    return;
                }
                ketQua = a / b;
            }
            default -> {
                return; // không bao giờ xảy ra vì toán tử do chính chương trình truyền vào
            }
        }

        String dongKetQua = String.format(Locale.US, "%s %c %s = %s",
                soGon(a), kyHieu(toanTu), soGon(b), soGon(ketQua));
        txtKetQua.setText(soGon(ketQua));
        ghiLichSu(dongKetQua);
    }

    static void ghiLichSu(String dong) {
        soPhepTinh++;
        txtLichSu.append(String.format("%2d. %s%n", soPhepTinh, dong));
        // Luôn cuộn xuống dòng mới nhất
        txtLichSu.setCaretPosition(txtLichSu.getDocument().getLength());
    }

    static void xoaTat() {
        txtA.setText("");
        txtB.setText("");
        txtKetQua.setText("");
        txtA.requestFocus();
    }

    static Double docSo(JTextField txt, String tenO) {
        String s = txt.getText().trim().replace(',', '.');
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
        txtKetQua.setText("");
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        oCanSua.requestFocus();
        oCanSua.selectAll();
    }

    /** Ký hiệu đẹp để ghi vào lịch sử: * hiển thị thành ×, / thành ÷ */
    static char kyHieu(char toanTu) {
        return switch (toanTu) {
            case '*' -> '×';
            case '/' -> '÷';
            default -> toanTu;
        };
    }

    /** 10.0 -> "10", 3.3333333 -> "3.3333" cho gọn màn hình. */
    static String soGon(double d) {
        if (d == Math.rint(d) && !Double.isInfinite(d)) {
            return String.valueOf((long) d);
        }
        return String.format(Locale.US, "%.4f", d);
    }
}
