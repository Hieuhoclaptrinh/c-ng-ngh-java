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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

/**
 * Lab 3 - Bài 8: Quản lý sinh viên bằng JTable. (bài tự làm, không có gợi ý code)
 *
 * Yêu cầu:
 *   - form gồm: mã sinh viên, họ tên, điểm trung bình, nút Thêm, Sửa, Xóa, Làm mới
 *   - hiển thị danh sách bằng JTable
 *   - tự động xếp loại: >= 8.5 Giỏi, >= 7 Khá, >= 5 Trung bình, còn lại Yếu
 *   - tách tối thiểu 2 lớp; mở rộng: tách TableModel / Service để làm quen MVC
 *
 * Bài này tách thành 4 lớp theo mô hình MVC cơ bản:
 *   SinhVienLab3        - MODEL: dữ liệu một sinh viên và quy tắc xếp loại
 *   SinhVienService     - phần xử lý nghiệp vụ: thêm/sửa/xóa, kiểm tra trùng mã.
 *                         Lớp này KHÔNG biết gì về Swing, ném IllegalArgumentException
 *                         khi dữ liệu sai để tầng giao diện tự quyết định cách báo lỗi
 *   SinhVienTableModel  - cầu nối giữa danh sách và JTable (VIEW đọc dữ liệu qua đây)
 *   Bai08QuanLySinhVien - VIEW + CONTROLLER: dựng giao diện và nối sự kiện
 *
 * Ghi chú: lớp model đặt tên SinhVienLab3 vì chương 1 đã có một lớp SinhVien
 * ở cùng default package, để trùng tên sẽ không biên dịch chung được.
 */
class Bai08QuanLySinhVien {

    static final SinhVienService service = new SinhVienService();

    static JFrame frame;
    static SinhVienTableModel tableModel;
    static JTable table;
    static JTextField txtMaSV;
    static JTextField txtHoTen;
    static JTextField txtDiem;
    static JLabel lblThongKe;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 8 - Quản lý sinh viên");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        themDuLieuMau();

        frame.add(taoPanelNhap(), BorderLayout.NORTH);
        frame.add(taoBang(), BorderLayout.CENTER);

        lblThongKe = new JLabel();
        lblThongKe.setBorder(BorderFactory.createEmptyBorder(5, 12, 10, 12));
        frame.add(lblThongKe, BorderLayout.SOUTH);

        capNhatThongKe();

