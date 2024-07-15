import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();
        boolean hasValidSize = inputString.matches("^.{12,}$");
        boolean containsUppercase = inputString.matches("^.*[A-Z].*$");
        boolean containsLowercase = inputString.matches("^.*[a-z].*$");
        boolean containsNumber = inputString.matches("^.*[0-9].*$");
        String output = hasValidSize && containsUppercase && containsLowercase && containsNumber ? "YES" : "NO";
        System.out.println(output);
    }
}