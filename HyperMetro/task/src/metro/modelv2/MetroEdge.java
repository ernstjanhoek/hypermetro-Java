package metro.modelv2;

import metro.base.BaseEdge;
import java.util.Objects;

public class MetroEdge extends BaseEdge<MetroNode> {

    public MetroEdge(MetroNode origin, MetroNode destination) {
        super(origin, destination);
    }


    @Override
    public int hashCode() {
        return Objects.hash(getOrigin().getName(), getDestination().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroEdge metroEdge = (MetroEdge) o;
        return Objects.equals(getOrigin(), metroEdge.getOrigin()) &&
                Objects.equals(getDestination(), metroEdge.getDestination());
    }
}
