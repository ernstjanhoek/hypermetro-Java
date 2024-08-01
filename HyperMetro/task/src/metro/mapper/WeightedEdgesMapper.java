package metro.mapper;

import metro.file.ConnectedStation;
import metro.file.WeightedStation;
import metro.modelv2.MetroNode;
import metro.modelv3.WeightedMetroEdge;
import metro.modelv4.WeightedEdgesMetroMap;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedEdgesMapper {
    public WeightedEdgesMetroMap<MetroNode> buildAndConnect(Map<String, List<ConnectedStation>> inputMap) {
        Set<String> lines = inputMap.keySet();
        // map for output
        Map<MetroNode, Set<WeightedMetroEdge<MetroNode>>> metroMap = new HashMap<>();

        // map of <line, list<n>> to keep track of nodes in correct order
        Map<String, List<MetroNode>> mapWithNodes = new HashMap<>();

        // map of <line, set<station> to keep of stations/node with transfers
        Map<String, Set<WeightedStation>> stationsWithTransfers = new HashMap<>();
        inputMap.forEach((line, stations) -> {
            stations.forEach(station -> {
                MetroNode node = new MetroNode(station.getName(), line);
                Set<MetroNode> previous = mapConnectedStations(station.getPrev(), line);
                Set<MetroNode> next = mapConnectedStations(station.getNext(), line);
                Set<MetroNode> transfers = mapTransfers(station);

            });
        });
        return null;
    }

    private WeightedMetroEdge<MetroNode> buildEdge(MetroNode origin, MetroNode destination, int time) {
        return new WeightedMetroEdge<>(origin, destination, time);
    }

    private Set<MetroNode> mapConnectedStations(String[] stations, String line) {
        return Arrays.stream(stations).map(e -> new MetroNode(e, line)).collect(Collectors.toSet());
    }

    private Set<MetroNode> mapTransfers(ConnectedStation station) {
        return Arrays.stream(station.getTransfer()).map(t -> new MetroNode(t.getStation(), t.getLine())).collect(Collectors.toSet());
    }

}
