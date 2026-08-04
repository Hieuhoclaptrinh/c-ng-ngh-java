import java.util.Scanner;

public class Bai4TamGiac {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập 3 số a, b, c: ");
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        double c = sc.nextDouble();

        if (!isTriangle(a, b, c)) {
            System.out.println("Không phải là 3 cạnh của tam giác");
        } else {
            System.out.println("Là 3 cạnh của tam giác");
            System.out.println("Loại tam giác: " + classify(a, b, c));
        }
        sc.close();
    }

    static boolean isTriangle(double a, double b, double c) {
        return a > 0 && b > 0 && c > 0
                && a + b > c && a + c > b && b + c > a;
    }

    static String classify(double a, double b, double c) {
        boolean equilateral = (a == b && b == c);
        boolean isosceles = (a == b || b == c || a == c);
        boolean right = isRightTriangle(a, b, c);

        if (equilateral) return "Tam giác đều";
        if (isosceles && right) return "Tam giác vuông cân";
        if (isosceles) return "Tam giác cân";
        if (right) return "Tam giác vuông";
        return "Tam giác thường";
    }

    static boolean isRightTriangle(double a, double b, double c) {
        double[] sides = {a, b, c};
        java.util.Arrays.sort(sides);
        double eps = 1e-9;
        return Math.abs(sides[0]*sides[0] + sides[1]*sides[1] - sides[2]*sides[2]) < eps;
    }
}
