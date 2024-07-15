package metro;

import metro.controller.CommandExecutor;
import metro.file.InputReader;
import metro.file.Station;
import metro.mapper.MetroNetworkMapper;
import metro.mapper.SubwayNetworkMapper;
import metro.controller.SubwayLineController;
import metro.controller.MetroNetworkController;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Station>> metroMap = InputReader.readFromFile(args[0]);

        // SubwayNetworkMapper mapper = new SubwayNetworkMapper();
        // SubwayLineController subwayLineController = mapper.buildAndConnect(metroMap);

        MetroNetworkMapper metroNetworkMapper = new MetroNetworkMapper();
        MetroNetworkController metroNetworkController = metroNetworkMapper.buildAndConnect(metroMap);

        CommandExecutor executor = metroNetworkController;

        while (executor.isRunning()) {
            List<String> commandArgs = InputReader.readCommand();
            Commands command = Commands.fromString(commandArgs.get(0));
            switch (command) {
                case EXIT -> executor.exit();
                case OUTPUT -> executor.output(commandArgs.get(1));
                case APPEND -> executor.append(commandArgs.get(1), commandArgs.get(2));
                case REMOVE -> executor.remove(commandArgs.get(1), commandArgs.get(2));
                case ADD_HEAD -> executor.addHead(commandArgs.get(1), commandArgs.get(2));
                case CONNECT -> executor.connect(commandArgs.get(1), commandArgs.get(2), commandArgs.get(3), commandArgs.get(4));
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
