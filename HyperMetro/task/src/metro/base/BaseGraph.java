package metro.base;

import metro.algorithms.BreadthFirstSearch;

import java.util.*;
import java.util.stream.Collectors;

public class BaseGraph<T extends BaseNode<?>, U extends BaseEdge<T>> implements BreadthFirstSearch<T> {
    Map<T, Set<U>> baseEdgeMap;

    @Override
    public List<T> bfs(T startNode, T targetNode) {
        Optional<T> startOptional = getNode(startNode);
        Optional<T> targetOptional = getNode(targetNode);
        if (startOptional.isPresent() && targetOptional.isPresent()) {
            T start = startOptional.get();
            T target = targetOptional.get();

            Queue<T> queue = new LinkedList<>();
            Set<T> visited = new HashSet<>();
            Map<T, T> parentMap = new HashMap<>();

            queue.add(start);
            visited.add(start);
            parentMap.put(start, null);

            while (!queue.isEmpty()) {
                T current = queue.poll();
                if (current.equals(target)) {
                    return reconstructPath(parentMap, target);
                }
                Set<T> neighbours = getNeighbours(current);
                for (T u : neighbours) {
                    if (!visited.contains(u)) {
                        visited.add(u);
                        queue.add(u);
                        parentMap.put(u, current);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<T> reconstructPath(Map<T, T> parentMap, T target) {
        List<T> path = new ArrayList<>();
        for (T at = target; at != null; at = parentMap.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    @Override
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
}
