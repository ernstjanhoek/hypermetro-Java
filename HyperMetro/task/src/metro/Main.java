package metro;

import metro.controller.CommandExecutor;
import metro.controller.SubwayLineController;
import metro.file.InputReader;
import metro.file.Station;
import metro.mapper.MetroNetworkMapper;
import metro.controller.MetroNetworkController;
import metro.mapper.SubwayNetworkMapper;
import metro.modelv2.MetroLine;
import metro.modelv2.MetroMap;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Station>> inputMap = InputReader.readFromFile(args[0]);

        SubwayNetworkMapper mapper = new SubwayNetworkMapper();
        SubwayLineController subwayLineController = mapper.buildAndConnect(inputMap);

        MetroNetworkMapper metroNetworkMapper = new MetroNetworkMapper();
        MetroMap metroMap = metroNetworkMapper.buildAndConnect(inputMap);

        CommandExecutor executor =  new MetroNetworkController(metroMap);

        while (executor.isRunning()) {
            List<String> commandArgs = InputReader.readCommand();
            Commands command = Commands.fromString(commandArgs.get(0));
            switch (command) {
                case EXIT -> executor.exit();
                case OUTPUT -> {
                    Optional<MetroLine> lineOption = metroMap.mapArgToMetroLine(commandArgs.get(1));
                    lineOption.ifPresent(executor::output);
                }
                case APPEND -> {
                    Optional<MetroLine> lineOption = metroMap.mapArgToMetroLine(commandArgs.get(1));
                    lineOption.ifPresent(metroLine -> executor.append(metroLine, commandArgs.get(2)));
                }
                case REMOVE -> {
                    Optional<MetroLine> lineOption = metroMap.mapArgToMetroLine(commandArgs.get(1));
                    lineOption.ifPresent(metroLine -> executor.remove(metroLine, commandArgs.get(2)));
                }
                case ADD_HEAD -> {
                    Optional<MetroLine> lineOption = metroMap.mapArgToMetroLine(commandArgs.get(1));
                    lineOption.ifPresent(metroLine -> executor.addHead(metroLine, commandArgs.get(2)));
                }
                case CONNECT -> {
                    Optional<MetroLine> lineOption1 = metroMap.mapArgToMetroLine(commandArgs.get(1));
                    Optional<MetroLine> lineOption2 = metroMap.mapArgToMetroLine(commandArgs.get(3));
                    if (lineOption1.isPresent() && lineOption2.isPresent()) {
                        executor.connect(lineOption1.get(), commandArgs.get(2), lineOption2.get(), commandArgs.get(4));
                    }
                }
            }
        }
    }

    public enum Commands {
        APPEND, ADD_HEAD, REMOVE, OUTPUT, EXIT, CONNECT;

        public static Commands fromString(String command) throws IllegalStateException {
            return switch (command) {
                case "/output" -> OUTPUT;
                case "/exit" -> EXIT;
                case "/add-head" -> ADD_HEAD;
                case "/append" -> APPEND;
                case "/connect" -> CONNECT;
                case "/remove" -> REMOVE;
                default -> throw new IllegalStateException("Invalid command");
            };
        }
    }
}
