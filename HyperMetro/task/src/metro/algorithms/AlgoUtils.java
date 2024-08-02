package metro.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AlgoUtils {
    public static<T> List<T> reconstructPath(Map<T, T> parentMap, T target) {
        List<T> path = new ArrayList<>();
        for (T at = target; at != null; at = parentMap.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

}
