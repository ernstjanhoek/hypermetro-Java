package metro.controller;

import metro.file.Station;
import metro.modelv2.MetroLine;
import metro.modelv2.MetroMap;
import java.util.*;

public class MetroNetworkController implements CommandExecutor {
    boolean isRunning = true;
    private final MetroMap metroNodeMap;

    public MetroNetworkController(MetroMap metroNodeMap) {
        super();
        this.metroNodeMap = metroNodeMap;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void exit() {
        isRunning = false;
    }

    @Override
    public void output(String lineName) {
        MetroLine line = new MetroLine(lineName);

        List<Station> stationList = metroNodeMap.getStationsForLine(line);

        System.out.println("depot");
        stationList.forEach(System.out::println);
        System.out.println("depot");
    }

    @Override
    public void append(String lineName, String stationName) {
        this.metroNodeMap.addAtEnd(stationName, lineName);
    }

    @Override
    public void remove(String lineName, String stationName) {
        this.metroNodeMap.remove(stationName, lineName);
    }

    @Override
    public void addHead(String lineName, String stationName) {

    }

    @Override
    public void connect(String lineName1, String stationName1, String lineName2, String stationName2) {

    }
}
