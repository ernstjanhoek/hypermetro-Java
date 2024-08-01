package metro.modelv4;

import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
import metro.modelv3.WeightedMetroEdge;
import java.util.Map;
import java.util.Set;

public class WeightedEdgesMetroMap<T extends MetroNode> extends MetroMap<T, WeightedMetroEdge<T>> {
    public WeightedEdgesMetroMap(Map<T, Set<WeightedMetroEdge<T>>> map, Set<String> lines) {
        super(map, lines);
    }
}
