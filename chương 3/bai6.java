import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Lab 3 - Bài 6: Form đăng nhập cơ bản. (bài tự làm, không có gợi ý code)
 *
 * Yêu cầu:
 *   - JFrame gồm: tài khoản, mật khẩu, vai trò người dùng và nút Đăng nhập
 *   - JTextField cho tài khoản, JPasswordField cho mật khẩu, JComboBox cho vai trò
 *   - JCheckBox "Hiển thị mật khẩu"
 *   - tài khoản kiểm thử: admin/123456 vai trò Admin; user/123456 vai trò User
 *   - đúng thì chào mừng, sai thì báo lỗi rõ ràng
 *
 * Điểm đáng học:
 *   - JPasswordField.getPassword() trả về char[] chứ không phải String, để có thể
 *     xóa sạch mật khẩu khỏi bộ nhớ sau khi dùng (String là bất biến, không xóa được)
 *   - JCheckBox bật/tắt ký tự che bằng setEchoChar: 0 là hiện chữ, '•' là che
 *   - GridBagLayout cho phép xếp form theo dòng/cột và cho một ô chiếm nhiều cột
 */
class Bai06LoginForm {

    /** Một tài khoản hợp lệ trong hệ thống. */
    static class TaiKhoan {
        final String ten;
        final String matKhau;
        final String vaiTro;

        TaiKhoan(String ten, String matKhau, String vaiTro) {
            this.ten = ten;
            this.matKhau = matKhau;
            this.vaiTro = vaiTro;
        }
    }

    /** Danh sách tài khoản kiểm thử theo quy ước của đề bài. */
    static final TaiKhoan[] DANH_SACH_TAI_KHOAN = {
            new TaiKhoan("admin", "123456", "Admin"),
            new TaiKhoan("user", "123456", "User")
    };

    static final int SO_LAN_THU_TOI_DA = 3;

    static JFrame frame;
    static JTextField txtTaiKhoan;
    static JPasswordField txtMatKhau;
    static JComboBox<String> cboVaiTro;
    static JCheckBox chkHienMatKhau;
    static JLabel lblTrangThai;
    static int soLanSai = 0;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 6 - Đăng nhập hệ thống");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel lblTieuDe = new JLabel("ĐĂNG NHẬP HỆ THỐNG", JLabel.CENTER);
        lblTieuDe.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        lblTrangThai = new JLabel(" ", JLabel.CENTER);
        lblTrangThai.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        frame.add(lblTieuDe, BorderLayout.NORTH);
        frame.add(taoPanelForm(), BorderLayout.CENTER);
        frame.add(lblTrangThai, BorderLayout.SOUTH);

        frame.setSize(430, 290);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static JPanel taoPanelForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        txtTaiKhoan = new JTextField(16);
        txtMatKhau = new JPasswordField(16);
        cboVaiTro = new JComboBox<>(new String[]{"Admin", "User"});
        chkHienMatKhau = new JCheckBox("Hiển thị mật khẩu");

        // Mặc định JPasswordField đã che chữ; lưu lại ký tự che để bật lại khi bỏ tick
        final char kyTuChe = txtMatKhau.getEchoChar();
        chkHienMatKhau.addActionListener(e ->
                txtMatKhau.setEchoChar(chkHienMatKhau.isSelected() ? (char) 0 : kyTuChe));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        gbc.gridx = 0; panel.add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1; panel.add(txtTaiKhoan, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; panel.add(txtMatKhau, gbc);

        gbc.gridy = 2;
        gbc.gridx = 1; panel.add(chkHienMatKhau, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0; panel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1; panel.add(cboVaiTro, gbc);

        JButton btnDangNhap = new JButton("Đăng nhập");
        JButton btnXoa = new JButton("Xóa");
        btnDangNhap.addActionListener(e -> dangNhap());
        btnXoa.addActionListener(e -> xoaForm());
        // Gõ Enter ở ô tài khoản hoặc mật khẩu cũng đăng nhập
        txtTaiKhoan.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());

        JPanel panelNut = new JPanel();
        panelNut.add(btnDangNhap);
        panelNut.add(btnXoa);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelNut, gbc);

        return panel;
    }

    static void dangNhap() {
        String ten = txtTaiKhoan.getText().trim();
        char[] matKhauNhap = txtMatKhau.getPassword();
        String vaiTro = (String) cboVaiTro.getSelectedItem();

        try {
            if (ten.isEmpty()) {
                canhBao("Bạn chưa nhập tên tài khoản.");
                txtTaiKhoan.requestFocus();
                return;
            }
            if (matKhauNhap.length == 0) {
                canhBao("Bạn chưa nhập mật khẩu.");
                txtMatKhau.requestFocus();
                return;
            }

            TaiKhoan tk = timTaiKhoan(ten);
            // Báo lỗi tách bạch từng nguyên nhân cho đúng yêu cầu "báo lỗi rõ ràng"
            if (tk == null) {
                dangNhapThatBai("Tài khoản \"" + ten + "\" không tồn tại.");
                return;
            }
            if (!Arrays.equals(matKhauNhap, tk.matKhau.toCharArray())) {
                dangNhapThatBai("Mật khẩu không đúng.");
                return;
            }
            if (!tk.vaiTro.equals(vaiTro)) {
                dangNhapThatBai("Tài khoản \"" + ten + "\" không có vai trò " + vaiTro
                        + ".\nVai trò đúng là: " + tk.vaiTro + ".");
                return;
            }

            soLanSai = 0;
            lblTrangThai.setText("Đăng nhập thành công với vai trò " + tk.vaiTro + ".");
            JOptionPane.showMessageDialog(frame,
                    "Chào mừng " + tk.ten + "!\nBạn đang đăng nhập với vai trò " + tk.vaiTro + ".",
                    "Đăng nhập thành công", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            // Xóa sạch mật khẩu vừa đọc khỏi bộ nhớ, dù đăng nhập đúng hay sai
            Arrays.fill(matKhauNhap, '\0');
        }
    }

    static void dangNhapThatBai(String lyDo) {
        soLanSai++;
        txtMatKhau.setText("");
        txtMatKhau.requestFocus();

        if (soLanSai >= SO_LAN_THU_TOI_DA) {
            JOptionPane.showMessageDialog(frame,
                    lyDo + "\n\nBạn đã nhập sai " + soLanSai + " lần. Chương trình sẽ đóng.",
                    "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            return;
        }

        lblTrangThai.setText("Sai lần " + soLanSai + "/" + SO_LAN_THU_TOI_DA + ".");
        JOptionPane.showMessageDialog(frame,
                lyDo + "\n\nCòn " + (SO_LAN_THU_TOI_DA - soLanSai) + " lần thử.",
                "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
    }

    static TaiKhoan timTaiKhoan(String ten) {
        for (TaiKhoan tk : DANH_SACH_TAI_KHOAN) {
            if (tk.ten.equalsIgnoreCase(ten)) return tk;
        }
        return null;
    }

    static void xoaForm() {
        txtTaiKhoan.setText("");
        txtMatKhau.setText("");
        cboVaiTro.setSelectedIndex(0);
        lblTrangThai.setText(" ");
        txtTaiKhoan.requestFocus();
    }

    static void canhBao(String thongBao) {
        JOptionPane.showMessageDialog(frame, thongBao, "Thiếu thông tin",
                JOptionPane.WARNING_MESSAGE);
    }
}
