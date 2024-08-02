package metro.mapper;

import metro.file.ConnectedStation;
import metro.file.WeightedStation;
import metro.modelv2.MetroNode;
import metro.modelv4.WeightedMetroEdge;
import metro.modelv4.WeightedEdgesMap;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedEdgesMapper {
    public WeightedEdgesMap<MetroNode> buildAndConnect(Map<String, List<ConnectedStation>> inputMap) {
        Set<String> lines = inputMap.keySet();
        // map for output
        Map<MetroNode, Set<WeightedMetroEdge<MetroNode>>> metroMap = new HashMap<>();
        WeightedEdgesMap<MetroNode> map = new WeightedEdgesMap<>(metroMap, lines);

        Map<String, Set<WeightedStation>> stationsWithTransfers = new HashMap<>();

        inputMap.forEach((line, stations) -> {
            stations.forEach(station -> {
                MetroNode node = new MetroNode(station.getName(), line);

                // is het nodig om previous ook te mappen? Zo ja, misschien Map maken van WeightedStations om time cost makkelijker te vinden.
                Set<MetroNode> previous = mapConnectedStations(station.getPrev(), line);
                Set<MetroNode> next = mapConnectedStations(station.getNext(), line);
                Set<MetroNode> transfers = mapTransfers(station);

                map.addNode(node);
                MetroNode originNode = map.getNode(node).get();
                next.forEach(n -> {
                    map.addNode(n);
                    MetroNode destinationNode = map.getNode(n).get();
                    map.addEdge(originNode, n, new WeightedMetroEdge<>(originNode, destinationNode, station.getTime()));
                });
                transfers.forEach(n -> {
                    map.addNode(n);
                    MetroNode destinationNode = map.getNode(n).get();
                    map.connectNodes(originNode, destinationNode);
                });
            });
        });
        return map;
    }

    private Set<MetroNode> mapConnectedStations(String[] stations, String line) {
        return Arrays.stream(stations).map(e -> new MetroNode(e, line)).collect(Collectors.toSet());
    }

    private Set<MetroNode> mapTransfers(ConnectedStation station) {
        return Arrays.stream(station.getTransfer()).map(t -> new MetroNode(t.getStation(), t.getLine())).collect(Collectors.toSet());
    }

}
