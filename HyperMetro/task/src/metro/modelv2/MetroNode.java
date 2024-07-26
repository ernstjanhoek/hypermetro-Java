package metro.modelv2;

import java.util.Objects;

public class MetroNode {
    private final String name;
    private final MetroLine line;


    public MetroNode(String name, MetroLine line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroNode metroNode = (MetroNode) o;
        return Objects.equals(name, metroNode.name) && Objects.equals(line, metroNode.line);
    }
}
