import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int philosophersNO, dishesToEat;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of philosophers: ");
        philosophersNO = scanner.nextInt();
        System.out.println("Enter number of dishes: ");
        dishesToEat = scanner.nextInt();
        System.out.println("\n");

        Philosopher[] philosophers = new Philosopher[philosophersNO];
        Object[] forks = new Object[philosophersNO];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % forks.length];

            boolean isLeftHanded = (i % 2 == 0);  // some philosophers are left-handed
            philosophers[i] = new Philosopher((i + 1), dishesToEat, isLeftHanded, rightFork, leftFork);

            Thread thread = new Thread(philosophers[i]);
            thread.start();
        }
    }
}
