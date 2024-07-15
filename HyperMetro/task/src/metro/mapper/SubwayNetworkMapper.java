package metro.mapper;

import metro.file.Station;
import metro.file.Transfer;
import metro.model.Edge;
import metro.model.SubwayLine;
import metro.controller.SubwayLineController;
import metro.model.SubwayNode;

import java.util.List;
import java.util.Map;

public class SubwayNetworkMapper implements Mapper<SubwayLineController> {
    @Override
    public SubwayLineController buildAndConnect(Map<String, Map<String, Station>> inputMap) {
        SubwayLineController network = new SubwayLineController();
        inputMap.forEach((key, value) -> {
            SubwayLine subway = SubwayNetworkMapper.buildSubwayLine(value, key);
            network.getSubwayLines().put(key, subway);
        });
        SubwayNetworkMapper.connectSubways(network.getSubwayLines(), inputMap);
        return network;
    }

    private static SubwayLine buildSubwayLine(Map<String, Station> map, String name) {
        SubwayLine subwayLine = new SubwayLine(name);
        List<SubwayNode> orderedStations = map.values().stream()
                .map(e -> new SubwayNode(e.getName()))
                .toList();

        subwayLine.setHead(orderedStations.get(0));
        subwayLine.getHead().setNext(orderedStations.get(1));

        subwayLine.setTail(orderedStations.get(orderedStations.size() - 1));
        subwayLine.getTail().setPrevious(orderedStations.get(orderedStations.size() - 2));

        for (int i = 1; i < map.size() - 1; i++) {
            SubwayNode node = orderedStations.get(i);
            node.setPrevious(orderedStations.get(i - 1));
            node.setNext(orderedStations.get(i + 1));
        }

        return subwayLine;
    }

    private static void connectSubways(Map<String, SubwayLine> subwayLineMap, Map<String, Map<String, Station>> outerStationMap) {
        outerStationMap.forEach((key, stationMap) -> {
            stationMap.forEach((key2, station) -> {
                for (Transfer transfer : station.getTransfer()) {
                    connectByTransferStation(subwayLineMap, transfer, key);
                }
            });
        });
    }

    private static void connectByTransferStation(Map<String, SubwayLine> subwayLineMap, Transfer transfer, String outerLineName) {
        // get line from transferObject in subwayMap
        SubwayLine edgeSubway = subwayLineMap.get(transfer.getLine());
        // get line from origin in subwayMap
        SubwayLine subwayLine = subwayLineMap.get(outerLineName);
        String nodeName = transfer.getStation();

        SubwayNode node = subwayLine.find(nodeName);
        node.addEdge(new Edge(edgeSubway, edgeSubway.find(nodeName)));
    }
}
