package metro.controller;

import metro.file.Station;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
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
    public void output(String line) {
        List<Station> stationList = metroNodeMap.getStationsForLine(line);

        System.out.println("depot");
        stationList.forEach(System.out::println);
        System.out.println("depot");
    }

    @Override
    public void append(String line, String stationName) {
        this.metroNodeMap.addAtEnd(new MetroNode(stationName, line));
    }

    @Override
    public void remove(String line, String stationName) {
        this.metroNodeMap.remove(new MetroNode(stationName, line));
    }

    @Override
    public void addHead(String line, String stationName) {
        this.metroNodeMap.addAtTail(new MetroNode(stationName, line));
    }

    @Override
    public void connect(String line1, String stationName1, String line2, String stationName2) {
        MetroNode firstNode = new MetroNode(stationName1, line1);
        MetroNode otherNode = new MetroNode(stationName2, line2);

        this.metroNodeMap.connectNodes(firstNode, otherNode);
    }

    @Override
    public void route(String line1, String stationName1, String line2, String stationName2) {
        List<MetroNode> list = this.metroNodeMap.bfs(new MetroNode(stationName1, line1), new MetroNode(stationName2, line2));
        printRoute(list);
    }

    private void printRoute(List<MetroNode> list) {
        if (list.isEmpty()) {
            return;
        }
        MetroNode[] array = list.toArray(new MetroNode[0]);
        for (int i = 0; i < array.length-1; i++) {
            System.out.println(array[i].getName());
            if (array[i].getName().equals(array[i+1].getName())) {
                System.out.println("Transition to line " + array[i].getLine());
            }
        }
        System.out.println(array[array.length - 1].getName());
    }
}
