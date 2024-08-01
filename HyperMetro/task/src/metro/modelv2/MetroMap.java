package metro.modelv2;

import metro.base.BaseEdge;
import metro.base.BaseGraph;
import metro.file.Station;
import metro.file.Transfer;
import java.util.*;
import java.util.stream.Collectors;

public class MetroMap<T extends MetroNode, U extends BaseEdge<T>> extends BaseGraph<T, U> {
    Set<String> lines;

    public MetroMap(Map<T, Set<U>> map, Set<String> lines) {
        super(map);
        this.lines = lines;
    }

    public Optional<String> findMetroLine(String arg) {
       if (lines.contains(arg)) {
           return Optional.of(arg);
       } else {
           return Optional.empty();
       }
    }

    public void connectNodes(T firstNode, T secondNode) {
        Optional<T> firstMetroNode = super.getNode(firstNode);
        Optional<T> secondMetroNode = super.getNode(secondNode);
        if (firstMetroNode.isPresent() && secondMetroNode.isPresent()) {
            firstMetroNode.get().addTransfer(secondMetroNode.get());
            secondMetroNode.get().addTransfer(firstMetroNode.get());
        }
    }

    public void remove(T node) {
        List<U> edges = super.getEdgesByNode(node).stream()
                .filter(ed -> ed.getDestination().getLine().equals(node.getLine()) || ed.getOrigin().getLine().equals(node.getLine()))
                .filter(ed -> ed.getDestination().equals(node) || ed.getOrigin().equals(node))
                .toList();

        Optional<U> edgeAB = super.getEdgesByDestination(node).stream().findFirst();
        Optional<U> edgeBC = super.getEdgesByOrigin(node).stream().findFirst();

        // create edge between A - C / C - A
        if (edgeAB.isPresent() && edgeBC.isPresent()) {
            T newEdgeStart = edgeAB.get().getOrigin();
            T newEdgeEnd = edgeBC.get().getDestination();

            U edge = (U) edges.get(0).splice(edges.get(1));
            super.addEdge(newEdgeStart, newEdgeEnd, edge);

            // remove edges between A - B / B - A
            super.removeEdge(newEdgeStart, node, edgeAB.get());

            // remove edges B - C / C - B
            super.removeEdge(node, newEdgeEnd, edgeBC.get());
        } else if (edges.size() == 1) {
            U edge = edges.get(0);
            T startNode = edges.get(0).getOrigin();
            T endNode = edges.get(0).getDestination();
            super.removeEdge(startNode, endNode, edge);
        } else {
            System.out.println("Node not found!");
        }
    }

    public void addAtEnd(T newNode, U edge) {
        Optional<T> endNode = getEndNode(newNode.getLine());
        if (endNode.isPresent()) {
            super.addNode(newNode);
            super.addEdge(newNode, endNode.get(), edge);
        }
    }

    public void addAtTail(T newNode, U edge) {
        Optional<T> startNode = getStartNode(newNode.getLine());
        if (startNode.isPresent()) {
            super.addNode(newNode);
            super.addEdge(newNode, startNode.get(), edge);
        }
    }

    public Optional<T> getEndNode(String line) {
        Optional<U> candidate = getMetroEdgeCandidate(line);

        T startNode = null;

        while (candidate.isPresent()) {
            startNode = candidate.get().getDestination();
            Optional<Set<U>> edges = findNextEdges(candidate.get());
            if (edges.isPresent()) {
                candidate = filterByLine(edges.get(), line);
            }
        }

        if (startNode == null) {
            return Optional.empty();
        } else {
            return Optional.of(startNode);
        }
    }

    public Optional<T> getStartNode(String line) {
        Optional<U> candidate = getMetroEdgeCandidate(line);

        T startNode = null;
        while (candidate.isPresent()) {
            startNode = candidate.get().getOrigin();
            Optional<Set<U>> edges = findPreviousEdges(candidate.get());
            if (edges.isPresent()) {
                candidate = filterByLine(edges.get(), line);
            }
        }
        if (startNode == null) {
            return Optional.empty();
        } else {
            return Optional.of(startNode);
        }
    }

    private Optional<U> getMetroEdgeCandidate(String line) {
        return this.getGraph().values().stream()
                .flatMap(Set::stream)
                .filter(edge -> edge.getOrigin().getLine().equals(line) || edge.getDestination().getLine().equals(line))
                .findFirst();
    }

    public List<Station> getStationsForLine(String line) {
        Optional<T> startNode = getStartNode(line);

        if (startNode.isPresent()) {
            Optional<U> startEdge = getGraph().get(startNode.get()).stream().findFirst();

            List<Station> stationList = new ArrayList<>();

            if (startEdge.isPresent()) {
                stationList.add(mapEdgeOriginToStation(startEdge.get()));
                stationList.add(mapEdgeDestinationToStation(startEdge.get()));
            }

            while (startEdge.isPresent()) {
                Optional<Set<U>> edges = findNextEdges(startEdge.get());
                if (edges.isPresent()) {
                    Optional<U> edge = filterByLine(edges.get(), line);
                    startEdge = edge;
                    if (edge.isPresent()) {
                        stationList.add(mapEdgeDestinationToStation(edge.get()));
                    } else {
                        startEdge = Optional.empty();
                    }
                }
            }
            return stationList;
        } else {
            return List.of();
        }
    }

    private Station mapEdgeOriginToStation(U edge) {
        MetroNode stationStart = edge.getOrigin();
        return new Station(stationStart.getName(),
                stationStart.getTransfers().stream()
                        .map(e -> new Transfer(e.getLine(), e.getName()))
                        .toList().toArray(new Transfer[0])
        );
    }

    private Station mapEdgeDestinationToStation(U edge) {
        MetroNode stationDestination = edge.getDestination();
        return new Station(stationDestination.getName(),
                stationDestination.getTransfers().stream()
                        .map(e -> new Transfer(e.getLine(), e.getName()))
                        .toList().toArray(new Transfer[0])
        );
    }

    private Optional<U> filterByLine(Set<U> edges, String line) {
        List<U> edgeList = edges.stream().filter(e -> e.getOrigin().getLine().equals(line)).toList();
        if (edgeList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(edgeList.get(0));
        }
    }

    private Optional<Set<U>> findPreviousEdges(U edge) {
        Set<U> result = getGraph().get(edge.getOrigin()).stream()
                .filter(e -> e.getDestination().equals(edge.getOrigin()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }

    private Optional<Set<U>> findNextEdges(U edge) {
        Set<U> result = getGraph().get(edge.getDestination()).stream()
                .filter(e -> e.getOrigin().equals(edge.getDestination()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }

    @Override
    public Set<T> getNeighbours(T current) {
        Set<T> neighbours = super.getNeighbours(current);

        Set<MetroNode> transfersCurrent = current.getTransfers();
        for (MetroNode transfer: transfersCurrent) {
            Set<T> transferNeighbours = super.getNeighbours((T) transfer);
            neighbours.addAll(transferNeighbours);
        }
        return neighbours;
    }
}
