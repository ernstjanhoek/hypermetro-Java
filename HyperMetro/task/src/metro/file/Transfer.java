package metro.file;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(line, transfer.line) && Objects.equals(station, transfer.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, station);
    }
}
