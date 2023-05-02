# Question 1

What is the difference between a class and an object?

___

Answer:

The class is essentially like the template for the object. It defines the fields and methods that the object will have. The object is an instance of the class. It is the actual thing that is created in memory.

As an example, if we have
```java
String someValue = "Hello!";
```

`String` is the class and `someValue` is the object.

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

The output is as follows:
```
Bert
Gerry
Bert
James
```

The reason for that is as follows. There's actually 3 Person objects being created. The first two are created in the main method, and are then modified by the `PersonModifier` class. The third is created in the `modifyPerson2` method of the `PersonModifier` class and is then returned. That means that both `person1` and `modifiedPerson1` are referrences to the same object (which was modified by `modifyPerson1`). Whereas `person2` and `modifiedPerson2` are referrences to different objects (the former was modified by `modifyPerson2` and the latter was created in `modifyPerson2`). 

As such we first see the name of the `person1`. Then we see the name of `person2`. Then we see the name of `modifiedPerson1` which is the same as `person1` because they are referrences to the same object. Finally we see the name of `modifiedPerson2` which is different from `person2` because they are referrences to different objects.

___
