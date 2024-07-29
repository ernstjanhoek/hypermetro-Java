package metro.modelv2;

import metro.base.BaseEdge;
import metro.base.BaseGraph;
import metro.file.Station;
import metro.file.Transfer;
import java.util.*;
import java.util.stream.Collectors;

public class MetroMap extends BaseGraph<MetroNode, MetroEdge> {
    Set<String> lines;

    public MetroMap(Map<MetroNode, Set<MetroEdge>> map, Set<String> lines) {
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

    public void connectNodes(MetroNode firstNode, MetroNode secondNode) {
        Optional<MetroNode> firstMetroNode = super.getNode(firstNode);
        Optional<MetroNode> secondMetroNode = super.getNode(secondNode);
        if (firstMetroNode.isPresent() && secondMetroNode.isPresent()) {
            firstMetroNode.get().addTransfer(secondMetroNode.get());
            secondMetroNode.get().addTransfer(firstMetroNode.get());
        }
    }

    public void remove(MetroNode node) {
        List<MetroEdge> edges = super.getEdgesByNode(node).stream()
                .filter(ed -> ed.getDestination().getLine().equals(node.getLine()) || ed.getOrigin().getLine().equals(node.getLine()))
                .filter(ed -> ed.getDestination().equals(node) || ed.getOrigin().equals(node))
                .toList();

        // optionals for new edge A-C
        Optional<MetroEdge> newEdgeOrigin;
        Optional<MetroEdge> newEdgeDestination;

        // create edge between A - C / C - A
        if (edges.size() > 1) {
            MetroNode newEdgeStart;
            MetroNode newEdgeEnd;

            if (edges.get(0).getOrigin().equals(edges.get(1).getDestination())) {
                newEdgeStart = edges.get(1).getOrigin();
                newEdgeEnd = edges.get(0).getDestination();
            } else {
                newEdgeStart = edges.get(0).getOrigin();
                newEdgeEnd = edges.get(1).getDestination();
            }

            MetroEdge edge = new MetroEdge(newEdgeStart, newEdgeEnd);
            super.addEdge(newEdgeStart, newEdgeEnd, edge);

            // remove edges between A - B / B - A
            MetroEdge edgeAB = new MetroEdge(newEdgeStart, node);
            super.removeEdge(newEdgeStart, node, edgeAB);

            // remove edges B - C / C - B
            MetroEdge edgeBC = new MetroEdge(node, newEdgeEnd);
            super.removeEdge(node, newEdgeEnd, edgeBC);
        } else if (edges.size() == 1) {
            MetroEdge edge = edges.get(0);
            MetroNode startNode = edges.get(0).getOrigin();
            MetroNode endNode = edges.get(0).getDestination();
            super.removeEdge(startNode, endNode, edge);
        } else {
            System.out.println("Node not found!");
        }
    }

    public void addAtEnd(MetroNode newNode) {
        Optional<MetroNode> endNode = getEndNode(newNode.getLine());
        if (endNode.isPresent()) {
            super.addNode(newNode);
            MetroEdge edge = new MetroEdge(endNode.get(), newNode);
            super.addEdge(newNode, endNode.get(), edge);
        }
    }

    public void addAtTail(MetroNode newNode) {
        Optional<MetroNode> startNode = getStartNode(newNode.getLine());
        if (startNode.isPresent()) {
            super.addNode(newNode);

            MetroEdge edge = new MetroEdge(newNode, startNode.get());
            super.addEdge(newNode, startNode.get(), edge);
        }
    }

    private Optional<MetroNode> getEndNode(String line) {
        Optional<MetroEdge> candidate = getMetroEdgeCandidate(line);

        MetroNode startNode = null;

        while (candidate.isPresent()) {
            startNode = candidate.get().getDestination();
            Optional<Set<MetroEdge>> edges = findNextEdges(candidate.get());
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

    private Optional<MetroNode> getStartNode(String line) {
        Optional<MetroEdge> candidate = getMetroEdgeCandidate(line);

        MetroNode startNode = null;
        while (candidate.isPresent()) {
            startNode = candidate.get().getOrigin();
            Optional<Set<MetroEdge>> edges = findPreviousEdges(candidate.get());
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

    private Optional<MetroEdge> getMetroEdgeCandidate(String line) {
        return this.getGraph().values().stream()
                .flatMap(Set::stream)
                .filter(edge -> edge.getOrigin().getLine().equals(line) || edge.getDestination().getLine().equals(line))
                .findFirst();
    }

    public List<Station> getStationsForLine(String line) {
        Optional<MetroNode> startNode = getStartNode(line);

        if (startNode.isPresent()) {
            Optional<MetroEdge> startEdge = getGraph().get(startNode.get()).stream().findFirst();

            List<Station> stationList = new ArrayList<>();

            if (startEdge.isPresent()) {
                stationList.add(mapEdgeOriginToStation(startEdge.get(), line));
                stationList.add(mapEdgeDestinationToStation(startEdge.get(), line));
            }

            while (startEdge.isPresent()) {
                Optional<Set<MetroEdge>> edges = findNextEdges(startEdge.get());
                if (edges.isPresent()) {
                    Optional<MetroEdge> edge = filterByLine(edges.get(), line);
                    startEdge = edge;
                    if (edge.isPresent()) {
                        stationList.add(mapEdgeDestinationToStation(edge.get(), line));
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

    private Station mapEdgeOriginToStation(MetroEdge edge, String line) {
        MetroNode stationStart = edge.getOrigin();
        return new Station(stationStart.getName(),
                stationStart.getTransfers().stream()
                        .map(e -> new Transfer(e.getLine(), e.getName()))
                        .toList().toArray(new Transfer[0])
        );
    }

    private Station mapEdgeDestinationToStation(MetroEdge edge, String line) {
        MetroNode stationDestination = edge.getDestination();
        return new Station(stationDestination.getName(),
                stationDestination.getTransfers().stream()
                        .map(e -> new Transfer(e.getLine(), e.getName()))
                        .toList().toArray(new Transfer[0])
        );
    }

    private Optional<MetroEdge> filterByLine(Set<MetroEdge> edges, String line) {
        List<MetroEdge> edgeList = edges.stream().filter(e -> e.getOrigin().getLine().equals(line)).toList();
        if (edgeList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(edgeList.get(0));
        }
    }

    private Optional<Set<MetroEdge>> findPreviousEdges(MetroEdge edge) {
        Set<MetroEdge> result = getGraph().get(edge.getOrigin()).stream()
                .filter(e -> e.getDestination().equals(edge.getOrigin()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }

    private Optional<Set<MetroEdge>> findNextEdges(MetroEdge edge) {
        Set<MetroEdge> result = getGraph().get(edge.getDestination()).stream()
                .filter(e -> e.getOrigin().equals(edge.getDestination()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }

    // override getNeighbours (bfs) to find possible transfers for MetroMap
    @Override
    public Set<MetroNode> getNeighbours(MetroNode current) {

        Set<MetroNode> transfersFromCurrent = current.getTransfers();

        Set<MetroEdge> edgesFromCurrent = getEdgesByDestination(current);
        Set<MetroNode> neighboursFromCurrent = edgesFromCurrent.stream()
                .map(BaseEdge::getOrigin)
                .collect(Collectors.toSet());

        Set<MetroNode> neighboursToCurrent = getEdgesByOrigin(current).stream()
                .map(BaseEdge::getDestination)
                .collect(Collectors.toSet());

        neighboursToCurrent.addAll(neighboursFromCurrent);
        neighboursToCurrent.addAll(transfersFromCurrent);
        return neighboursToCurrent;
    }
}
