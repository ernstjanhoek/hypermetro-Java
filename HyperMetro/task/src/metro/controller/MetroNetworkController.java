package metro.controller;

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
        // this deque holds edges that meet criteria (MetroLine.name == lineName)
        Deque<MetroEdge> edgeDeque = new ArrayDeque<>();
        MetroLine line = new MetroLine(lineName);

        // (some) starting edge
        Optional<MetroEdge> startingEdge = metroNodeMap.values().stream()
                .flatMap(Set::stream)
                .filter(edge -> edge.getLine().getName().equals(lineName))
                .findFirst();
        // find previous by finding all edges for origin node and filtering where 'origin node' as value == 'destination'
        startingEdge.ifPresent(edgeDeque::add);
        List<MetroNode> transferNodes = new ArrayList<>();
        startingEdge.ifPresent(e -> {
            transferNodes.addAll(findTransfers(e, line));
        });

        // previous Edges
        Optional<MetroEdge> edgePrev = startingEdge;
        while (edgePrev.isPresent()) {
            try {
                Optional<Set<MetroEdge>> previousEdges = findPreviousEdges(edgePrev.get());
                if (previousEdges.isPresent()) {
                    edgePrev = filterByLine(previousEdges.get(), line);
                    edgePrev.ifPresent( e-> {
                        edgeDeque.push(e);
                        transferNodes.addAll(findTransfers(e, line));
                    });
                }
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        // next Edges
        Optional<MetroEdge> edgeNext = startingEdge;
        while (edgeNext.isPresent()) {
            try {
                Optional<Set<MetroEdge>> nextEdges = findNextEdges(edgeNext.get());
                if (nextEdges.isPresent()) {
                    edgeNext = filterByLine(nextEdges.get(), line);
                    edgeNext.ifPresent(e -> {
                        edgeDeque.add(e);
                        transferNodes.addAll(findTransfers(e, line));
                    });
                }
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        System.out.println("depot");
        System.out.println(edgeDeque.getFirst().getOrigin().getName());
        edgeDeque.forEach(e -> System.out.println(e.getDestination().getName()));
        System.out.println("depot");
    }

    // find transfers for (origin) node for specific line
    private List<MetroNode> findTransfers(MetroEdge edge, MetroLine line) {
        Set<MetroEdge> originEdges = metroNodeMap.get(edge.getOrigin());
        List<MetroNode> transferNodeList = originEdges.stream()
                .filter(e -> e.getOrigin().equals(edge.getOrigin()))
                .filter(e -> !e.getLine().equals(line)) // assert that lines are not equal
                .map(MetroEdge::getOrigin)
                .toList();
        return transferNodeList;
    }

    private Optional<MetroEdge> filterByLine(Set<MetroEdge> edges, MetroLine line) throws Exception {
        List<MetroEdge> edgeList = edges.stream().filter(e -> e.getLine().equals(line)).toList();

        if (edgeList.size() > 1) {
            throw new Exception(String.format("Invalid size for line filter list (%d): each station can only have one origin.",
                    edgeList.size()));
        } else if (edgeList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(edgeList.get(0));
        }
    }

    // find previous by finding all edges for (origin) node and filtering where 'origin node' as value == 'destination'
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
