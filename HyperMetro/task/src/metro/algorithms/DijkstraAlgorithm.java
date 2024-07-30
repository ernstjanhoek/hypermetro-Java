package metro.algorithms;

import metro.modelv3.WeightedMetroMap;
import metro.modelv3.WeightedMetroNode;

import java.util.Optional;
import java.util.Set;

public class DijkstraAlgorithm {
    WeightedMetroMap map;
    WeightedMetroNode startNode;
    WeightedMetroNode endNode;

    public DijkstraAlgorithm(WeightedMetroMap map, WeightedMetroNode start, WeightedMetroNode end) {
        this.map = map;
        Optional<WeightedMetroNode> optionalStart = map.getNode(start);
        Optional<WeightedMetroNode> optionalEnd = map.getNode(end);
        if (optionalStart.isPresent() && optionalEnd.isPresent()) {
            startNode = optionalStart.get();
            endNode = optionalEnd.get();
        } else {
            throw new IllegalArgumentException("Start and end nodes must be present");
        }
    }

    public void search() {
        Set<WeightedMetroNode> neighbours = map.getNeighbours(startNode);
        Set<WeightedMetroNode> neighbours2 = map.getNeighbours(endNode);
    }


}
