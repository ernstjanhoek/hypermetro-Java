package metro;

import metro.controller.CommandExecutor;
import metro.controller.WeightedMetroNetworkController;
import metro.file.ConnectedStation;
import metro.file.InputReader;
import metro.file.Station;
import metro.file.WeightedStation;
import metro.mapper.MetroNetworkMapper;
import metro.controller.MetroNetworkController;
import metro.mapper.WeightedMetroMapper;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
import metro.modelv3.WeightedMetroMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            Map<String, List<ConnectedStation>> inputMap = InputReader.readConnectedStationsFromFile(args[0]);

            MetroMap<MetroNode> metroMap = null;

            System.exit(0);
            WeightedMetroNetworkController executor = new WeightedMetroNetworkController(null);


            while (executor.isRunning()) {
                try {
                    List<String> commandArgs = InputReader.readCommand();
                    Commands command = Commands.fromString(commandArgs.get(0));
                    switch (command) {
                        case EXIT -> executor.exit();
                        case OUTPUT -> {
                            Optional<String> lineOption = metroMap.findMetroLine(commandArgs.get(1));
                            lineOption.ifPresent(executor::output);
                        }

                        case APPEND -> {
                            Optional<String> lineOption = metroMap.findMetroLine(commandArgs.get(1));
                            lineOption.ifPresent(metroLine -> executor.append(metroLine, commandArgs.get(2)));
                        }

                        case REMOVE -> {
                            Optional<String> lineOption = metroMap.findMetroLine(commandArgs.get(1));
                            lineOption.ifPresent(metroLine -> executor.remove(metroLine, commandArgs.get(2)));
                        }

                        case ADD_HEAD -> {
                            Optional<String> lineOption = metroMap.findMetroLine(commandArgs.get(1));
                            lineOption.ifPresent(metroLine -> executor.addHead(metroLine, commandArgs.get(2)));
                        }

                        case CONNECT -> {
                            Optional<String> lineOption1 = metroMap.findMetroLine(commandArgs.get(1));
                            Optional<String> lineOption2 = metroMap.findMetroLine(commandArgs.get(3));
                            if (lineOption1.isPresent() && lineOption2.isPresent()) {
                                executor.connect(lineOption1.get(), commandArgs.get(2), lineOption2.get(), commandArgs.get(4));
                            }
                        }

                        case ROUTE -> {
                            Optional<String> lineOption1 = metroMap.findMetroLine(commandArgs.get(1));
                            Optional<String> lineOption2 = metroMap.findMetroLine(commandArgs.get(3));
                            if (lineOption1.isPresent() && lineOption2.isPresent()) {
                                executor.route(lineOption1.get(), commandArgs.get(2), lineOption2.get(), commandArgs.get(4));
                            }
                        }

                        case FASTEST_ROUTE -> {
                            Optional<String> lineOption1 = metroMap.findMetroLine(commandArgs.get(1));
                            Optional<String> lineOption2 = metroMap.findMetroLine(commandArgs.get(3));
                            if (lineOption1.isPresent() && lineOption2.isPresent()) {
                                executor.fastestRoute(lineOption1.get(), commandArgs.get(2), lineOption2.get(), commandArgs.get(4));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Commands {
        APPEND, ADD_HEAD, REMOVE, OUTPUT, EXIT, CONNECT, ROUTE, FASTEST_ROUTE;

        public static Commands fromString(String command) throws IllegalStateException {
            return switch (command) {
                case "/output" -> OUTPUT;
                case "/exit" -> EXIT;
                case "/add-head" -> ADD_HEAD;
                case "/append" -> APPEND;
                case "/connect" -> CONNECT;
                case "/remove" -> REMOVE;
                case "/route" -> ROUTE;
                case "/fastest-route" -> FASTEST_ROUTE;
                default -> throw new IllegalStateException("Invalid command");
            };
        }
    }
}
