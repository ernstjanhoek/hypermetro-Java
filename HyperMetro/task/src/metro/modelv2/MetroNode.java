package metro.modelv2;

import metro.base.BaseNode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MetroNode extends BaseNode<String> {
    private final String line;
    private final Set<MetroNode> transfers = new HashSet<>();

    public MetroNode(String name, String line) {
        super(name);
        this.line = line;
    }

    public String getName() {
        return super.getValue();
    }

    public String getLine() {
        return this.line;
    }

    public void addTransfer(MetroNode transfer) {
        transfers.add(transfer);
    }

    public Set<MetroNode> getTransfers() {
        return transfers;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MetroNode metroNode = (MetroNode) o;
        return Objects.equals(line, metroNode.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), line);
    }
}
