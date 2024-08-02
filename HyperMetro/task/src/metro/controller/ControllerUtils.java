package metro.controller;

import metro.file.Station;
import metro.modelv2.MetroNode;

import java.util.List;

public class ControllerUtils {
    static<T extends MetroNode> void printRoute(List<T> list) {
        if (list.isEmpty()) {
            return;
        }
        MetroNode[] array = list.toArray(new MetroNode[0]);
        for (int i = 0; i < array.length-1; i++) {
            System.out.println(array[i].getName());
            if (!array[i].getLine().equals(array[i+1].getLine())) {
                System.out.println("Transition to line " + array[i+1].getLine());
                System.out.println(array[i].getName());
            }
        }
        System.out.println(array[array.length - 1].getName());
    }

    static<T extends Station> void printLine(List<T> stationList) {
        System.out.println("depot");
        stationList.forEach(System.out::println);
        System.out.println("depot");
    }
}
