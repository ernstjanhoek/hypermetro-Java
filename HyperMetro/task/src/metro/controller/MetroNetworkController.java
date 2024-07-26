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
    public void output(MetroLine line) {
        List<Station> stationList = metroNodeMap.getStationsForLine(line);

        System.out.println("depot");
        stationList.forEach(System.out::println);
        System.out.println("depot");
    }

    @Override
    public void append(MetroLine line, String stationName) {
        this.metroNodeMap.addAtEnd(stationName, line);
    }

    @Override
    public void remove(MetroLine line, String stationName) {
        this.metroNodeMap.remove(stationName, line);
    }

    @Override
    public void addHead(MetroLine line, String stationName) {

    }

    @Override
    public void connect(MetroLine line1, String stationName1, MetroLine line2, String stationName2) {

    }
}
