package metro.algorithms;

import metro.base.BaseEdge;
import metro.base.BaseGraph;
import metro.base.BaseNode;

import java.util.*;

public class BFS<T extends BaseNode<?>, U extends BaseGraph<T, S>, S extends BaseEdge<T>> {
    U map;
    T start;
    T target;

    public BFS(U map, T startNode, T targetNode) {
        this.map = map;

        Optional<T> startOptional = map.getNode(startNode);
        Optional<T> targetOptional = map.getNode(targetNode);
        if (startOptional.isPresent() && targetOptional.isPresent()) {
            start = startOptional.get();
            target = targetOptional.get();
        } else {
            throw new IllegalArgumentException("Start and end nodes must be present");
        }
    }

    public List<T> search() {
        Queue<T> queue = new LinkedList<>();
        Set<T> visited = new HashSet<>();
        Map<T, T> parentMap = new HashMap<>();

        queue.add(start);
        visited.add(start);
        parentMap.put(start, null);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (current.equals(target)) {
                return Utils.reconstructPath(parentMap, target);
            }
            Set<T> neighbours = map.getNeighbours(current);
            for (T u : neighbours) {
                if (!visited.contains(u)) {
                    visited.add(u);
                    queue.add(u);
                    parentMap.put(u, current);
                }
            }
        }
        return Collections.emptyList();
    }

}
