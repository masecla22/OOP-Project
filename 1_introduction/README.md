# A simple program

Writing your first program in a new programming language is always a big step to take. The ability to create a program, compile it, run it, then see its output shows that you set up everything correctly and that the programming language works, which is quite useful.

This exercise is, however, more than just a simple `Hello World!` (don't worry; your first print statement will still be `"Hello World!"`). You are going to write a small interactive program and we are going to guide you through (some of) it.

If there are still unclarities, please do not hesitate to contact your TA! The purpose of this assignment is to get you familiar with Git, Java and the general procedure of handing in your assignments.

## Before you begin

We highly recommend you read the reader before starting this assignment. More specifically, before you can start this assignment, you should carefully read `Tools & Environment` and follow the steps in the `Getting Started` section. Moreover, it would be beneficial to read through sections 8-12 of `OOP Concepts` as well. This information is important for understanding this assignment.

Before you begin you must do the following things:

1.  Set up Git. The first step is cloning the GitHub repository.
    An explanation about what to do can be found in the `Setting up Git` section of the Reader. In short, you should do the following:
    - Download and install Git on your machine;
    - Cloned your group's GitHub repository to your local machine using :
      ```bash
        git clone <link that you copied from GitHub> [directory]`
      ```

    There are a few steps you have to follow before you can start working on an assignment. This is also explained in the reader, but since this is assignment 1, we will shortly go over it:

    - The assignments are automatically pushed to a new branch in your group's GitHub repository. Each assignment branch will be named after said assignment. The branch for this assignment is called `introduction`.

    - Navigate to the directory of the assignment and make sure that you are on the `introduction` branch. You can do this with the command `git checkout introduction`. Now you are ready to start working on it. You can verify that you are on the correct branch by running `git status`.

2.  Set up IntelliJ. You can find more information about this in the Reader (although it is very straightforward).

For this assignment, start IntelliJ and choose `Open or Import`. This will open a file navigator. Navigate to the directory of the assignment on your PC, open the `pom.xml` file and select `Open as Project`. This will open and set up the project for you. Note that it may take some time for IntelliJ to fully set up Maven (you don't need to know the specifics; we use it primarily as a build tool). Once the project is set up, it should display this `README` file in IntelliJ. Since you most likely already have the `README` opened up somewhere else, you can close it in IntelliJ.

## Important

OOP is all about managing complexity, so good code quality is crucial. There are several automated checks performed on your commits that ensure that your code quality meets a certain standard. **Without passing these checks, your submission will not be accepted**. A failed check can be compared to passing 0 test cases on Themis. The checks are only performed on commits that are part of an open pull request. One additional check is performed to ensure that you submitted your pull request on time. Note that opening a pull request on time, but pushing commits after the deadline will still result in a failed check.

The performed checks are quite rigorous, so it is important that you frequently run `mvn clean compile`.
Details about which checks are performed can be found at the end of this document. This table also contains links to the documentation of each of the checks. The documentation often also contains examples of code that fail the check. Since the majority of the errors indicate the exact file and line number where the check was violated, you should be able to fix the vast majority of the errors produced by this automatic check very quickly. The usual exceptions to this are the checks on the file length and the method length since these often indicate deeper design issues. If you get stuck on a given check and don't know how to fix it (after reading the documentation), don't hesitate to ask your TA.

> Incorrect formatting can produce a lot of checkstyle violations. To prevent this, frequently use the `reformat code` functionality of IntelliJ.

To ensure the checks always run properly, you are not allowed to modify the `.checkstyle` and `.github` directories. Additionally, you are also not allowed to modify the `pom.xml` file. Making modifications to these files/directories will also result in your pull request being rejected.

> Pay attention that you don't make any accidental changes to them. Should this happen, please inform your TA and they will help you revert the file to a previous commit.

## 1. The Main class

After having set up IntelliJ, you can find the project tool window, which contains the directory tree, on the left. This directory tree provides an overview of the files in our project and will be quite useful down the line. Java programs are organized in classes, which can be seen as blueprints. An actual instance of such a class is called an object.

To start writing code, we first need to create such a class. Just as in C, we need a starting point for our application. This is done in the `Main` class. Note that the name of the class that contains the starting point for our application need not be `Main`. This `Main.java` file has already been provided to you, so there is no need to create it yourself.

We can add the following code to this file:

```Java
public class Main {

}
```

However, IntelliJ is already quite smart, so it will probably do this for you. Nevertheless, you should pay attention to what we just added. This is the basic template of a class in Java. When the Java compiler finds the keywords **public** and **class** together, it expects the name of that class (`Main` in this case) to be in a file called `Main.java`. A `public class Card` would be in a file called `Card.java`. In other words, the class name should match the file name exactly (note that capital letters should also match).

If this is your first time using IntelliJ, a blue bar will appear on your screen where IntelliJ tells us to set up a Software Development Kit (SDK) to use. IntelliJ should already come with its own runtime environment (based on OpenJDK) pre-installed, so you can simply use that one. Click on `Setup SDK`, choose `Java 17` and press OK. In some cases, it will prompt you with a menu containing a few options. In this case, select `Download JDK` and then `OpenJDK`.

Similar to C, we also define a main function. In object-oriented programming, functions inside classes are referred to as methods. We can do this inside the `Main` class by writing:

```Java
public static void main(String[] args) {

}
```

This is what the main method looks like in Java. Let's try to compile and run our program. We can run the program in IntelliJ by clicking on the green run button next to the main method we just defined. Alternatively, we could also run it from the command line. We can do this as follows:

- Compile your class with `mvn compile`, generating `Main.class` inside the `target/classes` directory;

  - Alternatively, you can compile your program using `javac -d target/classes -cp src/main/java src/main/java/nl/rug/oop/introduction/Main.java`;
	- Note that running with `mvn compile` might already produce some errors due to the checkstyle plugin. As such, it might be easier to run things via IntelliJ first.

- Run your program with: `java -cp target/classes nl.rug.oop.introduction.Main`.

More information about compilation can be found in `Tools & Environment` in the reader.

Great, we just wasted a few seconds on running an empty program. Let's fix that!

## 2. Hello World

Now we finally get to the good stuff: printing! Printing "Hello World!" in Java can be done by adding the following line to the main method:

```Java
System.out.println("Hello World!");
```

This will print the line that is provided in the arguments of `println(String)` to the standard output, appending the newline character `\n` to it.

OK great! However, for this assignment, we are going to do more than just printing "Hello World!" (so you can remove the previous print statement). We are going to create a very small program. In this program, we will model something very familiar.

## 3. Students

Start off by creating a class called `Student`. Each `Student` has a certain knowledge level. Has-a relationships are modelled in Java by adding fields. Add the following field to the `Student` class:

```java
private int knowledgeLevel;
```

This knowledge level can change when the student, for example, attends a lecture. As such, we want to add the behaviour to obtain knowledge to a `Student`. To do this, add the following method:

```java
public void obtainKnowledge() {
    knowledgeLevel = Math.min(knowledgeLevel + 1, 6);
}
```

This will increment the knowledge level each time, with a maximum knowledge level of 6.

Moreover, students also have names, so add a field for this as well. The constructor of a student should have the name and the knowledge level as parameters.

## 4. Lecturers

Now it is time to integrate lecturers into the program. Create a class `Lecturer` and give it a field for a name. The constructor of the `Lecturer` has the name as a parameter.

Lecturers are able to lecture (clearly). We'll make the very realistic assumption that any attending students always pay attention and that lecturers are always able to clearly explain the material. Consequently, all students that attend the lecture obtain knowledge.
Give the lecturer a method `lecture(List<Student> attendees)`. Note that the `List` interface is from another package, so we have to explicitly import this. IntelliJ will give a hint on how to do this, but you can also manually add `import java.util.List;` to the top of your class.

> The `List<Student>` notation specifies a `List` containing the type `Student`. Lists are very useful data structures that we will use frequently throughout the course. Lists provide various useful methods such as `add()` and `get()`.

In this method, something should be printed so that it appears that a lecture is being given (think of some fun text). Additionally, all attendees should obtain knowledge.

> A student and a lecturer have a lot in common. For example, they are both persons and both have a name. Can you think of a way to model this in your program?


## 5. Assignments

Next, we want to create a way to model assignments that students make. Create a class called `Assignment`. Each assignment has a name and a deadline. Add a constructor that takes both of these things as parameters.

Represent the deadline of an assignment by the following:

```java
private final LocalDateTime deadline;
```

## 6. Submissions

Students also need a way to make assignments and hand them in. Create a `Submission` class for this purpose. Each submission has a `quality` (represented by an `int`), a field that indicates when the submission was submitted (represented by a `LocalDateTime`) and a reference to the assignment this submission belongs to and the student who made the submission.

The `Assignment` and `Student` should be passed as constructor parameters. The `quality` should be initialized in the constructor of `Submission` by setting it equal to the `knowledgeLevel` of the student. The field that indicates when the submission was submitted can be created in the constructor using `LocalDateTime.now()`.

Now that we have a structure in place for assignments and submissions, we need a way for students to do their homework. Create a method in `Student` called `doAssignment` that takes as its input an `Assignment` and returns a new `Submission`. Find a suitable way to instantiate a `Submission` from the information in `Student`.

## 7. Teaching Assistants

Lastly, we need someone to grade all of the submissions. In our model, this is a `TeachingAssistant`. Add a method to the teaching assistant that grades a single submission. Grading should be done as follows:

- If the submission was submitted before the deadline, the grade is a random integer between the quality of the submission and 10. For this, you can use the `Random` class:

```java
    Random random = new Random();
    // Random number between submission quality and 10 (quality is at most 6)
    int grade = random.nextInt(5) + submission.getQuality();
```
- If the submission was submitted after the deadline, the grade is a 0.

For the sake of seeing what's going on in the program, print the grade, assignment name, student name and the teaching assistant's name to the console every time a submission is graded. Other than printing the grade, we don't do anything else with it.

As grading is usually done in batches, also create a method in `TeachingAssistant` to grade multiple submissions (a `List<Submission>`).

## 8. Instantiating classes and output testing

With all of our classes set up, it is now time to create a simple university course by using the classes we just created. Instantiate a lecturer, some students, and an assignment. Set a suitable deadline for the assignment, and instruct all the students to create a submission.

> As an example of how to create a new date: `LocalDateTime.of(2022, Month.APRIL, 29, 17, 0);` will create a date object for 2022 the 29th of April at 17:00.

Additionally, create a teaching assistant and instruct the TA to grade the list of submissions. Next, find out what happens in the following scenarios:

- What is the output that you see in the console?
- What if you change the deadline to a date in the past, do students still pass?
- What happens if students attend lectures of a lecturer? How does it affect their grades?

We do not expect you to make a report/analysis about this or anything, just make sure that your program behaves as expected in these scenarios. You don't need to explicitly answer these questions anywhere. Also, make sure to properly document your code. Documentation in Java is done via Javadoc. You can read more about this in the `Javadoc` subsection under the `Additional Information` section in the Reader.

> Make sure to run your program using `mvn clean compile` as well. This will perform the checks and inform you if there are any changes that you should be making to your code. It is very important that you are able to build your program using `mvn clean compile`, since your Pull Request will be rejected if the build fails. Before running this, make sure to use the `Reformat Code` functionality from IntelliJ on all the files you just created.

# Questions

For every assignment, there will be a number of questions that you have to answer. These can be found in a file called `questions.md`. The quality of your answers influences your grade. You should provide your answers in the `questions.md` file as well.

# Peer Evaluation

For us to get an idea of how the group work went, you need to fill in a peer evaluation quiz. In this quiz, you will evaluate both yourself and your partner. We will look at this quiz to determine whether there are any significant issues with the group work. As a general tip: communicate clearly and often with each other and try to work together in person whenever you can.

You can find this quiz on Brightspace. Filling in this quiz is mandatory, so don't forget it. The deadline for filling in this quiz is 24 hours after the deadline of the assignment.

# Handing in

That was it! By now, you should have a basic understanding of how classes and objects work. If there are still things unclear, we would highly advise you to go over this document again and read through the `OOP Concepts` section of the reader. You can also always ask your TA, of course (づ｡◕‿‿◕｡)づ.

Before creating a pull request, **make sure you can successfully compile your program with `mvn clean compile` locally**. Doing these checks on GitHub costs resources, so there is no reason to have a failing check there when you can also check it locally.

To upload your local code to Github (if you haven't already done so), navigate to the directory of this assignment and execute the following commands in your terminal:

- `git add .`
- `git commit -m "think of a useful commit message here"`
- `git push`

Your program should now be on GitHub. To actually hand it in, you have to create a pull request from `introduction` into the `main` branch, verify all the checks pass and you are done. You can do this by going to your repository on GitHub and selecting `New pull request`. Make sure you set the comparison correctly. It should look like this:

`base: main <- compare: introduction`

You can now leave a comment (if you want) and click on `Create pull request`. **Make sure that all checks have passed.** If not, your pull request will be rejected and it will not be a valid submission! If the checks don't pass, you can check why by clicking on the checks. Most likely you didn't run `mvn clean compile` beforehand. If this is the case, slap yourself first, fix the errors and commit & push again.

More information about this (with pictures!) can be found in the `Git & GitHub` section of the Reader.

# ❗❗⚠️⚠️ Important ⚠️⚠️❗❗

Because not all of you will have read the previous paragraph for some reason, we just want to stress this again because of how important it is.

A valid submission needs at least:

- a pull request from the correct branch into `main`.
- **all checks of that pull request to pass**

# Checkstyle

Below you can find a table with all of the checks performed by GitHub. Your code should pass all of these checks for your pull request to be accepted. You do not need to read through every single check, but you can use this table to quickly find the documentation when you encounter a given checkstyle violation. In addition to the checks below, it will also check whether your program actually compiles properly.

If you don't know how to fix a checkstyle error, make sure to read the documentation first (or Google), before asking your TA.

> One final warning: don't postpone running these checks until a few minutes before the deadline. It might take quite some time to fix all of them.

## General

| Performed Check  | Notes |
| ------------- | ------------- |
| [LineLength](https://checkstyle.sourceforge.io/config_sizes.html#LineLength) | Lines cannot be longer than 120 characters |
| [FileLength](https://checkstyle.sourceforge.io/config_sizes.html#FileLength) | Files cannot be longer than 600 lines. Note that this is the bare minimum. You should strive for 300-400 at most. Also note that this includes Javadoc. Without Javadoc, strive for at most 200.|

## Code Quality

| Performed Check  | Notes |
| ------------- | ------------- |
| [MethodLength](https://checkstyle.sourceforge.io/config_sizes.html#MethodLength) | Methods can have a maximum length of 40 lines. Note that this is the bare minimum. You should strive for a maximum of ~20 lines. |
| [LambdaBodyLength](https://checkstyle.sourceforge.io/config_sizes.html#LambdaBodyLength) | Lambda bodies can have a maximum size of 15 lines.  |
| [ArrayTypeStyle](https://checkstyle.sourceforge.io/config_misc.html#ArrayTypeStyle) |  |
| [EmptyBlock](https://checkstyle.sourceforge.io/config_blocks.html#EmptyBlock) |  |
| [LeftCurly](https://checkstyle.sourceforge.io/config_blocks.html#LeftCurly) |  |
| [RightCurly](https://checkstyle.sourceforge.io/config_blocks.html#RightCurly) |  |
| [NeedBraces](https://checkstyle.sourceforge.io/config_blocks.html#NeedBraces) |  |
| [DeclarationOrder](https://checkstyle.sourceforge.io/config_coding.html#DeclarationOrder) |  |
| [FallThrough](https://checkstyle.sourceforge.io/config_coding.html#FallThrough) |  |
| [IllegalCatch](https://checkstyle.sourceforge.io/config_coding.html#IllegalCatch) |  |
| [IllegalThrows](https://checkstyle.sourceforge.io/config_coding.html#IllegalThrows) |  |
| [IllegalToken](https://checkstyle.sourceforge.io/config_coding.html#IllegalToken) |  |
| [IllegalType](https://checkstyle.sourceforge.io/config_coding.html#IllegalType) |  |
| [InnerAssignment](https://checkstyle.sourceforge.io/config_coding.html#InnerAssignment) |  |
| [MultipleStringLiterals](https://checkstyle.sourceforge.io/config_coding.html#MultipleStringLiterals) |  |
| [NoClone](https://checkstyle.sourceforge.io/config_coding.html#NoClone) |  |
| [OneStatementPerLine](https://checkstyle.sourceforge.io/config_coding.html#OneStatementPerLine) |  |
| [PackageDeclaration](https://checkstyle.sourceforge.io/config_coding.html#PackageDeclaration) |  |
| [ReturnCount](https://checkstyle.sourceforge.io/config_coding.html#ReturnCount) |  |
| [SimplifyBooleanExpression](https://checkstyle.sourceforge.io/config_coding.html#SimplifyBooleanExpression) |  |
| [SimplifyBooleanReturn](https://checkstyle.sourceforge.io/config_coding.html#SimplifyBooleanReturn) |  |
| [StringLiteralEquality](https://checkstyle.sourceforge.io/config_coding.html#StringLiteralEquality) |  |
| [UnnecessarySemicolonAfterOuterTypeDeclaration](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonAfterOuterTypeDeclaration) |  |
| [UnnecessarySemicolonAfterTypeMemberDeclaration](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonAfterTypeMemberDeclaration) |  |
| [UnnecessarySemicolonInEnumeration](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonInEnumeration) |  |
| [UnnecessarySemicolonInTryWithResources](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonInTryWithResources) |  |
| [UnnecessaryCheckstyleLink](https://www.youtube.com/watch?v=dQw4w9WgXcQ) |  |
| [InnerTypeLast](https://checkstyle.sourceforge.io/config_design.html#InnerTypeLast) |  |
| [InterfaceIsType](https://checkstyle.sourceforge.io/config_design.html#InterfaceIsType) |  |
| [MutableException](https://checkstyle.sourceforge.io/config_design.html#MutableException) |  |
| [OneTopLevelClass](https://checkstyle.sourceforge.io/config_design.html#OneTopLevelClass) |  |
| [ModifierOrder](https://checkstyle.sourceforge.io/config_modifier.html#ModifierOrder) |  |
| [RedundantModifier](https://checkstyle.sourceforge.io/config_modifier.html#RedundantModifier) |  |
| [MissingOverride](https://checkstyle.sourceforge.io/config_annotation.html#MissingOverride) |  |
| [EmptyForInitializerPad](https://checkstyle.sourceforge.io/config_whitespace.html#EmptyForInitializerPad) |  |
| [EmptyForIteratorPad](https://checkstyle.sourceforge.io/config_whitespace.html#EmptyForIteratorPad) |  |
| [EmptyLineSeparator](https://checkstyle.sourceforge.io/config_whitespace.html#EmptyLineSeparator) |  |
| [GenericWhitespace](https://checkstyle.sourceforge.io/config_whitespace.html#GenericWhitespace) |  |
| [MethodParamPad](https://checkstyle.sourceforge.io/config_whitespace.html#MethodParamPad) |  |
| [TypecastParenPad](https://checkstyle.sourceforge.io/config_whitespace.html#TypecastParenPad) |  |
| [RedundantImport](https://checkstyle.sourceforge.io/config_imports.html#RedundantImport) |  |
| [UnusedImports](https://checkstyle.sourceforge.io/config_imports.html#UnusedImports) |  |
| [Indentation](https://checkstyle.sourceforge.io/config_misc.html#Indentation) |  |
| [CommentsIndentation](https://checkstyle.sourceforge.io/config_misc.html#CommentsIndentation) |  |
| [ConstantName](https://checkstyle.sourceforge.io/config_naming.html#ConstantName) |  |
| [DefaultComesLast](https://checkstyle.sourceforge.io/config_coding.html#DefaultComesLast) |  |
| [EmptyCatchBlock](https://checkstyle.sourceforge.io/config_blocks.html#EmptyCatchBlock) |  |
| [EqualsHashCode](https://checkstyle.sourceforge.io/config_coding.html#EqualsHashCode) |  |
| [InvalidJavadocPosition](https://checkstyle.sourceforge.io/config_javadoc.html#InvalidJavadocPosition) |  |
| [MethodName](https://checkstyle.sourceforge.io/config_naming.html#MethodName) |  |
| [NestedIfDepth](https://checkstyle.sourceforge.io/config_coding.html#NestedIfDepth) | Maximum of 3 nested if-statements. |
| [TypeName](https://checkstyle.sourceforge.io/config_naming.html#TypeName) |  |
| [ParameterNumber](https://checkstyle.sourceforge.io/config_sizes.html#ParameterNumber) | Maximum of 7 parameters. |
| [VisibilityModifier](https://checkstyle.sourceforge.io/config_design.html#VisibilityModifier) |  |
| [AvoidNoArgumentSuperConstructorCall](https://checkstyle.sourceforge.io/config_coding.html#AvoidNoArgumentSuperConstructorCall) |  |

## Javadoc

| Performed Check  | Notes |
| ------------- | ------------- |
| [AtclauseOrder](https://checkstyle.sourceforge.io/config_javadoc.html#AtclauseOrder) |  |
| [NonEmptyAtclauseDescription](https://checkstyle.sourceforge.io/config_javadoc.html#NonEmptyAtclauseDescription) |  |
| [JavadocStyle](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocStyle) |  |
| [SummaryJavadoc](https://checkstyle.sourceforge.io/config_javadoc.html#SummaryJavadoc) |  |
| [JavadocMethod](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocMethod) |  |
| [JavadocType](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocType) |  |
| [MissingJavadocType](https://checkstyle.sourceforge.io/config_javadoc.html#MissingJavadocType) |  |
| [MissingJavadocMethod](https://checkstyle.sourceforge.io/config_javadoc.html#MissingJavadocMethod) |  |
| [JavadocVariable](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocVariable) |  |
| [JavadocContentLocationCheck](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocContentLocation) |  |
| [JavadocBlockTagLocation](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocBlockTagLocation) |  |
| [JavadocMissingLeadingAsterisk](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocMissingLeadingAsterisk) |  |
| [JavadocMissingWhitespaceAfterAsterisk](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocMissingWhitespaceAfterAsterisk) |  |
