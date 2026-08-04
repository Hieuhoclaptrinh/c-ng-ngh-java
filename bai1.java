import java.util.Scanner;

public class Bai1TongSoChan {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập n: ");
        int n = sc.nextInt();

        if (n % 2 != 0) n = n - 1; // nếu n lẻ, lấy số chẵn lớn nhất nhỏ hơn n

        long s = 0;
        for (int i = 2; i <= n; i += 2) {
            s += i;
        }

        System.out.println("Tổng các số chẵn đến " + n + " là: " + s);
        sc.close();
    }
}
