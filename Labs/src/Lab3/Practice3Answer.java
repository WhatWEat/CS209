package Lab3;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;

public class Practice3Answer {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        printFunction();
        int function = sc.nextInt();
        Predicate<Integer> filter = getFilter(function);
        while (function != 0) {
            printInput();
            int array_size = sc.nextInt();
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < array_size; i++) {
                int tmp = sc.nextInt();
                if (filter.test(tmp)) {
                    list.add(tmp);
                }
            }
            System.out.println("Filter results:\n");
            System.out.println(list);
            printFunction();
            function = sc.nextInt();
            filter = getFilter(function);
        }

    }

    public static boolean judgePrime(int n) {
        if (n == 2) {
            return true;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static Predicate<Integer> getFilter(int function) {
        Predicate<Integer> filter = null;
        switch (function) {
            case 1:
                filter = n -> n % 2 == 0;
                break;
            case 2:
                filter = n -> n % 2 == 1;
                break;
            case 3:
                filter = Practice3Answer::judgePrime;
                break;
            case 4:
                filter = n -> (judgePrime(n) & n > 5);
                break;
            default:
                break;
        }
        return filter;
    }

    public static void printInput() {
        System.out.println("Input size of the list:\n" +
            "Input elements of the list:\n");
    }

    public static void printFunction() {
        System.out.println("Please input the function No:\n" +
            "1 - Get even numbers\n" +
            "2 - Get odd numbers\n" +
            "3 - Get prime numbers\n" +
            "4 - Get prime numbers that are bigger than 5\n" +
            "0 - Quit\n");
    }
}
