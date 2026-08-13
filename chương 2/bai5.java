import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * Bài 5: Quản lý sinh viên bằng JTable - bài tổng hợp của chương 2.
 *
 * Đây chính là bài 6 của chương 1 (nhập điểm, tính tổng kết, xếp loại)
 * nhưng chuyển từ console sang giao diện, có thêm sửa và xóa.
 *
 * Điểm đáng học:
 *   - danh sách (List) là dữ liệu gốc, JTable chỉ là nơi HIỂN THỊ dữ liệu đó
 *   - DefaultTableModel giữ các dòng của bảng; sửa dữ liệu xong phải vẽ lại bảng
 *   - bảng có sắp xếp (row sorter) nên phải đổi chỉ số dòng xem -> chỉ số dòng gốc
 *     bằng convertRowIndexToModel, nếu không sẽ sửa/xóa sai sinh viên
 */
class Bai5QuanLySinhVien {

    /** Một sinh viên. Công thức điểm giống bài 6 chương 1. */
    static class SinhVien {
        String maSV;
        String hoTen;
        double chuyenCan;
        double giuaKy;
        double cuoiKy;

        SinhVien(String maSV, String hoTen, double chuyenCan, double giuaKy, double cuoiKy) {
            this.maSV = maSV;
            this.hoTen = hoTen;
            this.chuyenCan = chuyenCan;
            this.giuaKy = giuaKy;
            this.cuoiKy = cuoiKy;
        }

        double diemTongKet() {
            return chuyenCan * 0.1 + giuaKy * 0.3 + cuoiKy * 0.6;
        }

        String xepLoai() {
            double d = diemTongKet();
            if (d >= 8.5) return "A";
            if (d >= 7.0) return "B";
            if (d >= 5.5) return "C";
            if (d >= 4.0) return "D";
            return "F";
        }
    }

    static final List<SinhVien> danhSach = new ArrayList<>();

    static JFrame frame;
    static DefaultTableModel model;
    static JTable table;
    static JTextField txtMaSV;
    static JTextField txtHoTen;
    static JTextField txtChuyenCan;
    static JTextField txtGiuaKy;
    static JTextField txtCuoiKy;
    static JLabel lblThongKe;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 5 - Quản lý sinh viên");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setJMenuBar(taoMenu());

        frame.add(taoPanelNhap(), BorderLayout.NORTH);
        frame.add(taoBang(), BorderLayout.CENTER);

        lblThongKe = new JLabel();
        lblThongKe.setBorder(BorderFactory.createEmptyBorder(5, 12, 10, 12));
        frame.add(lblThongKe, BorderLayout.SOUTH);

        themDuLieuMau();
        veLaiBang();

