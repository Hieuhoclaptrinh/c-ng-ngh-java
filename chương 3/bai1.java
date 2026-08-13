import java.awt.FlowLayout;
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
 * Lab 3 - Bài 1: Tạo giao diện chào người dùng.
 *
 * Yêu cầu:
 *   - JFrame có 1 JLabel, 1 JTextField và 1 JButton
 *   - bấm nút thì hiện hộp thoại "Xin chào, [Tên]!"
 *   - chưa nhập tên thì nhắc người dùng nhập, chương trình không được dừng đột ngột
 *
 * Kiểm thử: nhập "An" -> hiện "Xin chào, An!"
 *
 * Ba điều bắt buộc của mọi bài Swing trong lab này:
 *   1. giao diện được tạo trên EDT bằng SwingUtilities.invokeLater
 *   2. dùng layout manager (ở đây là FlowLayout), không dùng null layout
 *   3. sự kiện nút bấm xử lý bằng addActionListener + biểu thức lambda
 */
class Bai01HelloSwing {

    static JFrame frame;
    static JTextField txtTen;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // không đổi được Look and Feel thì dùng giao diện mặc định của Java
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 1 - Chào người dùng");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // FlowLayout: xếp các thành phần theo hàng ngang, cách nhau 10px ngang / 15px dọc
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        txtTen = new JTextField(20);
        JButton btnChao = new JButton("Hiển thị lời chào");

        panel.add(new JLabel("Nhập tên:"));
        panel.add(txtTen);
        panel.add(btnChao);
        frame.add(panel);

        // Xử lý sự kiện bằng lambda: bấm nút -> gọi hienThiLoiChao()
        btnChao.addActionListener(e -> hienThiLoiChao());
        // Gõ Enter trong ô nhập cũng coi như bấm nút
        txtTen.addActionListener(e -> hienThiLoiChao());

        frame.pack();                      // cửa sổ tự co theo kích thước các thành phần
        frame.setLocationRelativeTo(null);  // đưa cửa sổ ra giữa màn hình
        frame.setVisible(true);
    }

    static void hienThiLoiChao() {
        String ten = txtTen.getText().trim();

        // Trường hợp lỗi: chưa nhập tên -> nhắc rồi dừng lại, KHÔNG chào
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập tên!",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return;
        }

        JOptionPane.showMessageDialog(frame, "Xin chào, " + ten + "!",
                "Lời chào", JOptionPane.INFORMATION_MESSAGE);
    }
}
