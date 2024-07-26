import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Queue<Person> queue = createQueue(scanner);

        System.out.println(queue.poll().getName());
        System.out.println(queue.poll().getName());
        System.out.println(queue.poll().getName());

    }

    public static Queue<Person> createQueue(Scanner scanner) {
        Queue<Person> queue = new PriorityQueue(new PersonComparator());
        queue.add(new Person(scanner.nextLine()));
        queue.add(new Person(scanner.nextLine()));
        queue.add(new Person(scanner.nextLine()));

        return queue;
    }
}

class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}

class PersonComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getName().compareTo(p2.getName());
    }
}