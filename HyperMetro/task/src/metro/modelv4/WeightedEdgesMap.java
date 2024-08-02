package metro.modelv4;

import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;

import java.util.Map;
import java.util.Set;

public class WeightedEdgesMap<T extends MetroNode> extends MetroMap<T, WeightedMetroEdge<T>> {
    public WeightedEdgesMap(Map<T, Set<WeightedMetroEdge<T>>> map, Set<String> lines) {
        super(map, lines);
    }
}
