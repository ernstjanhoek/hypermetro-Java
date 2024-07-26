package metro.mapper;

import metro.file.Station;
import metro.modelv2.MetroLine;
import metro.modelv2.MetroMap;
import metro.modelv2.MetroNode;
import metro.modelv2.MetroEdge;
import java.util.*;

public class MetroNetworkMapper implements Mapper<MetroMap> {
    public MetroMap buildAndConnect(Map<String, Map<String, Station>> inputMap) {
        Map<MetroLine, List<Station>> lineStations = getMetroLineListMap(inputMap);

        Map<MetroNode, Set<MetroEdge>> metroMap = new HashMap<>();

        lineStations.forEach((line, stationList) -> {
            Station[] stationArray = stationList.toArray(new Station[0]);

            for (int i = 0; i < stationArray.length - 1; i++) {
                // check if origin and destination nodes are in Map
                MetroNode origin = new MetroNode(stationArray[i].getName(), line);
                MetroNode destination = new MetroNode(stationArray[i + 1].getName(), line);
                MetroEdge metroEdge = new MetroEdge(origin, destination, line);

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
        return new MetroMap(metroMap, lineStations.keySet());
    }

    private static Map<MetroLine, List<Station>> getMetroLineListMap(Map<String, Map<String, Station>> inputMap) {
        Map<MetroLine, List<Station>> lineStations = new HashMap<>();

        for (Map.Entry<String, Map<String, Station>> entry : inputMap.entrySet()) {
            MetroLine line = new MetroLine(entry.getKey());
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
