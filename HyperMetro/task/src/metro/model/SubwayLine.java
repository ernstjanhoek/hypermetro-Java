package metro.model;

public class SubwayLine {
    private String lineName;
    private SubwayNode head;
    private SubwayNode tail;

    public SubwayLine(String name) {
        this.lineName = name;
        this.head = null;
        this.tail = null;
    }

    public SubwayNode getHead() {
        return head;
    }

    public void setHead(SubwayNode head) {
        this.head = head;
    }

    public SubwayNode getTail() {
        return tail;
    }

    public void setTail(SubwayNode tail) {
        this.tail = tail;
    }

    public void remove(String subwayName) {
        SubwayNode node = head;
        do {
            node = node.getNext();
            if (subwayName.equals(node.getStationName())) {
                SubwayNode previous = node.getPrevious();
                SubwayNode next = node.getNext();
                previous.setNext(next);
                next.setPrevious(previous);
            }
        } while (node.hasNext());
    }

    public void addAtTail(SubwayNode subwayNode) {
        tail.setNext(subwayNode);
        subwayNode.setPrevious(tail);
        tail = subwayNode;
    }

    public void addAtHead(SubwayNode subwayNode) {
        head.setPrevious(subwayNode);
        subwayNode.setNext(head);
        head = subwayNode;
    }

    public SubwayNode find(String name) {
        SubwayNode origin = this.head;
        while (true) {
            if (name.equals(origin.getStationName())) {
                return origin;
            }
            if (origin.hasNext()) {
                origin = origin.next();
            } else {
                return null;
            }
        }
    }

     public void printSubwayLine() {
        SubwayNode node = this.getHead();
        System.out.println("depot");
        System.out.println(node);
        while (node.hasNext()) {
            node = node.getNext();
            System.out.println(node);
        }
        System.out.println("depot");
    }


    public String getLineName() {
        return lineName;
    }
}
