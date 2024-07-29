package metro.mapper;

import metro.file.Station;
import metro.file.Transfer;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
import metro.modelv2.MetroEdge;
import java.util.*;

public class MetroNetworkMapper implements Mapper<MetroMap> {
    public MetroMap buildAndConnect(Map<String, Map<String, Station>> inputMap) {
        Map<String, List<Station>> lineStations = getMetroLineListMap(inputMap);

        Map<MetroNode, Set<MetroEdge>> metroMap = new HashMap<>();

        lineStations.forEach((line, stationList) -> {
            Station[] stationArray = stationList.toArray(new Station[0]);

            for (int i = 0; i < stationArray.length - 1; i++) {
                // check if origin and destination nodes are in Map
                MetroNode origin = new MetroNode(stationArray[i].getName(), line);
                MetroNode destination = new MetroNode(stationArray[i + 1].getName(), line);

                MetroEdge metroEdge = new MetroEdge(origin, destination);

                // add entry for origin
                if (!metroMap.containsKey(origin)) {
                    HashSet<MetroEdge> metroEdges = new HashSet<>();
                    metroEdges.add(metroEdge);
                    metroMap.put(origin, metroEdges);
                } else {
                    metroMap.get(origin).add(metroEdge);
                }
                // add entry for destination
                if (!metroMap.containsKey(destination)) {
                    HashSet<MetroEdge> metroEdges = new HashSet<>();
                    metroEdges.add(metroEdge);
                    metroMap.put(destination, metroEdges);
                } else {
                    metroMap.get(destination).add(metroEdge);
                }
            }
        });

        // loop again over stationArray and connect transfers in nodes
        lineStations.forEach((line, stationList) -> {
            Station[] stationArray = stationList.toArray(new Station[0]);

            for (Station station : stationArray) {
                if (station.getTransfer().length > 0) {
                    Set<MetroNode> transfers = new HashSet<>();
                    for (Transfer transfer: station.getTransfer()) {
                        transfers.add(new MetroNode(transfer.getStation(), transfer.getLine()));
                    }
                    Optional<MetroNode> node = metroMap.keySet().stream()
                            .filter(n -> n.getName().equals(station.getName()) &&
                                    n.getLine().equals(line))
                            .findFirst();
                    node.ifPresent(metroNode -> transfers.forEach(metroNode::addTransfer));
                }
            }
        });

        return new MetroMap(metroMap, lineStations.keySet());
    }

    private static Map<String, List<Station>> getMetroLineListMap(Map<String, Map<String, Station>> inputMap) {
        Map<String, List<Station>> lineStations = new HashMap<>();

        for (Map.Entry<String, Map<String, Station>> entry : inputMap.entrySet()) {
            String line = entry.getKey();
            for (Map.Entry<String, Station> stationEntry : entry.getValue().entrySet()) {
                Station station = stationEntry.getValue();
                if (!lineStations.containsKey(line)) {
                    List<Station> stationList = new ArrayList<>();
                    stationList.add(station);
                    lineStations.put(line, stationList);
                } else {
                    List<Station> stationSet = lineStations.get(line);
                    stationSet.add(station);
                    lineStations.put(line, stationSet);
                }
            }
        }
        return lineStations;
    }
}
