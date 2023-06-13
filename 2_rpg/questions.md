# Question 1

In step 1, you were asked to create a `Room` class with a description, which will be printed if inspected. Two software developers proposed two different implementations for the `Room` class.

The first developer proposed one implementation:

```java
public class Room {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 
```

The second developer proposed another implementation:

```java
public class Room {
    public String description;
}
```

Both developers are discussing which implementation is better and why. Please answer the following question:

Which of these two implementations would you select? And why?

Justify your answer in at least 250 words. Please explain the consequences, benefits and drawbacks of each implementation and support it with an example.

___

Answer:

We believe that the first approach is the one that should be used. First of all, the fields of a class should be kept private and should only be interacted with using getters and setters. The reason for using getters and setters is because we can then easily implement further logic into them without further modifying the code in every single place which uses it. As an example, we may want to throw an exception if the value of the description is set to null, or we may want to validate some of its other properties. As such when using getters and setters we are able to guarantee certain properties about the values coming in and out. Another possible example would be to provide a default description for the room if it's null, which can be easily accomplished using an if statement, and thus guaranteeing that no NullPointerExceptions are thrown when calling methods on the return result. On the other hand, an argument can be made about the verbosity of getters and setters, as they require quite a bit of boilerplate, however this issue can be quite easily mitigated by some of the code generation tools provided by modern IDE's (IntelliJ, VSCode, Eclipse) or by tools such as Lombok which allow for the generation of this code at compile time using annotations. Taking all of this into account we recommend using the second approach (with Lombok added on top) as we believe it will provide the best balance when it comes to readability and offering full control over the flow of data.

___

# Question 2

In step 2, you are asked to create two interfaces: `Inspectable` and `Interactable`.
Interfaces by definition do not have implementations. They consist of method signatures:

```java
interface Inspectable {
    void inspect();
}
```

A software developer claims that interfaces are not useful, because they do not contain implementations. Thus, we should just use classes, and we do not need to define empty interfaces.

What do you think about this opinion? Do you agree or disagree with this opinion?

Please justify your answer in at least 250 words, and support your justifications with an example.

___

Answer:

First of all, it is safe to say that the software developer which claims that interfaces do not use implementations is wrong. That is because starting with Java 17, interfaces are given the option to provide what are called default implementations, which is done by marking the method with the default modifier and then providing an implementation as if a regular method is being added, thus making overriding the method optional. Second of all, even though abstract classes allow for fields to be placed in it, which can at some point be made available to a child class, they don't allow us to extend from multiple classes at once, whereas interfaces allow us to do that by separating the wanted interfaces with a comma. 

There is no such thing as a fair comparison between interfaces and abstract classes as they simply represent different concepts. Even though they both allow you to force (or provide) an implementation for a specific method method, they are built for vastly different purposes. Abstract classes are meant for full inheritance which gives access to all of the properties of the parent class and are thus better suited for subtypes of the parent, whereas interfaces are meant for additive behaviours (also called traits), which allows a developer to add different types of behaviour to a class without necesarily coupling said behaviour to other parts of the class, allowing us to compose classes which exhibit certain traits. 

As an example for a good interface (which also used in our project) would be Inspectable, which can be added on top of any class to allow the player to inspect said object.
As for abstract classes, an example from our project would be the enemy class, where classes which extend it are given the full behaviour of an enemy including it's health, damage and other statistics.

It should be noted that, although Java offers us multiple inheritance through interfaces and fields through abstract classes, it is not necesarily the only valid paradigm. As an example, C++ allows for multiple inheritance of classes, and handles name collisions (such as two parents having the same method name) by allowing the developer to specify the name of the class the implementation is being called from before the method itself.

___

# Question 3

To save your game state, you were asked to use Java classes `FileOutputStream` and `ObjectOutputStream`.
These two classes are part of the Java libraries, and they are designed based on a specific design pattern.

Which design pattern do `FileOutputStream` and `ObjectOutputStream` implement?

Explain the roles of this design pattern and how `FileOutputStream` and `ObjectOutputStream` implement it. Also explain the benefit of implementing this design pattern.

___

Answer:

The design pattern being exhibited by the FileOutputStream and ObjectOutputStream is the decorator pattern. This allows us to essentially wrap the FileOutputStream in an ObjectOutputStream which takes in objects rather than bytes, applies the Java serialization mechanism to convert what we give it to bytes, and then forwards it to the FileOutputStream under the hood.  

The advantages of implementing this design is that we are able to abstract away whatever our source may be (as we can provide any type of output stream to the ObjectOutputStream) and are able to simply worry about objects coming in and objects going out. 

The same design pattern is also shown when using ObjectInputStream and FileInputStream which allows us to reverse the process of serialization.

___
