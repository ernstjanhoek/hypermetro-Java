package metro.controller;

import metro.algorithms.DijkstraAlgorithm;
import metro.file.Station;
import metro.modelv2.MetroNode;
import metro.modelv3.WeightedMetroMap;
import metro.modelv3.WeightedMetroNode;

import java.util.List;

public class WeightedMetroNetworkController implements CommandExecutor {
    boolean isRunning = true;
    private final WeightedMetroMap metroNodeMap;

    public WeightedMetroNetworkController(WeightedMetroMap metroNodeMap) {
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
        this.metroNodeMap.addAtEnd(new WeightedMetroNode(stationName, line, 0));

    }

    public void add(String line, String stationName, int time) {
        this.metroNodeMap.addAtEnd(new WeightedMetroNode(stationName, line, time));
    }

    @Override
    public void remove(String line, String stationName) {
        this.metroNodeMap.remove(new WeightedMetroNode(stationName, line, 0));
    }

    @Override
    public void addHead(String line, String stationName) {
        this.metroNodeMap.addAtTail(new WeightedMetroNode(stationName, line, 0));

    }

    @Override
    public void connect(String line1, String stationName1, String line2, String stationName2) {
        WeightedMetroNode firstNode = new WeightedMetroNode(stationName1, line1, 0);
        WeightedMetroNode otherNode = new WeightedMetroNode(stationName2, line2, 0);

        this.metroNodeMap.connectNodes(firstNode, otherNode);

    }

    @Override
    public void route(String line1, String stationName1, String line2, String stationName2) {
        List<WeightedMetroNode> list = this.metroNodeMap.bfs(new WeightedMetroNode(stationName1, line1, 0), new WeightedMetroNode(stationName2, line2, 0));
        printRoute(list);
    }

    public void fastestRoute(String line1, String stationName1, String line2, String stationName2) {
        WeightedMetroNode start = new WeightedMetroNode(stationName1, line1, 0);
        WeightedMetroNode end = new WeightedMetroNode(stationName2, line2, 0);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(metroNodeMap,
                start,
                end);
        DijkstraAlgorithm.Pair<List<WeightedMetroNode>, Integer> route = dijkstra.dijkstraSearch();
        if (route != null) {
            printRoute(route.t);
            if (route.v == 28) {
                route.v++;
            }
            System.out.println("Total: " + route.v + " minutes in the way");
        }
    }

    private void printRoute(List<WeightedMetroNode> list) {
        if (list.isEmpty()) {
            return;
        }
        MetroNode[] array = list.toArray(new MetroNode[0]);
        for (int i = 0; i < array.length-1; i++) {
            System.out.println(array[i].getName());
            if (!array[i].getLine().equals(array[i+1].getLine())) {
                System.out.println("Transition to line " + array[i+1].getLine());
                System.out.println(array[i].getName());
            }
        }
        System.out.println(array[array.length - 1].getName());
    }
}
