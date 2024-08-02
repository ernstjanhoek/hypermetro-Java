package metro.algorithms;

import metro.modelv3.WeightedMetroMap;
import metro.modelv3.WeightedMetroNode;

import java.util.*;

public class WeightedNodeDijkstra {
    WeightedMetroMap<WeightedMetroNode> map;
    WeightedMetroNode startNode;
    WeightedMetroNode endNode;
    static final Integer TRANSFER_TIME = 5;

    public WeightedNodeDijkstra(WeightedMetroMap<WeightedMetroNode> map, WeightedMetroNode start, WeightedMetroNode end) {
        this.map = map;
        Optional<WeightedMetroNode> optionalStart = map.getNode(start);
        Optional<WeightedMetroNode> optionalEnd = map.getNode(end);
        if (optionalStart.isPresent() && optionalEnd.isPresent()) {
            startNode = optionalStart.get();
            endNode = optionalEnd.get();
        } else {
            throw new IllegalArgumentException("Start and end nodes must be present");
        }
    }

    // returns a pair of list and cost from startNode to endNode
    public Pair<List<WeightedMetroNode>, Integer> dijkstraSearch() {
        Map<WeightedMetroNode, Integer> distances = new HashMap<>();
        Map<WeightedMetroNode, WeightedMetroNode> routeMap = new HashMap<>();
        map.getGraph().keySet().forEach(s -> {
            distances.put(s, Integer.MAX_VALUE);
            routeMap.put(s, null);
        });
        distances.put(startNode, 0);

        PriorityQueue<WeightedMetroNode> queue = new PriorityQueue<>();
        queue.add(startNode);

        while (!queue.isEmpty()) {
            WeightedMetroNode currentNode = queue.poll();
            List<Pair<WeightedMetroNode, Integer>> neighbours = getNeighbours(currentNode);
            for (Pair<WeightedMetroNode, Integer> pair: neighbours) {
                if (distances.get(currentNode) + pair.v < distances.get(pair.t)) {
                    distances.put(pair.t, pair.v + distances.get(currentNode));
                    routeMap.put(pair.t, currentNode);
                    queue.remove(pair.t);
                    queue.add(pair.t);
                }
            }
        }
        if (routeMap.containsKey(endNode) && routeMap.containsKey(startNode)) {
            List<WeightedMetroNode> route = AlgoUtils.reconstructPath(routeMap, endNode);
            return new Pair<>(route, distances.get(endNode));
        } else {
            return null;
        }
    }

    private List<Pair<WeightedMetroNode, Integer>> getNeighbours(WeightedMetroNode currentNode) {
        Set<WeightedMetroNode> neighbours = map.getNeighbours(currentNode);
        return neighbours.stream().map(n -> processNeighbourForNode(n, currentNode)).toList();
    }

    private Pair<WeightedMetroNode, Integer> processNeighbourForNode(WeightedMetroNode neighbourNode,
            WeightedMetroNode currentNode) {
        int distance = currentNode.getTime();
        if (!neighbourNode.getLine().equals(currentNode.getLine())) {
            distance += TRANSFER_TIME;
        }
        return new Pair<>(neighbourNode, distance);
    }

    public static class Pair<T, V> {
        public T t;
        public V v;

        Pair(T t, V v) {
            this.t = t;
            this.v = v;
        }
    }
}
