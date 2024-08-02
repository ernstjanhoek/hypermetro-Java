package metro.controller;

import metro.algorithms.BFS;
import metro.base.BaseEdge;
import metro.file.Station;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
import java.util.*;

import static metro.controller.ControllerUtils.printLine;
import static metro.controller.ControllerUtils.printRoute;

public class MetroNetworkController implements CommandExecutor {
    boolean isRunning = true;
    private final MetroMap<MetroNode, BaseEdge<MetroNode>> metroNodeMap;

    public MetroNetworkController(MetroMap<MetroNode, BaseEdge<MetroNode>> metroNodeMap) {
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
        MetroNode node = new MetroNode(stationName, line);
        BaseEdge<MetroNode> edge = new BaseEdge<>(metroNodeMap.getEndNode(line).get(), node);
        this.metroNodeMap.addAtEnd(node, edge);
    }

    @Override
    public void remove(String line, String stationName) {
        this.metroNodeMap.remove(new MetroNode(stationName, line));
    }

    @Override
    public void addHead(String line, String stationName) {
        MetroNode node = new MetroNode(stationName, line);
        BaseEdge<MetroNode> edge = new BaseEdge<>(node, metroNodeMap.getStartNode(line).get());
        this.metroNodeMap.addAtTail(new MetroNode(stationName, line), edge);
    }

    @Override
    public void connect(String line1, String stationName1, String line2, String stationName2) {
        MetroNode firstNode = new MetroNode(stationName1, line1);
        MetroNode otherNode = new MetroNode(stationName2, line2);

        this.metroNodeMap.connectNodes(firstNode, otherNode);
    }

    @Override
    public void add(String line, String stationName, int time) {
        addHead(line, stationName);
    }

    @Override
    public void route(String line1, String stationName1, String line2, String stationName2) {
        MetroNode startNode = new MetroNode(stationName1, line1);
        MetroNode endNode = new MetroNode(stationName2, line2);
        BFS<MetroNode, MetroMap<MetroNode, BaseEdge<MetroNode>>, BaseEdge<MetroNode>> bfs = new BFS<>(metroNodeMap, startNode, endNode);
        List<MetroNode> list = bfs.search();
        printRoute(list);
    }

    @Override
    public void fastestRoute(String line1, String stationName1, String line2, String stationName2) {
        route(line1, stationName1, line2, stationName2);
    }

}
