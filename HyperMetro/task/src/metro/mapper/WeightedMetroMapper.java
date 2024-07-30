package metro.mapper;

import metro.file.Transfer;
import metro.file.WeightedStation;
import metro.modelv2.MetroEdge;
import metro.modelv3.WeightedMetroMap;
import metro.modelv3.WeightedMetroNode;

import java.util.*;

public class WeightedMetroMapper implements Mapper<WeightedMetroMap, WeightedStation> {
    @Override
    public WeightedMetroMap buildAndConnect(Map<String, Map<String, WeightedStation>> inputMap) {

        Set<String> lines = inputMap.keySet();
        Map<WeightedMetroNode, Set<MetroEdge<WeightedMetroNode>>> metroMap = new HashMap<>();
        Map<String, List<WeightedMetroNode>> mapWithNodes = new HashMap<>();
        Map<String, Set<WeightedStation>> stationsWithTransfers = new HashMap<>();

        inputMap.forEach((line, stationMap) -> {
            mapWithNodes.computeIfAbsent(line, k -> new ArrayList<>());
            stationsWithTransfers.computeIfAbsent(line, k -> new HashSet<>());
            stationMap.forEach((id, station) -> {
                WeightedMetroNode node = new WeightedMetroNode(station.getName(), line, station.getTime());
                metroMap.put(node, new HashSet<>());
                mapWithNodes.get(line).add(node);
                if (station.getTransfer().length > 0) {
                    stationsWithTransfers.get(line).add(station);
                }
            });
        });

        // connect nodes with edges
        mapWithNodes.forEach((line, nodeMap) -> {
            for (int i = 1; i < nodeMap.size(); i++) {
                MetroEdge<WeightedMetroNode> edge = new MetroEdge<>(nodeMap.get(i - 1), nodeMap.get(i));
                metroMap.get(nodeMap.get(i - 1)).add(edge);
                metroMap.get(nodeMap.get(i)).add(edge);
            }
        });

        // add transfers to nodes
        stationsWithTransfers.forEach((originLine, transferSet) -> {
            transferSet.forEach(s -> {
                // get origin node
                WeightedMetroNode originNode = null;
                for (WeightedMetroNode node : mapWithNodes.get(originLine)) {
                    if (node.equals(new WeightedMetroNode(s.getName(), originLine, s.getTime()))) {
                        originNode = node;
                    }
                }
                Transfer[] transferObjects = s.getTransfer();
                for (Transfer transferObject : transferObjects) {
                    if (originNode != null) {
                        Optional<WeightedMetroNode> transferNode = mapWithNodes.get(transferObject.getLine()).stream()
                                .filter(n -> n.getName().equals(transferObject.getStation()) &&
                                        n.getLine().equals(transferObject.getLine()))
                                .findFirst();
                        transferNode.ifPresent(originNode::addTransfer);
                    }
                }
            });
        });

        return new WeightedMetroMap(metroMap, lines);
    }
}
