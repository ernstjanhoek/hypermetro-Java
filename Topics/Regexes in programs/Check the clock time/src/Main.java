import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String regex = "^(2[0-3]|[01][0-9]):[0-5][0-9]$";

        String time = scanner.nextLine();
        System.out.println(time.matches(regex) ? "YES" : "NO");
    }
}