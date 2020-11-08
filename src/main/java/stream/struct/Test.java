package stream.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            if (i > 3) {
                users.add(new User("name" + i, 15, i + 1,
                        new UserPassport("user#" + (i + 1),
                                null, "address" + i)));
            } else {
                users.add(new User("name" + i, 15, i + 1,
                        new UserPassport("lol#" + (i + 1),
                                null, "address" + i)));
            }
        }

        List<String> list = users.stream()
                .map(User::getPassport)
                .peek(pass -> pass.setPhoto(new byte[]{1, 2, 3, 4}))
                .filter(UserPassport::validateId)
                .map(UserPassport::getPassportId)
                .collect(Collectors.toList());

        System.out.println(list);

        boolean allValid = users.stream()
                .map(User::getPassport)
                .anyMatch(UserPassport::validateId);



        System.out.println(allValid);

        Optional<UserPassport> any = users.stream()
                .map(User::getPassport)
                .filter(UserPassport::validateId)
                .findAny();

        // 1 -> 2 -> 3 ->

        UserPassport stub = new UserPassport("1", null, null);

        any.orElse(stub);

        any.ifPresent(System.out::println);

        Stream.iterate(1, i -> i % 2)
        .forEach(System.out::println);


    }
}
