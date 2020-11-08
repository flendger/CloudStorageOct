package stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lambdas {

    public static void main(String[] args) {

        Callback callback = System.out::println;
        callback.call("asdfasf");
        System.out.println(callback.getClass());

        Callback callback1 = a -> {
            System.out.println(a + "1");
        };

        // forEach, ifPresent
        Consumer<Integer> consumer = a -> {
            a = a + 1;
            System.out.println(a);
        };

        consumer = consumer.andThen(arg -> {
            arg = arg  * 2;
            System.out.println(arg);
        });

        consumer.accept(5);

        // filter, match
        Predicate<Integer> isEven = value -> value % 2 == 0;
        isEven = isEven.and(v -> v > 7);
        System.out.println(isEven.test(9));

        // map, flatMap
        Function<Integer, String> converter = "a"::repeat;

        System.out.println(converter.apply(5));

        Function<String, Integer> len = String::length;

        System.out.println(len.apply("15"));

        // collectors
        Supplier<List<Integer>> getList = ArrayList::new;

        System.out.println(getList.get());

        HashMap<String, HashMap<String, TreeSet<Integer>>> map = new HashMap<>();

    }
}