        frame.setSize(760, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static JMenuBar taoMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuHeThong = new JMenu("Hệ thống");
        JMenuItem miXoaHet = new JMenuItem("Xóa toàn bộ danh sách");
        JMenuItem miThoat = new JMenuItem("Thoát");
        miXoaHet.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(frame,
                    "Xóa toàn bộ danh sách sinh viên?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (chon == JOptionPane.YES_OPTION) {
                danhSach.clear();
                veLaiBang();
                lamMoiForm();
            }
        });
        miThoat.addActionListener(e -> System.exit(0));
        menuHeThong.add(miXoaHet);
        menuHeThong.addSeparator();
        menuHeThong.add(miThoat);

        JMenu menuTroGiup = new JMenu("Trợ giúp");
        JMenuItem miGioiThieu = new JMenuItem("Giới thiệu");
        miGioiThieu.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Quản lý sinh viên - Chương 2 Java Swing\n\n"
                        + "Điểm tổng kết = Chuyên cần x 10% + Giữa kỳ x 30% + Cuối kỳ x 60%\n"
                        + "Xếp loại: A >= 8.5, B >= 7.0, C >= 5.5, D >= 4.0, còn lại F",
                "Giới thiệu", JOptionPane.INFORMATION_MESSAGE));
        menuTroGiup.add(miGioiThieu);

        menuBar.add(menuHeThong);
        menuBar.add(menuTroGiup);
        return menuBar;
    }

    static JPanel taoPanelNhap() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        txtMaSV = new JTextField(10);
        txtHoTen = new JTextField(18);
        txtChuyenCan = new JTextField(5);
        txtGiuaKy = new JTextField(5);
        txtCuoiKy = new JTextField(5);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Dòng 1: mã SV, họ tên
        gbc.gridy = 0;
        gbc.gridx = 0; panel.add(new JLabel("Mã SV:"), gbc);
        gbc.gridx = 1; panel.add(txtMaSV, gbc);
        gbc.gridx = 2; panel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 3; panel.add(txtHoTen, gbc);
        gbc.gridwidth = 1;

        // Dòng 2: ba loại điểm
        gbc.gridy = 1;
        gbc.gridx = 0; panel.add(new JLabel("Chuyên cần:"), gbc);
        gbc.gridx = 1; panel.add(txtChuyenCan, gbc);
        gbc.gridx = 2; panel.add(new JLabel("Giữa kỳ:"), gbc);
        gbc.gridx = 3; panel.add(txtGiuaKy, gbc);
        gbc.gridx = 4; panel.add(new JLabel("Cuối kỳ:"), gbc);
        gbc.gridx = 5; panel.add(txtCuoiKy, gbc);

        // Dòng 3: các nút
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        JPanel panelNut = new JPanel();
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelNut, gbc);

        return panel;
    }

    static JScrollPane taoBang() {
        String[] cot = {"Mã SV", "Họ tên", "Chuyên cần", "Giữa kỳ", "Cuối kỳ", "Tổng kết", "Xếp loại"};

        // Ghi đè isCellEditable để không cho sửa trực tiếp trên bảng
        // (mọi thay đổi phải đi qua form ở trên để còn kiểm tra dữ liệu)
        model = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true); // bấm vào tiêu đề cột để sắp xếp
        table.setRowHeight(24);

        // Chọn một dòng thì đổ dữ liệu dòng đó lên form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return; // bỏ qua sự kiện trung gian khi đang kéo chuột
            SinhVien sv = sinhVienDangChon();
            if (sv != null) {
                txtMaSV.setText(sv.maSV);
                txtHoTen.setText(sv.hoTen);
                txtChuyenCan.setText(soGon(sv.chuyenCan));
                txtGiuaKy.setText(soGon(sv.giuaKy));
                txtCuoiKy.setText(soGon(sv.cuoiKy));
            }
        });

        return new JScrollPane(table);
    }

    // ----- Các thao tác thêm / sửa / xóa -----

    static void them() {
        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            canhBao("Mã SV và họ tên không được để trống.");
            return;
        }
        if (timTheoMa(ma) != null) {
            canhBao("Mã SV \"" + ma + "\" đã tồn tại.");
            return;
        }

        Double cc = docDiem(txtChuyenCan, "chuyên cần");
        Double gk = docDiem(txtGiuaKy, "giữa kỳ");
        Double ck = docDiem(txtCuoiKy, "cuối kỳ");
        if (cc == null || gk == null || ck == null) return;

        danhSach.add(new SinhVien(ma, ten, cc, gk, ck));
        veLaiBang();
        lamMoiForm();
    }

    static void sua() {
        SinhVien sv = sinhVienDangChon();
        if (sv == null) {
            canhBao("Hãy chọn một sinh viên trong bảng để sửa.");
            return;
        }

        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            canhBao("Mã SV và họ tên không được để trống.");
            return;
        }
        SinhVien trung = timTheoMa(ma);
        if (trung != null && trung != sv) { // đổi sang mã của một sinh viên khác
            canhBao("Mã SV \"" + ma + "\" đã thuộc về sinh viên khác.");
            return;
        }

        Double cc = docDiem(txtChuyenCan, "chuyên cần");
        Double gk = docDiem(txtGiuaKy, "giữa kỳ");
        Double ck = docDiem(txtCuoiKy, "cuối kỳ");
        if (cc == null || gk == null || ck == null) return;

        sv.maSV = ma;
        sv.hoTen = ten;
        sv.chuyenCan = cc;
        sv.giuaKy = gk;
        sv.cuoiKy = ck;
        veLaiBang();
    }

    static void xoa() {
        SinhVien sv = sinhVienDangChon();
        if (sv == null) {
            canhBao("Hãy chọn một sinh viên trong bảng để xóa.");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(frame,
                "Xóa sinh viên " + sv.hoTen + " (" + sv.maSV + ")?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            danhSach.remove(sv);
            veLaiBang();
            lamMoiForm();
        }
    }

    // ----- Hỗ trợ -----

    /** Sinh viên tương ứng dòng đang chọn, hoặc null nếu không chọn dòng nào. */
    static SinhVien sinhVienDangChon() {
        int dongXem = table.getSelectedRow();
        if (dongXem < 0) return null;
        // Bảng đang bật sắp xếp: dòng thứ 0 trên màn hình chưa chắc là phần tử thứ 0
        int dongGoc = table.convertRowIndexToModel(dongXem);
        return danhSach.get(dongGoc);
    }

    static SinhVien timTheoMa(String ma) {
        for (SinhVien sv : danhSach) {
            if (sv.maSV.equalsIgnoreCase(ma)) return sv;
        }
        return null;
    }

    /** Đọc điểm 0-10 từ ô nhập. Trả về null (và báo lỗi) nếu không hợp lệ. */
    static Double docDiem(JTextField txt, String tenDiem) {
        String s = txt.getText().trim().replace(',', '.'); // cho phép gõ cả dấu phẩy
        try {
            double d = Double.parseDouble(s);
            if (d < 0 || d > 10) {
                canhBao("Điểm " + tenDiem + " phải nằm trong khoảng 0 - 10.");
                txt.requestFocus();
                return null;
            }
            return d;
        } catch (NumberFormatException e) {
            canhBao("Điểm " + tenDiem + " không phải là số.");
            txt.requestFocus();
            return null;
        }
    }

    /** Xóa hết dòng cũ rồi nạp lại từ danh sách gốc, đồng thời cập nhật thống kê. */
    static void veLaiBang() {
        model.setRowCount(0);
        for (SinhVien sv : danhSach) {
            model.addRow(new Object[]{
                    sv.maSV,
                    sv.hoTen,
                    soGon(sv.chuyenCan),
                    soGon(sv.giuaKy),
                    soGon(sv.cuoiKy),
                    String.format(Locale.US, "%.2f", sv.diemTongKet()),
                    sv.xepLoai()
            });
        }
        capNhatThongKe();
    }

    static void capNhatThongKe() {
        if (danhSach.isEmpty()) {
            lblThongKe.setText("Danh sách trống.");
            return;
        }
        double tong = 0;
        int soDat = 0;
        for (SinhVien sv : danhSach) {
            tong += sv.diemTongKet();
            if (sv.diemTongKet() >= 4.0) soDat++;
        }
        lblThongKe.setText(String.format(Locale.US,
                "Tổng số sinh viên: %d   |   Điểm trung bình: %.2f   |   Đạt: %d   |   Không đạt: %d",
                danhSach.size(), tong / danhSach.size(), soDat, danhSach.size() - soDat));
    }

    static void lamMoiForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtChuyenCan.setText("");
        txtGiuaKy.setText("");
        txtCuoiKy.setText("");
        table.clearSelection();
        txtMaSV.requestFocus();
    }

    static void canhBao(String thongBao) {
        JOptionPane.showMessageDialog(frame, thongBao, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    /** 8.0 -> "8", 7.5 -> "7.5" cho gọn bảng. */
    static String soGon(double d) {
        if (d == Math.rint(d)) return String.valueOf((long) d);
        return String.format(Locale.US, "%.1f", d);
    }

    static void themDuLieuMau() {
        danhSach.add(new SinhVien("SV001", "Nguyễn Văn An", 10, 8.5, 9));
        danhSach.add(new SinhVien("SV002", "Trần Thị Bình", 8, 7, 6.5));
        danhSach.add(new SinhVien("SV003", "Lê Hoàng Cường", 9, 5, 2.5)); // ví dụ không đạt
    }
}
