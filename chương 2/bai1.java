import java.awt.BorderLayout;
import java.awt.Font;
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
 * Bài 1: Cửa sổ Swing đầu tiên.
 *
 * Ba thứ cần nhớ trong mọi chương trình Swing:
 *   1. Giao diện phải được tạo trên EDT (Event Dispatch Thread) -> SwingUtilities.invokeLater
 *   2. JFrame là cửa sổ, các thành phần được thêm vào bên trong nó
 *   3. Nút bấm chỉ hoạt động khi ta gắn ActionListener cho nó
 */
class Bai1HelloSwing {

    public static void main(String[] args) {
        // Dùng Look and Feel của hệ điều hành cho giao diện trông "tự nhiên" hơn
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // không đổi được thì dùng giao diện mặc định của Java, không sao cả
        }

        // Mọi thao tác với giao diện đều chạy trên EDT
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        JFrame frame = new JFrame("Bài 1 - Hello Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // đóng cửa sổ là kết thúc chương trình
        frame.setLayout(new BorderLayout(10, 10));

        // --- Phần giữa: dòng chữ lớn ---
        JLabel lblKetQua = new JLabel("Nhập tên của bạn rồi bấm Chào", JLabel.CENTER);
        lblKetQua.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblKetQua.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Phần dưới: ô nhập + 2 nút ---
        JTextField txtTen = new JTextField(15);
        JButton btnChao = new JButton("Chào");
        JButton btnXoa = new JButton("Xóa");

        JPanel panelDuoi = new JPanel(); // JPanel mặc định dùng FlowLayout: xếp ngang, giữa
        panelDuoi.add(new JLabel("Họ tên:"));
        panelDuoi.add(txtTen);
        panelDuoi.add(btnChao);
        panelDuoi.add(btnXoa);

        frame.add(lblKetQua, BorderLayout.CENTER);
        frame.add(panelDuoi, BorderLayout.SOUTH);

        // --- Xử lý sự kiện ---
        btnChao.addActionListener(e -> {
            String ten = txtTen.getText().trim();
            if (ten.isEmpty()) {
                // Hộp thoại cảnh báo có sẵn của Swing
                JOptionPane.showMessageDialog(frame, "Bạn chưa nhập tên!",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                txtTen.requestFocus();
                return;
            }
            lblKetQua.setText("Xin chào, " + ten + "!");
        });

        btnXoa.addActionListener(e -> {
            txtTen.setText("");
            lblKetQua.setText("Nhập tên của bạn rồi bấm Chào");
            txtTen.requestFocus();
        });

        // Enter trong ô nhập cũng coi như bấm nút Chào
        txtTen.addActionListener(e -> btnChao.doClick());

        frame.setSize(420, 200);
        frame.setLocationRelativeTo(null); // đưa cửa sổ ra giữa màn hình
        frame.setVisible(true);
    }
}
