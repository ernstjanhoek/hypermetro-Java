package metro.controller;

import metro.file.Station;
import metro.file.Transfer;
import metro.modelv2.MetroEdge;
import metro.modelv2.MetroLine;
import metro.modelv2.MetroNode;

import java.util.*;
import java.util.stream.Collectors;

public class MetroNetworkController implements CommandExecutor {
    boolean isRunning = true;
    private final Map<MetroNode, Set<MetroEdge>> metroNodeMap;

    public MetroNetworkController(Map<MetroNode, Set<MetroEdge>> metroNodeMap) {
        super();
        this.metroNodeMap = metroNodeMap;
    }

    public Map<MetroNode, Set<MetroEdge>> getMetroNodeMap() {
        return metroNodeMap;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void exit() {
        isRunning = false;
    }

    @Override
    public void output(String lineName) {
        try {
            // this deque holds edges that meet criteria (MetroLine.name == lineName)
            Deque<MetroEdge> edgeDeque = new ArrayDeque<>();
            MetroLine line = new MetroLine(lineName);

            // (some) starting edge
            Optional<MetroEdge> startingEdge = metroNodeMap.values().stream()
                    .flatMap(Set::stream)
                    .filter(edge -> edge.getDestination().getName().equals("Edgver road")) // .getLine().getName().equals(lineName))
                    .findFirst();
            // find previous by finding all edges for origin node and filtering where 'origin node' as value == 'destination'
            metroNodeMap.get(startingEdge.get().getOrigin());

            startingEdge.get().getOrigin();

            //edge destination
            startingEdge.get().getDestination();

            while (startingEdge.isPresent()) {
                edgeDeque.add(startingEdge.get());
                Optional<Set<MetroEdge>> previousEdges = findPreviousEdges(startingEdge.get());
                if (previousEdges.isPresent()) {
                    startingEdge = filterByLine(previousEdges.get(), line);
                } else {
                    startingEdge = Optional.empty();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
        /*
        if (startingEdge.isPresent()) {
            MetroEdge edge = startingEdge.get();
            MetroNode startNode = new MetroNode(edge.getOrigin().getName());
            Set<MetroEdge> edges = metroNodeMap.get(startNode);
            List<Transfer> transfers = edges.stream()
                    .filter(e -> e.getOrigin().equals(startNode))
                    .map(MetroEdge::getLine)
                    .map(e -> new Transfer(e.getName(), startNode.getName()))
                    .toList();

            lineDeque.add(new Station(edge.getOrigin().getName(), transfers.toArray(new Transfer[0])));

            // find previous node?
            MetroNode newNode = startNode;
            Optional<Set<MetroEdge>> newEdges = findPreviousEdgesByLine(edge);
            while (newEdges.isPresent() && !newEdges.get().isEmpty()) {

                Set<MetroEdge> locEdges = newEdges.get();
                newNode = new MetroNode(edge.getOrigin().getName());
                MetroNode finalNewNode = newNode;
                MetroNode finalNewNode1 = newNode;

                List<Transfer> newTransfers = locEdges.stream()
                        .filter(e -> e.getOrigin().equals(newNode))
                        .map(MetroEdge::getLine)
                        .map(e -> new Transfer(e.getName(), newNode.getName()))
                        .toList();
                lineDeque.add(new Station(newNode.getName(), newTransfers.toArray(new Transfer[0])));

                Optional<MetroEdge> newEdge = filterOriginByLine(locEdges, new MetroLine(lineName));

                newEdges = newEdge.flatMap(this::findPreviousEdgesByLine);
            }

            // find next node?


        }

        System.out.println("depot");
        lineDeque.forEach(System.out::println);
        System.out.println("depot");
        */
    private Optional<MetroEdge> filterByLine(Set<MetroEdge> edges, MetroLine line) throws Exception {
        List<MetroEdge> edgeList = edges.stream().filter(e -> e.getLine().equals(line)).toList();

        if (edgeList.size() != 1) {
            throw new Exception("Invalid size for line filter list: each station can only have one origin.");
        }

        return Optional.of(edgeList.get(0));
    }

    // find previous by finding all edges for origin node and filtering where 'origin node' as value == 'destination'
    private Optional<Set<MetroEdge>> findPreviousEdges(MetroEdge edge) {
        Set<MetroEdge> result = metroNodeMap.get(edge.getOrigin()).stream()
                .filter(e -> e.getDestination().equals(edge.getOrigin()))
                .collect(Collectors.toSet());
        return Optional.of(result);
    }

    private Optional<Set<MetroEdge>> findNextEdges(MetroEdge edge) {
        Set<MetroEdge> result = metroNodeMap.get(edge.getDestination()).stream()
                .filter(e -> e.getOrigin().equals(edge.getDestination()))
                .collect(Collectors.toSet());
        return Optional.of(result);

    }

    @Override
    public void append(String lineName, String stationName) {

    }

    @Override
    public void remove(String lineName, String stationName) {

    }

    @Override
    public void addHead(String lineName, String stationName) {

    }

    @Override
    public void connect(String lineName1, String stationName1, String lineName2, String stationName2) {

    }
}