        frame.setSize(680, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static JPanel taoPanelNhap() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 12, 0, 12),
                BorderFactory.createTitledBorder("Thông tin sinh viên")));

        txtMaSV = new JTextField(10);
        txtHoTen = new JTextField(20);
        txtDiem = new JTextField(6);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        gbc.gridx = 0; panel.add(new JLabel("Mã SV:"), gbc);
        gbc.gridx = 1; panel.add(txtMaSV, gbc);
        gbc.gridx = 2; panel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 3; panel.add(txtHoTen, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; panel.add(new JLabel("Điểm TB:"), gbc);
        gbc.gridx = 1; panel.add(txtDiem, gbc);

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
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelNut, gbc);

        return panel;
    }

    static JScrollPane taoBang() {
        tableModel = new SinhVienTableModel(service.layDanhSach());
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);   // bấm tiêu đề cột để sắp xếp
        table.setRowHeight(24);

        // Chọn một dòng thì đổ dữ liệu dòng đó lên form để sửa
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;   // bỏ qua sự kiện trung gian khi đang kéo chuột
            SinhVienLab3 sv = sinhVienDangChon();
            if (sv != null) {
                txtMaSV.setText(sv.getMaSV());
                txtHoTen.setText(sv.getHoTen());
                txtDiem.setText(String.format(Locale.US, "%.1f", sv.getDiemTrungBinh()));
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        return scroll;
    }

    // ----- Các thao tác Thêm / Sửa / Xóa -----

    static void them() {
        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        Double diem = docDiem();
        if (diem == null) return;

        try {
            // Mọi quy tắc nghiệp vụ nằm trong service; giao diện chỉ hiển thị lỗi
            service.them(new SinhVienLab3(ma, ten, diem));
        } catch (IllegalArgumentException ex) {
            canhBao(ex.getMessage());
            return;
        }

        capNhatBang();
        lamMoiForm();
    }

    static void sua() {
        SinhVienLab3 sv = sinhVienDangChon();
        if (sv == null) {
            canhBao("Hãy chọn một sinh viên trong bảng để sửa.");
            return;
        }

        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        Double diem = docDiem();
        if (diem == null) return;

        try {
            service.sua(sv, ma, ten, diem);
        } catch (IllegalArgumentException ex) {
            canhBao(ex.getMessage());
            return;
        }

        capNhatBang();
    }

    static void xoa() {
        SinhVienLab3 sv = sinhVienDangChon();
        if (sv == null) {
            canhBao("Hãy chọn một sinh viên trong bảng để xóa.");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(frame,
                "Xóa sinh viên " + sv.getHoTen() + " (" + sv.getMaSV() + ")?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            service.xoa(sv);
            capNhatBang();
            lamMoiForm();
        }
    }

    // ----- Hỗ trợ -----

    /** Sinh viên ứng với dòng đang chọn, null nếu chưa chọn dòng nào. */
    static SinhVienLab3 sinhVienDangChon() {
        int dongXem = table.getSelectedRow();
        if (dongXem < 0) return null;
        // Bảng đang bật sắp xếp nên dòng thứ i trên màn hình chưa chắc là phần tử thứ i
        return tableModel.getSinhVienAt(table.convertRowIndexToModel(dongXem));
    }

    /** Đọc điểm trung bình từ ô nhập, trả về null (và báo lỗi) nếu không hợp lệ. */
    static Double docDiem() {
        String s = txtDiem.getText().trim().replace(',', '.');
        if (s.isEmpty()) {
            canhBao("Bạn chưa nhập điểm trung bình.");
            txtDiem.requestFocus();
            return null;
        }
        try {
            double d = Double.parseDouble(s);
            if (d < 0 || d > 10) {
                canhBao("Điểm trung bình phải nằm trong khoảng 0 - 10.");
                txtDiem.requestFocus();
                txtDiem.selectAll();
                return null;
            }
            return d;
        } catch (NumberFormatException ex) {
            canhBao("Điểm trung bình phải là số hợp lệ.");
            txtDiem.requestFocus();
            txtDiem.selectAll();
            return null;
        }
    }

    static void capNhatBang() {
        tableModel.capNhat();   // báo cho JTable vẽ lại toàn bộ dữ liệu
        capNhatThongKe();
    }

    static void capNhatThongKe() {
        List<SinhVienLab3> ds = service.layDanhSach();
        if (ds.isEmpty()) {
            lblThongKe.setText("Danh sách trống.");
            return;
        }
        double tong = 0;
        int gioi = 0, kha = 0, trungBinh = 0, yeu = 0;
        for (SinhVienLab3 sv : ds) {
            tong += sv.getDiemTrungBinh();
            switch (sv.xepLoai()) {
                case "Giỏi" -> gioi++;
                case "Khá" -> kha++;
                case "Trung bình" -> trungBinh++;
                default -> yeu++;
            }
        }
        lblThongKe.setText(String.format(Locale.US,
                "Sĩ số: %d   |   Điểm TB lớp: %.2f   |   Giỏi: %d   Khá: %d   Trung bình: %d   Yếu: %d",
                ds.size(), tong / ds.size(), gioi, kha, trungBinh, yeu));
    }

    static void lamMoiForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtDiem.setText("");
        table.clearSelection();
        txtMaSV.requestFocus();
    }

    static void canhBao(String thongBao) {
        JOptionPane.showMessageDialog(frame, thongBao, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    static void themDuLieuMau() {
        service.them(new SinhVienLab3("SV001", "Nguyễn Văn An", 8.7));
        service.them(new SinhVienLab3("SV002", "Trần Thị Bình", 7.2));
        service.them(new SinhVienLab3("SV003", "Lê Hoàng Cường", 5.4));
        service.them(new SinhVienLab3("SV004", "Phạm Thu Dung", 4.1));
    }
}

/**
 * MODEL: dữ liệu của một sinh viên.
 * Quy tắc xếp loại đặt ngay trong lớp này vì nó là thuộc tính của sinh viên,
 * không phải việc của giao diện.
 */
class SinhVienLab3 {

    private String maSV;
    private String hoTen;
    private double diemTrungBinh;

    SinhVienLab3(String maSV, String hoTen, double diemTrungBinh) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.diemTrungBinh = diemTrungBinh;
    }

    String getMaSV() {
        return maSV;
    }

    void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    String getHoTen() {
        return hoTen;
    }

    void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    double getDiemTrungBinh() {
        return diemTrungBinh;
    }

    void setDiemTrungBinh(double diemTrungBinh) {
        this.diemTrungBinh = diemTrungBinh;
    }

    /** >= 8.5 Giỏi, >= 7 Khá, >= 5 Trung bình, còn lại Yếu. */
    String xepLoai() {
        if (diemTrungBinh >= 8.5) return "Giỏi";
        if (diemTrungBinh >= 7.0) return "Khá";
        if (diemTrungBinh >= 5.0) return "Trung bình";
        return "Yếu";
    }
}

