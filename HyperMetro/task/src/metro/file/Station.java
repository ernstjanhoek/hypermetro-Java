package metro.file;

public class Station {
    private final String name;
    private final Transfer[] transfer;

    public Station(String name, Transfer[] transfer) {
        this.name = name;
        this.transfer = transfer;
    }

    public Transfer[] getTransfer() {
        return transfer;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (Transfer transfer : transfer) {
            sb.append(" - ").append(transfer.toString());
        }
        return sb.toString();
    }
}
