package metro.modelv3;

import metro.modelv2.MetroEdge;
import metro.modelv2.MetroMap;

import java.util.Map;
import java.util.Set;

public class WeightedMetroMap extends MetroMap<WeightedMetroNode> {
    public WeightedMetroMap(Map<WeightedMetroNode, Set<MetroEdge<WeightedMetroNode>>> map, Set<String> lines) {
        super(map, lines);
    }
}
