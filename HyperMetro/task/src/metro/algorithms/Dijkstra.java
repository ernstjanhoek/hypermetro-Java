package metro.algorithms;

import metro.modelv2.MetroNode;
import metro.modelv4.WeightedMetroEdge;
import metro.modelv4.WeightedEdgesMap;
import java.util.*;

public class Dijkstra<T extends MetroNode> {
    WeightedEdgesMap<T> map;
    T startNode;
    T endNode;
    static final Integer TRANSFER_TIME = 7;

    public Dijkstra(WeightedEdgesMap<T> map, T start, T end) {
        this.map = map;
        Optional<T> optionalStart = map.getNode(start);
        Optional<T> optionalEnd = map.getNode(end);
        if (optionalStart.isPresent() && optionalEnd.isPresent()) {
            startNode = optionalStart.get();
            endNode = optionalEnd.get();
        } else {
            throw new IllegalArgumentException("Start and end nodes must be present");
        }
    }

    public Dijkstra.Pair<List<T>, Integer> dijkstraSearch() {
        Map<T, Integer> distances = new HashMap<>();
        Map<T, T> routeMap = new HashMap<>();
        map.getGraph().keySet().forEach(s -> {
            distances.put(s, Integer.MAX_VALUE);
            routeMap.put(s, null);
        });
        distances.put(startNode, 0);

        PriorityQueue<TimedNode<T>> queue = new PriorityQueue<>();
        queue.add(new TimedNode<>(startNode, 0));

        while (!queue.isEmpty()) {
            T currentNode = queue.poll().t;
            List<Dijkstra.Pair<T, Integer>> neighbours = getNeighbours(currentNode);
            for (Dijkstra.Pair<T, Integer> pair: neighbours) {
                if (distances.get(currentNode) + pair.v < distances.get(pair.t)) {
                    distances.put(pair.t, pair.v + distances.get(currentNode));
                    routeMap.put(pair.t, currentNode);
                    queue.remove(new TimedNode<>(pair.t));
                    queue.add(new TimedNode<>(pair.t, pair.v));
                }
            }
        }
        if (routeMap.containsKey(endNode) && routeMap.containsKey(startNode)) {
            List<T> route = AlgoUtils.reconstructPath(routeMap, endNode);
            return new Dijkstra.Pair<>(route, distances.get(endNode));
        } else {
            return null;
        }
    }

    private List<Dijkstra.Pair<T, Integer>> getNeighbours(T currentNode) {
        Set<T> neighbours = map.getNeighbours(currentNode);
        return neighbours.stream().map(n -> processNeighbourForNode(n, currentNode)).toList();
    }

    private Dijkstra.Pair<T, Integer> processNeighbourForNode(T neighbourNode, T currentNode) {
        Optional<WeightedMetroEdge<T>> edge = map.getEdgesByNode(currentNode).stream().filter(e -> (e.getDestination().equals(currentNode) && e.getOrigin().equals(neighbourNode)) ||
                (e.getDestination().equals(neighbourNode) && e.getOrigin().equals(currentNode)))
                .findFirst();
        int distance = edge.map(WeightedMetroEdge::getTime).orElse(getCostForNeighbourPlusTransfer(currentNode));
        return new Dijkstra.Pair<>(neighbourNode, distance);
    }

    private int getCostForNeighbourPlusTransfer(T currentNode) {
        Optional<WeightedMetroEdge<T>> optionalEdge = map.getEdgesByOrigin(currentNode).stream().findFirst();
        if (optionalEdge.isPresent()) {
            return optionalEdge.get().getTime() + TRANSFER_TIME;
        } else {
            Optional<WeightedMetroEdge<T>> optionalEdge2 = map.getEdgesByDestination(currentNode).stream().findFirst();
            return optionalEdge2.map(tWeightedMetroEdge -> tWeightedMetroEdge.getTime() + TRANSFER_TIME).orElse(TRANSFER_TIME);
        }
    }

    public static class Pair<T, V> {
        public T t;
        public V v;

        Pair(T t, V v) {
            this.t = t;
            this.v = v;
        }
    }

    static class TimedNode<T> implements Comparable<TimedNode<T>> {
        T t;
        int time;

        TimedNode(T t) {
            this.t = t;
            this.time = -1;
        }

        TimedNode(T t, int time) {
            this.t = t;
            this.time = time;
        }

        @Override
        public int compareTo(TimedNode<T> tTimedNode) {
            return Integer.compare(time, tTimedNode.time);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimedNode<?> timedNode = (TimedNode<?>) o;
            return Objects.equals(t, timedNode.t);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(t);
        }
    }
}
