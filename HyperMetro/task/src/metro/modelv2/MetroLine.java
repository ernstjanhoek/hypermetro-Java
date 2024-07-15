package metro.modelv2;

import java.util.Objects;

public class MetroLine {
    private final String name;

    public MetroLine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroLine metroLine = (MetroLine) o;
        return Objects.equals(name, metroLine.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
