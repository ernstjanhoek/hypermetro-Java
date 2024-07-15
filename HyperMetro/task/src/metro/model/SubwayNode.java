package metro.model;

import java.util.ArrayList;
import java.util.List;

public class SubwayNode {
    private final String stationName;
    private SubwayNode previous;
    private SubwayNode next;

    private List<Edge> edges = new ArrayList<>();

    public SubwayNode(String name) {
        this.stationName = name;
    }

    public SubwayNode(String name, SubwayNode previous, SubwayNode next, List<Edge> connectionNodes) {
        this.stationName = name;
        this.previous = previous;
        this.next = next;
        this.edges = connectionNodes;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public SubwayNode next() {
        return this.next;
    }

    public SubwayNode previous() {
        return this.previous;
    }

    public void setPrevious(SubwayNode previous) {
        this.previous = previous;
    }

    public void setNext(SubwayNode next) {
        this.next = next;
    }

    public String getStationName() {
        return stationName;
    }

    public SubwayNode getPrevious() {
        return previous;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    public SubwayNode getNext() {
        return next;
    }

    public boolean hasNext() {
        return next != null;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stationName);
        for (Edge edge : edges) {
            stringBuilder.append(" - ").append(edge);
        }
        return stringBuilder.toString();
    }
}
