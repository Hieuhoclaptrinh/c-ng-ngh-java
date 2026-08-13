import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Bài 3: Máy tính đơn giản - luyện GridLayout và xử lý sự kiện cho nhiều nút.
 *
 * Điểm đáng học: thay vì viết 16 ActionListener, ta dùng MỘT listener chung
 * rồi phân loại dựa vào nhãn của nút (e.getActionCommand()).
 */
class Bai3MayTinh {

    static JTextField manHinh;      // ô hiển thị số
    static double soTruoc = 0;      // toán hạng đã nhập trước đó
    static String phepToan = "";    // phép toán đang chờ: + - * /
    static boolean batDauSoMoi = true; // true = ký tự số tiếp theo sẽ ghi đè màn hình

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        JFrame frame = new JFrame("Bài 3 - Máy tính");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(8, 8));

        manHinh = new JTextField("0");
        manHinh.setEditable(false); // chỉ nhập bằng cách bấm nút
        manHinh.setHorizontalAlignment(JTextField.RIGHT);
        manHinh.setFont(new Font("Monospaced", Font.BOLD, 28));
        manHinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bàn phím 5 dòng x 4 cột
        String[] nhan = {
                "C", "±", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "=", ""
        };

        JPanel banPhim = new JPanel(new GridLayout(5, 4, 5, 5));
        banPhim.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

        for (String s : nhan) {
            if (s.isEmpty()) {
                banPhim.add(new JPanel()); // ô trống cho khớp lưới
                continue;
            }
            JButton btn = new JButton(s);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.setFocusPainted(false);
            btn.addActionListener(e -> xuLyNut(e.getActionCommand(), frame));
            banPhim.add(btn);
        }

        frame.add(manHinh, BorderLayout.NORTH);
        frame.add(banPhim, BorderLayout.CENTER);
        frame.setSize(330, 420);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void xuLyNut(String lenh, JFrame frame) {
        switch (lenh) {
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                if (batDauSoMoi) {
                    manHinh.setText(lenh);
                    batDauSoMoi = false;
                } else if (manHinh.getText().equals("0")) {
                    manHinh.setText(lenh); // tránh kiểu "007"
                } else {
                    manHinh.setText(manHinh.getText() + lenh);
                }
                break;

            case ".":
                if (batDauSoMoi) {
                    manHinh.setText("0.");
                    batDauSoMoi = false;
                } else if (!manHinh.getText().contains(".")) {
                    manHinh.setText(manHinh.getText() + ".");
                }
                break;

            case "C": // xóa hết, về trạng thái ban đầu
                manHinh.setText("0");
                soTruoc = 0;
                phepToan = "";
                batDauSoMoi = true;
                break;

            case "±": // đổi dấu số đang hiện
                double d = laySoTrenManHinh();
                manHinh.setText(dinhDang(-d));
                break;

            case "%": // đổi số đang hiện thành phần trăm
                manHinh.setText(dinhDang(laySoTrenManHinh() / 100));
                break;

            case "+": case "-": case "*": case "/":
                // Bấm liên tiếp "2 + 3 +" thì phải tính 2+3 trước rồi mới chờ toán hạng mới
                if (!phepToan.isEmpty() && !batDauSoMoi) {
                    if (!tinhVaHienKetQua(frame)) return;
                } else {
                    soTruoc = laySoTrenManHinh();
                }
                phepToan = lenh;
                batDauSoMoi = true;
                break;

            case "=":
                if (phepToan.isEmpty()) return;
                if (tinhVaHienKetQua(frame)) {
                    phepToan = "";
                }
                break;
        }
    }

    /** Thực hiện phép toán đang chờ. Trả về false nếu lỗi (chia cho 0). */
    static boolean tinhVaHienKetQua(JFrame frame) {
        double soSau = laySoTrenManHinh();

        if (phepToan.equals("/") && soSau == 0) {
            JOptionPane.showMessageDialog(frame, "Không thể chia cho 0!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            manHinh.setText("0");
            soTruoc = 0;
            phepToan = "";
            batDauSoMoi = true;
            return false;
        }

        double kq = switch (phepToan) {
            case "+" -> soTruoc + soSau;
            case "-" -> soTruoc - soSau;
            case "*" -> soTruoc * soSau;
            case "/" -> soTruoc / soSau;
            default -> soSau;
        };

        manHinh.setText(dinhDang(kq));
        soTruoc = kq;
        batDauSoMoi = true;
        return true;
    }

    static double laySoTrenManHinh() {
        try {
            return Double.parseDouble(manHinh.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Bỏ ".0" cho số nguyên, và luôn dùng dấu chấm thập phân (máy đang ở locale vi_VN). */
    static String dinhDang(double d) {
        if (Math.abs(d) < 1e15 && d == Math.rint(d)) {
            return String.valueOf((long) d);
        }
        String s = String.format(Locale.US, "%.10f", d);
        s = s.replaceAll("0+$", ""); // cắt các số 0 vô nghĩa ở cuối
        return s;
    }
}
