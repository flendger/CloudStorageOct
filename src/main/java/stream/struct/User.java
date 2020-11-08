package stream.struct;

public class User {

    String name;
    int age;
    long id;
    UserPassport passport;

    public User(String name, int age,
                long id, UserPassport passport) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.passport = passport;
    }

    public UserPassport getPassport() {
        return passport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
