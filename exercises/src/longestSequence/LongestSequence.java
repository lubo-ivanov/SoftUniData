package longestSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LongestSequence {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Integer> list = Arrays.stream(scanner.nextLine().split("\s+")).map(Integer::parseInt).toList();
        List<Integer> result = new ArrayList<>();
        int currentCount = 0;
        int longestCount = 0;
        int current = 0;
        int previous;
        List<Integer> currentList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            previous = current;
            current = list.get(i);
            if (current == previous + 1) {
                currentCount++;
                currentList.add(current);
            } else {
                if (currentCount >= longestCount){
                    result.addAll(currentList);
                }
                currentCount = 0;
            }
        }


    }
}
