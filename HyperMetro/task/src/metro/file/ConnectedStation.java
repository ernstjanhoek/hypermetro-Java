package metro.file;

public class ConnectedStation {
    private final String name;
    private final String[] prev;
    private final String[] next;

    public int getTime() {
        return time;
    }

    public Transfer[] getTransfer() {
        return transfer;
    }

    public String[] getNext() {
        return next;
    }

    public String[] getPrev() {
        return prev;
    }

    public String getName() {
        return name;
    }

    private final Transfer[] transfer;
    private final int time;

    public ConnectedStation(String name, String[] prev, String[] next, Transfer[] transfer, int time) {
        this.name = name;
        this.prev = prev;
        this.next = next;
        this.transfer = transfer;
        this.time = time;
    }

}
