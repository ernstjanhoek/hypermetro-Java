package metro.modelv2;

import java.util.Objects;

public class MetroEdge {
    private final MetroNode origin;
    private final MetroNode destination;
    private final MetroLine line;

    public MetroEdge(MetroNode origin, MetroNode destination, MetroLine line) {
        this.origin = origin;
        this.destination = destination;
        this.line = line;
    }

    public MetroNode getDestination() {
        return destination;
    }

    public MetroNode getOrigin() {
        return origin;
    }

    public MetroLine getLine() {
        return line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin.getName(), destination.getName(), line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroEdge metroEdge = (MetroEdge) o;
        return Objects.equals(origin, metroEdge.origin) && Objects.equals(destination, metroEdge.destination) && Objects.equals(line, metroEdge.line);
    }
}
