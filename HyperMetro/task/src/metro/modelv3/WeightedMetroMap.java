package metro.modelv3;

import metro.base.BaseEdge;
import metro.modelv2.MetroMap;
import java.util.Map;
import java.util.Set;

public class WeightedMetroMap<T extends WeightedMetroNode> extends MetroMap<T, BaseEdge<T>> {
    public WeightedMetroMap(Map<T, Set<BaseEdge<T>>> map, Set<String> lines) {
        super(map, lines);
    }
}
