package metro.controller;

import metro.modelv2.MetroLine;

public interface CommandExecutor {
    void exit();
    void output(MetroLine line);
    void append(MetroLine line, String stationName);
    void remove(MetroLine line, String stationName);
    void addHead(MetroLine line, String stationName);
    void connect(MetroLine line1, String stationName1, MetroLine line2, String stationName2);
    boolean isRunning();
}
