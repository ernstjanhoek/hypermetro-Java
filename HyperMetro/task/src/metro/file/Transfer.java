package metro.file;

public class Transfer {
    private final String line;
    private final String station;

    public Transfer(String line, String station) {
        this.line = line;
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", station, line);

    }
}
