package metro.controller;

import metro.algorithms.BFS;
import metro.base.BaseEdge;
import metro.file.Station;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
import metro.modelv3.WeightedMetroNode;
import metro.modelv4.WeightedEdgesMetroMap;
import metro.modelv4.WeightedMetroEdge;

import java.util.List;

import static metro.controller.ControllerUtils.printLine;
import static metro.controller.ControllerUtils.printRoute;

public class WeightedEdgesController implements CommandExecutor {
    boolean isRunning = true;
    private final WeightedEdgesMetroMap<MetroNode> metroNodeMap;

    public WeightedEdgesController(WeightedEdgesMetroMap<MetroNode> metroNodeMap) {
        this.metroNodeMap = metroNodeMap;
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
        WeightedMetroEdge<MetroNode> edge = new WeightedMetroEdge<>(metroNodeMap.getEndNode(line).get(), node, 0);

        this.metroNodeMap.addAtEnd(node, edge);
    }

    @Override
    public void remove(String line, String stationName) {
        this.metroNodeMap.remove(new MetroNode(stationName, line));
    }

    @Override
    public void addHead(String line, String stationName) {
        MetroNode node = new MetroNode(stationName, line);
        WeightedMetroEdge<MetroNode> edge = new WeightedMetroEdge<>(node, metroNodeMap.getStartNode(line).get(), 0);

        this.metroNodeMap.addAtTail(node, edge);
    }

    @Override
    public void connect(String line1, String stationName1, String line2, String stationName2) {
        MetroNode firstNode = new MetroNode(stationName1, line1);
        MetroNode otherNode = new MetroNode(stationName2, line2);

        this.metroNodeMap.connectNodes(firstNode, otherNode);
    }

    @Override
    public void add(String line, String stationName, int time) {
        WeightedMetroNode node = new WeightedMetroNode(stationName, line, 0);
        WeightedMetroEdge<MetroNode> edge = new WeightedMetroEdge<>(metroNodeMap.getEndNode(line).get(), node, time);

        this.metroNodeMap.addAtEnd(node, edge);
    }

    @Override
    public void route(String line1, String stationName1, String line2, String stationName2) {
        MetroNode startNode = new MetroNode(stationName1, line1);
        MetroNode endNode = new MetroNode(stationName2, line2);
        BFS<MetroNode, MetroMap<MetroNode, WeightedMetroEdge<MetroNode>>, WeightedMetroEdge<MetroNode>> bfs = new BFS<>(metroNodeMap, startNode, endNode);
        List<MetroNode> list = bfs.search();
        printRoute(list);
    }

    @Override
    public void fastestRoute(String line1, String stationName1, String line2, String stationName2) {
        route(line1, stationName1, line2, stationName2);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
