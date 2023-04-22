# Question 1

What is the difference between a class and an object?

___

Answer:

___

# Question 2

Given are the following three classes:

`Person.java`:

```java
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printName() {
        System.out.println(name);
    }
}
```

`PersonModifier.java`:

```java
public class PersonModifier {

    public Person modifyPerson1(Person person) {
        person.setName("Bert");
        return person;
    }

    public Person modifyPerson2(Person person) {
        person.setName("Gerry");
        person = new Person("James");
        return person;
    }
}
```

`Main.java`:

```java
public class Main {

    public static void main(String[] args) {
        Person person1 = new Person("John");
        Person person2 = new Person("Bob");

        PersonModifier personModifier = new PersonModifier();
        Person modifiedPerson1 = personModifier.modifyPerson1(person1);
        Person modifiedPerson2 = personModifier.modifyPerson2(person2);

        person1.printName();
        person2.printName();
        modifiedPerson1.printName();
        modifiedPerson2.printName();
    }
}
```

What is the output of this program? Explain why.

___

Answer:

___
