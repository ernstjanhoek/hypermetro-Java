package metro.file;

public class WeightedStation extends Station {
    private final int time;
    public WeightedStation(String name, Transfer[] transfer, int time) {
        super(name, transfer);
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
