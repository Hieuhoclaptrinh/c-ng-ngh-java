import java.util.Scanner;

class SinhVien {
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

class bai6 {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n = nhapSoLuong("Nhập số sinh viên: ");
        SinhVien[] ds = new SinhVien[n];

        for (int i = 0; i < n; i++) {
            System.out.println("--- Sinh viên thứ " + (i + 1) + " ---");
            System.out.print("Mã sinh viên: ");
            String ma = sc.nextLine().trim();
            System.out.print("Họ tên: ");
            String ten = sc.nextLine().trim();
            double cc = nhapDiem("Điểm chuyên cần (0-10): ");
            double gk = nhapDiem("Điểm giữa kỳ (0-10): ");
            double ck = nhapDiem("Điểm cuối kỳ (0-10): ");

            ds[i] = new SinhVien(ma, ten, cc, gk, ck);
        }

        System.out.println();
        System.out.println("===== BẢNG KẾT QUẢ =====");
        System.out.printf("%-10s %-20s %-10s %-8s%n", "Mã SV", "Họ tên", "Tổng kết", "Xếp loại");
        for (SinhVien sv : ds) {
            System.out.printf("%-10s %-20s %-10.2f %-8s%n",
                    sv.maSV, sv.hoTen, sv.diemTongKet(), sv.xepLoai());
        }
    }

    // Nhập điểm, lặp lại đến khi hợp lệ (0 - 10)
    static double nhapDiem(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String s = sc.nextLine().trim();
            try {
                double d = Double.parseDouble(s);
                if (d < 0 || d > 10) {
                    System.out.println("Điểm " + s + " không hợp lệ (phải từ 0 đến 10). Nhập lại!");
                    continue;
                }
                return d;
            } catch (NumberFormatException e) {
                System.out.println("Giá trị không phải là số. Nhập lại!");
            }
        }
    }

    static int nhapSoLuong(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String s = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n <= 0) {
                    System.out.println("Số sinh viên phải lớn hơn 0. Nhập lại!");
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                System.out.println("Giá trị không phải là số nguyên. Nhập lại!");
            }
        }
    }
}
