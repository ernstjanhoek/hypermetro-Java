package metro;

import metro.controller.CommandExecutor;
import metro.controller.SubwayLineController;
import metro.file.InputReader;
import metro.file.Station;
import metro.mapper.MetroNetworkMapper;
import metro.controller.MetroNetworkController;
import metro.mapper.SubwayNetworkMapper;
import metro.modelv2.MetroMap;

import java.io.*;
import java.util.List;
import java.util.Map;

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