/**
 * Tầng nghiệp vụ: giữ danh sách sinh viên và các quy tắc thêm/sửa/xóa.
 * Lớp này không import gì của Swing - đó chính là ý nghĩa của việc "tách logic
 * ra khỏi giao diện": có thể đem sang ứng dụng web hay console dùng lại nguyên vẹn.
 */
class SinhVienService {

    private final List<SinhVienLab3> danhSach = new ArrayList<>();

    /** Trả về chính danh sách gốc để TableModel dùng chung, không phải bản sao. */
    List<SinhVienLab3> layDanhSach() {
        return danhSach;
    }

    void them(SinhVienLab3 sv) {
        kiemTra(sv.getMaSV(), sv.getHoTen(), null);
        danhSach.add(sv);
    }

    void sua(SinhVienLab3 sv, String maMoi, String tenMoi, double diemMoi) {
        kiemTra(maMoi, tenMoi, sv);
        sv.setMaSV(maMoi);
        sv.setHoTen(tenMoi);
        sv.setDiemTrungBinh(diemMoi);
    }

    void xoa(SinhVienLab3 sv) {
        danhSach.remove(sv);
    }

    SinhVienLab3 timTheoMa(String ma) {
        for (SinhVienLab3 sv : danhSach) {
            if (sv.getMaSV().equalsIgnoreCase(ma)) return sv;
        }
        return null;
    }

    /**
     * Kiểm tra mã và họ tên. Tham số boQua là sinh viên đang được sửa - chính nó thì
     * không tính là trùng mã với bản thân.
     */
    private void kiemTra(String ma, String ten, SinhVienLab3 boQua) {
        if (ma == null || ma.isBlank()) {
            throw new IllegalArgumentException("Mã sinh viên không được để trống.");
        }
        if (ten == null || ten.isBlank()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        SinhVienLab3 trung = timTheoMa(ma);
        if (trung != null && trung != boQua) {
            throw new IllegalArgumentException("Mã sinh viên \"" + ma + "\" đã tồn tại.");
        }
    }
}

/**
 * Cầu nối giữa danh sách sinh viên và JTable.
 * So với DefaultTableModel (phải tự nạp lại từng dòng), AbstractTableModel đọc thẳng
 * từ List nên dữ liệu trên bảng luôn khớp với dữ liệu gốc; cột "Xếp loại" được tính
 * ngay lúc vẽ nên sửa điểm là xếp loại tự đổi theo.
 */
class SinhVienTableModel extends AbstractTableModel {

    private static final String[] TEN_COT = {"Mã SV", "Họ tên", "Điểm TB", "Xếp loại"};

    private final List<SinhVienLab3> danhSach;

    SinhVienTableModel(List<SinhVienLab3> danhSach) {
        this.danhSach = danhSach;
    }

    @Override
    public int getRowCount() {
        return danhSach.size();
    }

    @Override
    public int getColumnCount() {
        return TEN_COT.length;
    }

    @Override
    public String getColumnName(int cot) {
        return TEN_COT[cot];
    }

    @Override
    public Object getValueAt(int dong, int cot) {
        SinhVienLab3 sv = danhSach.get(dong);
        return switch (cot) {
            case 0 -> sv.getMaSV();
            case 1 -> sv.getHoTen();
            case 2 -> String.format(Locale.US, "%.1f", sv.getDiemTrungBinh());
            case 3 -> sv.xepLoai();
            default -> "";
        };
    }

    @Override
    public boolean isCellEditable(int dong, int cot) {
        // Không cho sửa trực tiếp trên bảng: mọi thay đổi phải đi qua form để còn kiểm tra dữ liệu
        return false;
    }

    SinhVienLab3 getSinhVienAt(int dong) {
        return danhSach.get(dong);
    }

    /** Gọi sau khi danh sách gốc thay đổi để JTable vẽ lại. */
    void capNhat() {
        fireTableDataChanged();
    }
}
