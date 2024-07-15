package metro.controller;

public interface CommandExecutor {
    void exit();
    void output(String lineName);
    void append(String lineName, String stationName);
    void remove(String lineName, String stationName);
    void addHead(String lineName, String stationName);
    void connect(String lineName1, String stationName1, String lineName2, String stationName2);
    boolean isRunning();
}
