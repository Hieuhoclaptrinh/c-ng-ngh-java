import java.util.Scanner;

public class Bai2TongNghichDao {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập n: ");
        int n = sc.nextInt();

        double s = 0;
        for (int i = 1; i <= n; i++) {
            s += 1.0 / i;
        }

        System.out.printf("Tổng S = %.4f%n", s);
        sc.close();
    }
}
