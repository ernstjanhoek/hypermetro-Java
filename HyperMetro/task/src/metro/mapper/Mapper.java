package metro.mapper;

import metro.file.Station;

import java.util.Map;

public interface Mapper<T, U extends Station> {
    T buildAndConnect(Map<String, Map<String, U>> inputMap);
}
