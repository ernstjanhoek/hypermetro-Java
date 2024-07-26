package metro.modelv2;

import metro.file.Station;
import metro.file.Transfer;
import java.util.*;
import java.util.stream.Collectors;

public class MetroMap {
    Map<MetroNode, Set<MetroEdge>> map;
    Set<MetroLine> lines;

    public MetroMap(Map<MetroNode, Set<MetroEdge>> map, Set<MetroLine> lines) {
        this.map = map;
        this.lines = lines;
    }

    public Optional<MetroLine> mapArgToMetroLine(String arg) {
       if (lines.contains(new MetroLine(arg))) {
           return Optional.of(new MetroLine(arg));
       } else {
           return Optional.empty();
       }
    }

    public void remove(String stationName, MetroLine line) {
        MetroNode node = new MetroNode(stationName, line);

        List<MetroEdge> edges = this.map.get(node).stream()
                .filter(ed -> ed.getLine().equals(line))
                .filter(ed -> ed.getDestination().equals(node) || ed.getOrigin().equals(node))
                .toList();

        MetroNode newEdgeStart;
        MetroNode newEdgeEnd;

        // create edge between A - C / C - A
        if (edges.get(0).getOrigin().equals(edges.get(1).getDestination())) {
            newEdgeStart = edges.get(1).getOrigin();
            newEdgeEnd = edges.get(0).getDestination();
        } else {
            newEdgeStart = edges.get(0).getOrigin();
            newEdgeEnd = edges.get(1).getDestination();
        }
        MetroEdge edge = new MetroEdge(newEdgeStart, newEdgeEnd, line);
        this.addEdge(edge);

        // remove edges between A - B / B - A
        MetroEdge edgeAB = new MetroEdge(newEdgeStart, node, line);
        boolean removeAB = this.map.get(newEdgeStart).remove(edgeAB);
        boolean removeBA = this.map.get(node).remove(edgeAB);

        // remove edges B - C / C - B
        MetroEdge edgeBC = new MetroEdge(node, newEdgeEnd, line);
        boolean removeBC = this.map.get(node).remove(edgeBC);
        boolean removeCB = this.map.get(newEdgeEnd).remove(edgeBC);
    }

    public void addAtEnd(String stationName, MetroLine line) {
        MetroNode endNode = getEndNode(line);
        MetroNode newNode = new MetroNode(stationName, line);

        MetroEdge edge = new MetroEdge(endNode, newNode, line);

        this.addEdge(edge);
    }

    public void addEdge(MetroEdge edge) {
        // add for origin
        if (!this.map.containsKey(edge.getOrigin())) {
            Set<MetroEdge> metroEdges = new HashSet<>();
            metroEdges.add(edge);
            this.map.put(edge.getDestination(), metroEdges);
        } else {
            this.map.get(edge.getOrigin()).add(edge);
        }

        // add entry for destination
        if (!this.map.containsKey(edge.getDestination())) {
            Set<MetroEdge> metroEdges = new HashSet<>();
            metroEdges.add(edge);
            this.map.put(edge.getDestination(), metroEdges);
        } else {
            this.map.get(edge.getDestination()).add(edge);
        }
    }

    private void removeEdgesBetweenNodes(
            MetroNode startingNode,
            MetroNode endingNode) {
    }

    private boolean isEndNode(MetroNode node) {
       return false;
    }

    private boolean isStartingNode(MetroNode node) {
       return false;
    }

    private void removeEdgeFromStartingNode(MetroNode node) {

    }

    private void removeEdgeFromEndingNode(MetroNode node) {

    }

    private MetroNode getEndNode(MetroLine line) {
        Optional<MetroEdge> candidate = getMetroEdgeCandidate(line);

        MetroNode startNode = null;

        while (candidate.isPresent()) {
            startNode = candidate.get().getDestination();
            Optional<Set<MetroEdge>> edges = findNextEdges(candidate.get());
            if (edges.isPresent()) {
                candidate = filterByLine(edges.get(), line);
            }
        }
        return startNode;
    }

    private MetroNode getStartNode(MetroLine line) {
        Optional<MetroEdge> candidate = getMetroEdgeCandidate(line);

        MetroNode startNode = null;

        while (candidate.isPresent()) {
            startNode = candidate.get().getOrigin();
            Optional<Set<MetroEdge>> edges = findPreviousEdges(candidate.get());
            if (edges.isPresent()) {
                candidate = filterByLine(edges.get(), line);
            }
        }
        return startNode;
    }

    private Optional<MetroEdge> getMetroEdgeCandidate(MetroLine line) {
        Optional<MetroEdge> candidate = this.map.values().stream()
                .flatMap(Set::stream)
                .filter(edge -> edge.getLine().equals(line))
                .findFirst();
        return candidate;
    }

    public List<Station> getStationsForLine(MetroLine line) {

        Optional<MetroEdge> startEdge = this.map.get(getStartNode(line)).stream().findFirst();

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
    }

    private Station mapEdgeOriginToStation(MetroEdge edge, MetroLine line) {
        String stationNameStart = edge.getOrigin().getName();

        return new Station(stationNameStart, this.map.get(edge.getOrigin()).stream()
                .filter(ed -> !ed.getLine().equals(line))
                .map(ed -> ed.getLine().getName())
                .map(t -> new Transfer(t, stationNameStart)).distinct().toArray(Transfer[]::new));
    }

    private Station mapEdgeDestinationToStation(MetroEdge edge, MetroLine line) {
        String stationNameEnd = edge.getDestination().getName();

        return new Station(stationNameEnd, this.map.get(edge.getDestination()).stream()
                .filter(ed -> !ed.getLine().equals(line))
                .map(ed -> ed.getLine().getName())
                .map(t -> new Transfer(t, stationNameEnd)).distinct().toArray(Transfer[]::new));
    }

    private Optional<MetroEdge> filterByLine(Set<MetroEdge> edges, MetroLine line) {
        List<MetroEdge> edgeList = edges.stream().filter(e -> e.getLine().equals(line)).toList();
        if (edgeList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(edgeList.get(0));
        }
    }

    private Optional<Set<MetroEdge>> findPreviousEdges(MetroEdge edge) {
        Set<MetroEdge> result = this.map.get(edge.getOrigin()).stream()
                .filter(e -> e.getDestination().equals(edge.getOrigin()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }

    private Optional<Set<MetroEdge>> findNextEdges(MetroEdge edge) {
        Set<MetroEdge> result = this.map.get(edge.getDestination()).stream()
                .filter(e -> e.getOrigin().equals(edge.getDestination()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }
}
