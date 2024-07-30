package metro.modelv3;

import metro.modelv2.MetroNode;

public class WeightedMetroNode extends MetroNode {
    private final int time;
    public WeightedMetroNode(String name, String line, int time) {
        super(name, line);
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
