package metro.controller;

public interface CommandExecutor {
    void exit();
    void output(String line);
    void append(String line, String stationName);
    void remove(String line, String stationName);
    void addHead(String line, String stationName);
    void connect(String line1, String stationName1, String line2, String stationName2);
    void add(String line, String stationName, int time);
    void route(String line1, String stationName1, String line2, String stationName2);
    void fastestRoute(String line1, String stationName1, String line2, String stationName2);
    boolean isRunning();
}
