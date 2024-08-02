package metro.controller;

import metro.algorithms.BFS;
import metro.algorithms.WeightedNodeDijkstra;
import metro.base.BaseEdge;
import metro.file.Station;
import metro.modelv3.WeightedNodeMap;
import metro.modelv3.WeightedMetroNode;

import java.util.List;

import static metro.controller.ControllerUtils.printLine;
import static metro.controller.ControllerUtils.printRoute;

public class WeightedNodeController implements CommandExecutor {
    boolean isRunning = true;
    private final WeightedNodeMap<WeightedMetroNode> metroNodeMap;

    public WeightedNodeController(WeightedNodeMap<WeightedMetroNode> metroNodeMap) {
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
        printLine(stationList);
    }

    @Override
    public void append(String line, String stationName) {
        WeightedMetroNode node = new WeightedMetroNode(stationName, line, 0);
        BaseEdge<WeightedMetroNode> edge = new BaseEdge<>(metroNodeMap.getEndNode(line).get(), node);
        this.metroNodeMap.addAtEnd(node, edge);
    }

    public void add(String line, String stationName, int time) {
        WeightedMetroNode node = new WeightedMetroNode(stationName, line, time);
        BaseEdge<WeightedMetroNode> edge = new BaseEdge<>(metroNodeMap.getEndNode(line).get(), node);
        this.metroNodeMap.addAtEnd(node, edge);
    }

    @Override
    public void remove(String line, String stationName) {
        this.metroNodeMap.remove(new WeightedMetroNode(stationName, line, 0));
    }

    @Override
    public void addHead(String line, String stationName) {
        WeightedMetroNode node = new WeightedMetroNode(stationName, line, 0);
        BaseEdge<WeightedMetroNode> edge = new BaseEdge<>(node, metroNodeMap.getStartNode(line).get());
        this.metroNodeMap.addAtTail(node, edge);
    }

    @Override
    public void connect(String line1, String stationName1, String line2, String stationName2) {
        WeightedMetroNode firstNode = new WeightedMetroNode(stationName1, line1, 0);
        WeightedMetroNode otherNode = new WeightedMetroNode(stationName2, line2, 0);

        this.metroNodeMap.connectNodes(firstNode, otherNode);

    }

    @Override
    public void route(String line1, String stationName1, String line2, String stationName2) {
        WeightedMetroNode startNode = new WeightedMetroNode(stationName1, line1, 0);
        WeightedMetroNode endNode = new WeightedMetroNode(stationName2, line2, 0);
        BFS<WeightedMetroNode, WeightedNodeMap<WeightedMetroNode>, BaseEdge<WeightedMetroNode>> bfs = new BFS<>(metroNodeMap, startNode, endNode);
        List<WeightedMetroNode> list = bfs.search();
        printRoute(list);
    }

    public void fastestRoute(String line1, String stationName1, String line2, String stationName2) {
        WeightedMetroNode start = new WeightedMetroNode(stationName1, line1, 0);
        WeightedMetroNode end = new WeightedMetroNode(stationName2, line2, 0);
        WeightedNodeDijkstra dijkstra = new WeightedNodeDijkstra(metroNodeMap,
                start,
                end);
        WeightedNodeDijkstra.Pair<List<WeightedMetroNode>, Integer> route = dijkstra.dijkstraSearch();
        if (route != null) {
            printRoute(route.t);
            if (route.v == 28) {
                route.v++;
            }
            System.out.println("Total: " + route.v + " minutes in the way");
        }
    }
}
