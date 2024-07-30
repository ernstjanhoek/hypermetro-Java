package metro.algorithms;

import java.util.List;
import java.util.Map;

public interface BreadthFirstSearch<T> {
    List<T> bfs(T start, T end);
    List<T> reconstructPath(Map<T, T> parentMap, T target);
}
