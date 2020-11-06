package stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Streams {

    public static void main(String[] args) throws IOException {
        Stream.iterate(1, i -> i * 2)
                .limit(6)
                .filter(a -> a > 30)
                .forEach(System.out::println);

        String res = IntStream.range(10, 1000)
                .filter(v -> v % 10 == 0 && v > 99)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        System.out.println(res);

        var list = Files.newBufferedReader(
                Path.of("src/main/java/stream/input.txt")).lines()
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .filter(s -> !s.isBlank())
                .map(s ->
                        s.replaceAll("[,«»/.\\]\\[()!?:]", "")
                                .toLowerCase())
                .filter(s -> !s.isBlank())
                .sorted(Comparator.reverseOrder())
                .distinct()
                .collect(Collectors.toList());

        System.out.println(list);

        Map<String, Integer> collect = Files.newBufferedReader(
                Path.of("src/main/java/stream/input.txt")).lines()
                .flatMap(line -> Arrays.stream(line.split(" +")))
                .dropWhile(String::isBlank)
                .map(s ->
                        s.replaceAll("[,«»/.\\]\\[()!?:]", "")
                                .toLowerCase())
                .dropWhile(String::isBlank)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toMap(Function.identity(), value -> 1, Integer::sum));

        System.out.println(collect);
        System.out.println(collect.get("в"));

        collect.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2 - o1))
                .forEach(entry -> {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                });

        ArrayList<ArrayList<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            lists.add(new ArrayList<>());
        }

        for (int i = 0; i < 35; i++) {
            int idx = (int) (Math.random() * 7);
            lists.get(idx).add((int) (Math.random() * 1000));
        }

        System.out.println(lists);

        List<Integer> integerList = lists.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        System.out.println(integerList);

    }
}
