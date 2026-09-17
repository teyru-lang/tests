import java.util.ArrayList;
import java.util.List;

record Person(String name, int age) {}

public class comparator_sort {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>(List.of(
                new Person("bob", 30),
                new Person("alice", 25),
                new Person("carol", 41),
                new Person("alice", 20),
                new Person("bob", 22)));

        List<Person> byNameAge = new ArrayList<>(people);
        byNameAge.sort(java.util.Comparator.comparing(Person::name).thenComparing(Person::age));
        for (Person p : byNameAge) System.out.println(p.name() + " " + p.age());

        List<Person> byAgeDesc = new ArrayList<>(people);
        byAgeDesc.sort(java.util.Comparator.comparing(Person::age).reversed());
        for (Person p : byAgeDesc) System.out.println("desc " + p.name() + " " + p.age());

        List<Person> byNameOnly = new ArrayList<>(people);
        byNameOnly.sort(java.util.Comparator.comparing(Person::name));
        for (Person p : byNameOnly) System.out.println("name " + p.name() + " " + p.age());
    }
}
