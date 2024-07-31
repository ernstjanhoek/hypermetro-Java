package metro.modelv3;

import metro.modelv2.MetroNode;

public class WeightedMetroNode extends MetroNode implements Comparable<WeightedMetroNode> {
    private final int time;
    public WeightedMetroNode(String name, String line, int time) {
        super(name, line);
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(WeightedMetroNode weightedMetroNode) {
        return Integer.compare(this.time, weightedMetroNode.time);
    }
}
