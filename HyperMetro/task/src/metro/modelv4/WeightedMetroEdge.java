package metro.modelv4;

import metro.base.BaseEdge;
import metro.modelv2.MetroNode;

public class WeightedMetroEdge<T extends MetroNode> extends BaseEdge<T> {
    private final int time;

    public WeightedMetroEdge(T origin, T destination, int time) {
        super(origin, destination);
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
