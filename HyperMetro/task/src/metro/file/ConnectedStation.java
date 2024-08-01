package metro.file;

public class ConnectedStation extends WeightedStation {
    private final String[] prev;
    private final String[] next;

    public String[] getNext() {
        return next;
    }

    public String[] getPrev() {
        return prev;
    }

    public ConnectedStation(String name, String[] prev, String[] next, Transfer[] transfer, int time) {
        super(name, transfer, time);
        this.prev = prev;
        this.next = next;
    }

}
