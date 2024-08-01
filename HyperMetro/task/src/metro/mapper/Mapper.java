package metro.mapper;

import metro.base.BaseEdge;
import metro.base.BaseGraph;
import metro.base.BaseNode;
import metro.file.Station;
import java.util.Map;

public interface Mapper<V extends BaseNode<?>, W extends BaseEdge<V>, T extends BaseGraph<V, W>, U extends Station> {
    T buildAndConnect(Map<String, Map<String, U>> inputMap);
}
