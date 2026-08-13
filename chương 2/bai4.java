import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Bài 4: Form đăng ký - dùng gần hết các thành phần nhập liệu của Swing
 * và kiểm tra dữ liệu (validate) trước khi chấp nhận.
 *
 * Thành phần: JTextField, JPasswordField, JRadioButton + ButtonGroup,
 *             JComboBox, JCheckBox, JTextArea + JScrollPane.
 * Layout:     GridBagLayout - linh hoạt nhất, xếp theo dòng/cột và cho phép
 *             một ô chiếm nhiều cột (gridwidth).
 */
class Bai4FormDangKy {

    static JTextField txtHoTen;
    static JTextField txtEmail;
    static JPasswordField txtMatKhau;
    static JRadioButton rdNam;
    static JRadioButton rdNu;
    static JComboBox<String> cboNganh;
    static JCheckBox chkDocSach;
    static JCheckBox chkTheThao;
    static JCheckBox chkAmNhac;
    static JCheckBox chkDongY;
    static JTextArea txtGhiChu;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        JFrame frame = new JFrame("Bài 4 - Form đăng ký");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtHoTen = new JTextField(20);
        txtEmail = new JTextField(20);
        txtMatKhau = new JPasswordField(20);

        // ButtonGroup: đảm bảo chỉ chọn được MỘT radio button trong nhóm
        rdNam = new JRadioButton("Nam", true);
        rdNu = new JRadioButton("Nữ");
        ButtonGroup nhomGioiTinh = new ButtonGroup();
        nhomGioiTinh.add(rdNam);
        nhomGioiTinh.add(rdNu);
        JPanel panelGioiTinh = new JPanel();
        panelGioiTinh.add(rdNam);
        panelGioiTinh.add(rdNu);

        cboNganh = new JComboBox<>(new String[]{
                "Công nghệ thông tin", "Kế toán", "Cơ khí", "Ngôn ngữ Anh"
        });

        // JCheckBox: chọn được nhiều cái cùng lúc
        chkDocSach = new JCheckBox("Đọc sách");
        chkTheThao = new JCheckBox("Thể thao");
        chkAmNhac = new JCheckBox("Âm nhạc");
        JPanel panelSoThich = new JPanel();
        panelSoThich.add(chkDocSach);
        panelSoThich.add(chkTheThao);
        panelSoThich.add(chkAmNhac);

        txtGhiChu = new JTextArea(4, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        // JTextArea không tự có thanh cuộn, phải bọc trong JScrollPane
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);

        chkDongY = new JCheckBox("Tôi đồng ý với điều khoản sử dụng");

        int dong = 0;
        themDong(form, dong++, "Họ tên:", txtHoTen);
        themDong(form, dong++, "Email:", txtEmail);
        themDong(form, dong++, "Mật khẩu:", txtMatKhau);
        themDong(form, dong++, "Giới tính:", panelGioiTinh);
        themDong(form, dong++, "Ngành học:", cboNganh);
        themDong(form, dong++, "Sở thích:", panelSoThich);
        themDong(form, dong++, "Ghi chú:", scrollGhiChu);
        themDong(form, dong++, "", chkDongY);

        JButton btnDangKy = new JButton("Đăng ký");
        JButton btnLamMoi = new JButton("Làm mới");
        JPanel panelNut = new JPanel();
        panelNut.add(btnDangKy);
        panelNut.add(btnLamMoi);

        // Hàng nút: chiếm cả 2 cột
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        form.add(panelNut, gbc);

        btnDangKy.addActionListener(e -> dangKy(frame));
        btnLamMoi.addActionListener(e -> lamMoi());

        frame.add(form);
        frame.pack(); // tự tính kích thước vừa đủ cho nội dung
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** Thêm một dòng "nhãn : thành phần" vào GridBagLayout. */
    static void themDong(JPanel form, int dong, String nhan, java.awt.Component thanhPhan) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.anchor = GridBagConstraints.EAST; // nhãn căn phải cho thẳng cột
        form.add(new JLabel(nhan), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(thanhPhan, gbc);
    }

    static void dangKy(JFrame frame) {
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        // --- Kiểm tra dữ liệu ---
        if (hoTen.isEmpty()) {
            canhBao(frame, "Vui lòng nhập họ tên.", txtHoTen);
            return;
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            canhBao(frame, "Email không đúng định dạng (ví dụ: an@gmail.com).", txtEmail);
            return;
        }
        if (matKhau.length() < 6) {
            canhBao(frame, "Mật khẩu phải có ít nhất 6 ký tự.", txtMatKhau);
            return;
        }
        if (!chkDongY.isSelected()) {
            canhBao(frame, "Bạn phải đồng ý với điều khoản sử dụng.", chkDongY);
            return;
        }

        // --- Gom sở thích đã chọn ---
        StringBuilder soThich = new StringBuilder();
        if (chkDocSach.isSelected()) soThich.append("Đọc sách, ");
        if (chkTheThao.isSelected()) soThich.append("Thể thao, ");
        if (chkAmNhac.isSelected()) soThich.append("Âm nhạc, ");
        String dsSoThich = soThich.isEmpty()
                ? "(không chọn)"
                : soThich.substring(0, soThich.length() - 2); // bỏ ", " cuối

        String ghiChu = txtGhiChu.getText().trim();

        String thongTin = "ĐĂNG KÝ THÀNH CÔNG\n\n"
                + "Họ tên: " + hoTen + "\n"
                + "Email: " + email + "\n"
                + "Mật khẩu: " + "*".repeat(matKhau.length()) + "\n"
                + "Giới tính: " + (rdNam.isSelected() ? "Nam" : "Nữ") + "\n"
                + "Ngành học: " + cboNganh.getSelectedItem() + "\n"
                + "Sở thích: " + dsSoThich + "\n"
                + "Ghi chú: " + (ghiChu.isEmpty() ? "(trống)" : ghiChu);

        JOptionPane.showMessageDialog(frame, thongTin, "Kết quả", JOptionPane.INFORMATION_MESSAGE);
    }

    static void canhBao(JFrame frame, String thongBao, java.awt.Component canFocus) {
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        canFocus.requestFocus();
    }

    static void lamMoi() {
        txtHoTen.setText("");
        txtEmail.setText("");
        txtMatKhau.setText("");
        rdNam.setSelected(true);
        cboNganh.setSelectedIndex(0);
        chkDocSach.setSelected(false);
        chkTheThao.setSelected(false);
        chkAmNhac.setSelected(false);
        chkDongY.setSelected(false);
        txtGhiChu.setText("");
        txtHoTen.requestFocus();
    }
}
