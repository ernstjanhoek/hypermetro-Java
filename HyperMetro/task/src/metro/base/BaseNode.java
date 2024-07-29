package metro.base;

import java.util.Objects;

public class BaseNode<T> {
    public BaseNode(T value) {
        this.value = value;
    }

    private final T value;

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseNode<?> baseNode = (BaseNode<?>) o;
        return Objects.equals(value, baseNode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
