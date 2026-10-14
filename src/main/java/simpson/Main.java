package simpson;

import java.util.Locale;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        if (args.length == 2 || args.length == 3) {
            double x = Double.parseDouble(args[0]);
            int dof = Integer.parseInt(args[1]);
            double lower = args.length == 3 ? Double.parseDouble(args[2]) : 0;
            System.out.printf("p = %.5f%n", Simpson.tIntegral(lower, x, dof));
            return;
        }

        double[][] cases = {{1.1, 9, 0.35006}, {1.1812, 10, 0.36757}, {2.750, 30, 0.49500}};
        System.out.println("   x      dof   esperado   actual");
        for (double[] c : cases) {
            double p = Simpson.tIntegral(c[0], (int) c[1]);
            System.out.printf("%-7.4f %-5d %-10.5f %.5f%n", c[0], (int) c[1], c[2], p);
        }

        Scanner in = new Scanner(System.in);
        System.out.print("\nx (0 para salir): ");
        while (in.hasNextDouble()) {
            double x = in.nextDouble();
            if (x == 0) {
                break;
            }
            System.out.print("dof: ");
            int dof = in.nextInt();
            System.out.print("intervalo (límite inferior): ");
            double lower = in.nextDouble();
            System.out.printf("p = %.5f%n", Simpson.tIntegral(lower, x, dof));
            System.out.print("\nx (0 para salir): ");
        }
    }
}
// prueba de CI
