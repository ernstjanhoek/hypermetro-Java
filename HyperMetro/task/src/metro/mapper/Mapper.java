package metro.mapper;

import metro.file.Station;

import java.util.Map;

public interface Mapper<T> {
    T buildAndConnect(Map<String, Map<String, Station>> inputMap);
}
