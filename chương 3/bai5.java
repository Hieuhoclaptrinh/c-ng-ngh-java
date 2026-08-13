import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
 * Lab 3 - Bài 5: Hiển thị n số Fibonacci đầu tiên trong JTextArea.
 *
 * Yêu cầu:
 *   - nhập n, bấm nút "Hiển thị"
 *   - kết quả nằm trong JTextArea đặt trong JScrollPane
 *   - n phải là số nguyên dương, giới hạn n <= 92 để không tràn kiểu long
 *
 * Kiểm thử: n = 7 -> 0 1 1 2 3 5 8
 *
 * Vì sao là 92? Fibonacci thứ 93 đã vượt quá Long.MAX_VALUE (khoảng 9.22e18).
 * Nếu vẫn cố tính, kiểu long sẽ "quay vòng" sang số âm mà KHÔNG báo lỗi gì cả -
 * đây là loại lỗi khó phát hiện nhất, nên phải chặn ngay từ khâu nhập liệu.
 */
class Bai05FibonacciSwing {

    static final int N_TOI_DA = 92;

    static JFrame frame;
    static JTextField txtN;
    static JTextArea txtKetQua;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 5 - Hiển thị dãy Fibonacci");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        txtN = new JTextField(8);
        JButton btnHienThi = new JButton("Hiển thị");
        JButton btnXoa = new JButton("Xóa");

        JPanel panelTren = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        panelTren.add(new JLabel("Nhập n (1 - " + N_TOI_DA + "):"));
        panelTren.add(txtN);
        panelTren.add(btnHienThi);
        panelTren.add(btnXoa);

        txtKetQua = new JTextArea(10, 40);
        txtKetQua.setEditable(false);                          // chỉ để xem, không cho sửa
        txtKetQua.setLineWrap(true);                           // dài quá thì tự xuống dòng
        txtKetQua.setWrapStyleWord(true);                      // xuống dòng theo từ, không cắt giữa số
        txtKetQua.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtKetQua.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        btnHienThi.addActionListener(e -> hienThiFibonacci());
        txtN.addActionListener(e -> hienThiFibonacci());       // gõ Enter cũng chạy
        btnXoa.addActionListener(e -> {
            txtN.setText("");
            txtKetQua.setText("");
            txtN.requestFocus();
        });

        frame.add(panelTren, BorderLayout.NORTH);
        // JScrollPane bọc ngoài JTextArea để có thanh cuộn khi dãy số dài
        frame.add(new JScrollPane(txtKetQua), BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void hienThiFibonacci() {
        String s = txtN.getText().trim();
        if (s.isEmpty()) {
            baoLoi("Bạn chưa nhập n.");
            return;
        }

        try {
            int n = Integer.parseInt(s);
            if (n <= 0 || n > N_TOI_DA) {
                baoLoi("n phải nằm trong khoảng 1 đến " + N_TOI_DA
                        + " để tránh tràn kiểu long!");
                return;
            }
            txtKetQua.setText(taoDayFibonacci(n));
            txtKetQua.setCaretPosition(0); // cuộn về đầu nội dung
        } catch (NumberFormatException ex) {
            baoLoi("n phải là số nguyên dương!");
        }
    }

    /** Sinh n số Fibonacci đầu tiên: 0 1 1 2 3 5 8 ... */
    static String taoDayFibonacci(int n) {
        StringBuilder sb = new StringBuilder();
        long a = 0;
        long b = 1;
        for (int i = 1; i <= n; i++) {
            if (i > 1) sb.append(" ");
            sb.append(a);
            long tiepTheo = a + b;   // phải lưu tạm trước khi ghi đè lên a
            a = b;
            b = tiepTheo;
        }
        return sb.toString();
    }

    static void baoLoi(String thongBao) {
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        txtN.requestFocus();
        txtN.selectAll();
    }
}
