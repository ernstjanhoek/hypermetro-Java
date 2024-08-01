package metro.base;

import java.util.*;
import java.util.stream.Collectors;

public class BaseGraph<T extends BaseNode<?>, U extends BaseEdge<T>> {
    Map<T, Set<U>> baseEdgeMap;

    public Set<T> getNeighbours(T current) {
        Set<T> neighboursFromCurrent = getEdgesByDestination(current).stream()
                .map(BaseEdge::getOrigin)
                .collect(Collectors.toSet());

        Set<T> neighboursToCurrent = getEdgesByOrigin(current).stream()
                .map(BaseEdge::getDestination)
                .collect(Collectors.toSet());

        neighboursToCurrent.addAll(neighboursFromCurrent);
        return neighboursToCurrent;
    }

    public BaseGraph() {
        this.baseEdgeMap = new HashMap<>();
    }

    public BaseGraph(Map<T, Set<U>> baseEdgeMap) {
        this.baseEdgeMap = baseEdgeMap;
    }

    public boolean containsNode(T node) {
        return baseEdgeMap.containsKey(node);
    }

    public Optional<T> getNode(T node) {
        return baseEdgeMap.keySet().stream().filter(n -> n.equals(node)).findFirst();
    }

    public Map<T, Set<U>> getGraph() {
        return baseEdgeMap;
    }

    public boolean addNode(T node) {
        return baseEdgeMap.putIfAbsent(node, new HashSet<>()) == null;
    }

    public boolean removeNode(T node) {
        return baseEdgeMap.remove(node) != null;
    }

    public boolean addEdge(T source, T target, U edge) {
        if (baseEdgeMap.containsKey(source) && baseEdgeMap.containsKey(target)) {
            return baseEdgeMap.get(source).add(edge) && baseEdgeMap.get(target).add(edge);
        }
        return false;
    }

    public boolean removeEdge(T source, T target, U edge) {
        if (baseEdgeMap.containsKey(source) && baseEdgeMap.containsKey(target)) {
            return baseEdgeMap.get(source).remove(edge) && baseEdgeMap.get(target).remove(edge);
        }
        return false;
    }

    public Set<U> getEdgesByNode(T node) {
        return baseEdgeMap.getOrDefault(node, new HashSet<>());
    }

    public Set<U> getEdgesByOrigin(T source) {
        return baseEdgeMap.get(source).stream().filter(e -> e.getOrigin().equals(source)).collect(Collectors.toSet());
    }

    public Set<U> getEdgesByDestination(T target) {
        return baseEdgeMap.get(target).stream().filter(e -> e.getDestination().equals(target)).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        Set<BaseEdge<T>> edges = baseEdgeMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        String string = edges.stream().map(e -> "\"" + e.getOrigin() + "\" -- \"" + e.getDestination() + "\""
        ).collect(Collectors.joining("\n"));
        return "graph {\n" + string + "\n}";
    }
}
