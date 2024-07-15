package metro.modelv2;

import java.util.Objects;

public class MetroNode {
    private final String name;

    public MetroNode(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroNode metroNode = (MetroNode) o;
        return Objects.equals(name, metroNode.name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
