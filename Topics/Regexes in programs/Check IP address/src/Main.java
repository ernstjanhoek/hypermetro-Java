import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String regexIPAdress ="^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        System.out.println(scanner.nextLine().matches(regexIPAdress) ? "YES" : "NO");
    }
}