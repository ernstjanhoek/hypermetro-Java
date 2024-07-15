import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();
        HashMap<String, Integer> programming = new HashMap<>();

        Pattern pattern = Pattern.compile("program(mers|ming|s|)", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.start() + " " + matcher.group());
        }
    }
}