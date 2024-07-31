package metro.modelv3;

import metro.base.BaseGraph;
import metro.modelv2.MetroEdge;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;

import java.util.Map;
import java.util.Set;

public class WeightedMetroMap<T extends MetroNode> extends BaseGraph<T, WeightedMetroEdge<T>> {
    public WeightedMetroMap(Map<T, Set<WeightedMetroEdge<T>>> map, Set<String> lines) {
        super(map);
    }
}
