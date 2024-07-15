package metro.model;

public class Edge {
    private final SubwayLine line;
    private final SubwayNode node;

    public Edge(SubwayLine line, SubwayNode node) {
        this.line = line;
        this.node = node;
    }

    public String getNodeName() {
        return node.getStationName();
    }

    public SubwayLine getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", node.getStationName(), line.getLineName());
    }
}
