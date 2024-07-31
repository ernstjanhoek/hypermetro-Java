package metro.file;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InputReader {

    public static Map<String, Map<String, Station>> readFromFile(String path) throws IOException {
        Gson gson = new Gson();
        // Initialize your reader here with the path to your JSON file
        Reader reader = Files.newBufferedReader(Paths.get(path));

        // Define the TypeToken for the complex map structure
        Type type = new TypeToken<Map<String, Map<String, Station>>>() {}.getType();

        // Deserialize the JSON into the SubwaySystem class
        return gson.fromJson(reader, type);
    }

    public static Map<String, Map<String, WeightedStation>> readWeightedFromFile(String path) throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        Type type = new TypeToken<Map<String, Map<String, WeightedStation>>>() {}.getType();
        return gson.fromJson(reader, type);
    }

    public static Map<String, List<ConnectedStation>> readConnectedStationsFromFile(String path) throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        Type type = new TypeToken<Map<String, List<ConnectedStation>>>() {}.getType();
        return gson.fromJson(reader, type);
    }

    public static List<String> readCommand() {
        Scanner scanner = new Scanner(System.in);
        List<String> arguments = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        String input = scanner.nextLine();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Quoted string without the quotes
                arguments.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Unquoted word
                arguments.add(matcher.group(2));
            }
        }
        return arguments;
    }
}
