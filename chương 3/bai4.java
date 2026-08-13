import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
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
 * Lab 3 - Bài 4: Nhập 3 số, kiểm tra và phân loại tam giác.
 *
 * Yêu cầu:
 *   - nhập ba cạnh a, b, c
 *   - kiểm tra điều kiện tạo thành tam giác (tổng hai cạnh phải lớn hơn cạnh còn lại)
 *   - nếu là tam giác thì phân loại: đều, vuông cân, vuông, cân, thường
 *
 * Kiểm thử: 3, 4, 5 -> tam giác vuông
 *           3, 3, 3 -> tam giác đều
 *           1, 2, 3 -> không phải tam giác
 *
 * Điểm đáng học: thứ tự các phép kiểm tra rất quan trọng. Tam giác đều cũng là
 * tam giác cân, tam giác vuông cân vừa vuông vừa cân, nên phải xét từ trường hợp
 * ĐẶC BIỆT NHẤT xuống trường hợp chung nhất.
 */
class Bai04TamGiacSwing {

    static final double EPS = 1e-9;

    static JFrame frame;
    static JTextField txtA;
    static JTextField txtB;
    static JTextField txtC;
    static JLabel lblKetQua;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 4 - Kiểm tra tam giác");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        lblKetQua = new JLabel("Kết quả: ");
        lblKetQua.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblKetQua.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));

        txtA = new JTextField();
        txtB = new JTextField();
        txtC = new JTextField();

        JPanel panelNhap = new JPanel(new GridLayout(3, 2, 8, 8));
        panelNhap.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        panelNhap.add(new JLabel("Cạnh a:"));
        panelNhap.add(txtA);
        panelNhap.add(new JLabel("Cạnh b:"));
        panelNhap.add(txtB);
        panelNhap.add(new JLabel("Cạnh c:"));
        panelNhap.add(txtC);

        JButton btnKiemTra = new JButton("Kiểm tra");
        JButton btnLamMoi = new JButton("Làm mới");
        btnKiemTra.addActionListener(e -> kiemTraTamGiac());
        btnLamMoi.addActionListener(e -> lamMoi());

        JPanel panelNut = new JPanel();
        panelNut.add(btnKiemTra);
        panelNut.add(btnLamMoi);

        frame.add(lblKetQua, BorderLayout.NORTH);
        frame.add(panelNhap, BorderLayout.CENTER);
        frame.add(panelNut, BorderLayout.SOUTH);

        frame.setSize(440, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void kiemTraTamGiac() {
        Double a = docCanh(txtA, "a");
        if (a == null) return;
        Double b = docCanh(txtB, "b");
        if (b == null) return;
        Double c = docCanh(txtC, "c");
        if (c == null) return;

        lblKetQua.setText("Kết quả: " + loaiTamGiac(a, b, c));
    }

    /** Logic phân loại, tách hẳn khỏi giao diện. */
    static String loaiTamGiac(double a, double b, double c) {
        // Điều kiện tam giác: ba cạnh dương và tổng hai cạnh bất kỳ lớn hơn cạnh còn lại
        if (a <= 0 || b <= 0 || c <= 0 || a + b <= c || a + c <= b || b + c <= a) {
            return "Không phải tam giác";
        }

        boolean deu = Math.abs(a - b) < EPS && Math.abs(b - c) < EPS;
        boolean can = Math.abs(a - b) < EPS || Math.abs(a - c) < EPS || Math.abs(b - c) < EPS;

        // Sắp xếp để cạnh lớn nhất nằm cuối, khi đó kiểm tra Pythagore chỉ còn một phép so sánh
        double[] d = {a, b, c};
        Arrays.sort(d);
        boolean vuong = Math.abs(d[0] * d[0] + d[1] * d[1] - d[2] * d[2]) < EPS;

        if (deu) return "Tam giác đều";
        if (vuong && can) return "Tam giác vuông cân";
        if (vuong) return "Tam giác vuông";
        if (can) return "Tam giác cân";
        return "Tam giác thường";
    }

    static Double docCanh(JTextField txt, String tenCanh) {
        String s = txt.getText().trim().replace(',', '.');
        if (s.isEmpty()) {
            baoLoi("Bạn chưa nhập cạnh " + tenCanh + ".", txt);
            return null;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            baoLoi("Cạnh " + tenCanh + " phải là số hợp lệ.", txt);
            return null;
        }
    }

    static void lamMoi() {
        txtA.setText("");
        txtB.setText("");
        txtC.setText("");
        lblKetQua.setText("Kết quả: ");
        txtA.requestFocus();
    }

    static void baoLoi(String thongBao, JTextField oCanSua) {
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        oCanSua.requestFocus();
        oCanSua.selectAll();
    }
}
