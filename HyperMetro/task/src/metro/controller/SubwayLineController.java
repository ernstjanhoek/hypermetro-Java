package metro.controller;

import metro.model.Edge;
import metro.model.SubwayLine;
import metro.model.SubwayNode;

import java.util.HashMap;
import java.util.Map;

public class SubwayLineController implements CommandExecutor {
    boolean isRunning = true;
    Map<String, SubwayLine> subwayLines;

    public SubwayLineController() {
        this.subwayLines = new HashMap<>();
    }

    public Map<String, SubwayLine> getSubwayLines() {
        return subwayLines;
    }

    @Override
    public void exit() {
        isRunning = false;
    }

    @Override
    public void output(String lineName) {
        subwayLines.get(lineName).printSubwayLine();
    }

    @Override
    public void append(String lineName, String stationName) {
        subwayLines.get(lineName).addAtTail(new SubwayNode(stationName));
    }

    @Override
    public void remove(String lineName, String stationName) {
        subwayLines.get(lineName).remove(stationName);
    }

    @Override
    public void addHead(String lineName, String stationName) {
        subwayLines.get(lineName).addAtHead(new SubwayNode(stationName));
    }

    @Override
    public void connect(String lineName1, String stationName1, String lineName2, String stationName2) {
        SubwayLine line1 = subwayLines.get(lineName1);
        SubwayNode node1 = line1.find(stationName1);
        SubwayLine line2 = subwayLines.get(lineName2);
        SubwayNode node2 = line2.find(stationName2);
        node1.addEdge(new Edge(line2, node2));
        node2.addEdge(new Edge(line1, node1));
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
