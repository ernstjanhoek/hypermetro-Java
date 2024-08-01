package metro.base;

import java.util.Objects;

public class BaseEdge<T extends BaseNode<?>> {
    public BaseEdge(T origin, T destination) {
        this.origin = origin;
        this.destination = destination;
    }

    private final T origin;
    private final T destination;

    public T getOrigin() {
        return origin;
    }

    public T getDestination() {
        return destination;
    }

    /**
     * Checks for equal nodes in this edge and other edges (parameter value)
     * If there are matching nodes in the edges. Method returns an optional new edge of the outer non equal nodes.
     */
    public BaseEdge<T> splice(BaseEdge<T> otherEdge) {
        if (this.getDestination().equals(otherEdge.getOrigin())) {
            return new BaseEdge<>(this.origin, otherEdge.getDestination());
        } else if (this.getOrigin().equals(otherEdge.getDestination())) {
            return new BaseEdge<>(this.destination, otherEdge.getOrigin());
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEdge<?> baseEdge = (BaseEdge<?>) o;
        return Objects.equals(origin, baseEdge.origin) && Objects.equals(destination, baseEdge.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination);
    }

    @Override
    public String toString() {
        return origin.toString() + " -- " + destination.toString();
    }
}
