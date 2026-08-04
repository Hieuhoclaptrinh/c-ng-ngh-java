import java.util.Scanner;

public class Bai5Fibonacci {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập n: ");
        int n = sc.nextInt();

        long a = 0, b = 1;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            sb.append(a);
            if (i < n - 1) sb.append(" ");
            long next = a + b;
            a = b;
            b = next;
        }

        System.out.println(sb.toString());
        sc.close();
    }
}
