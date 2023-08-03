MiniJava Static Checking (Semantic Analysis)

Some generall information about mini java:
MiniJava is fully object-oriented, like Java. It does not allow global functions, only classes, fields and methods. The basic types are int, boolean, int [] which is an array of int, and boolean [] which is an array of boolean. You can build classes that contain fields of these basic types or of other classes. Classes contain methods with arguments of basic or class types, etc.

MiniJava supports single inheritance but not interfaces. It does not support function overloading, which means that each method name must be unique. In addition, all methods are inherently polymorphic (i.e., “virtual” in C++ terminology). This means that foo can be defined in a subclass if it has the same return type and argument types (ordered) as in the parent, but it is an error if it exists with other argument types or return type in the parent. Also all methods must have a return type--there are no void methods. Fields in the base and derived class are allowed to have the same names, and are essentially different fields.

All MiniJava methods are “public” and all fields “protected”. A class method cannot access fields of another class, with the exception of its superclasses. Methods are visible, however. A class's own methods can be called via “this”. E.g., this.foo(5) calls the object's own foo method, a.foo(5) calls the foo method of object a. Local variables are defined only at the beginning of a method. A name cannot be repeated in local variables (of the same method) and cannot be repeated in fields (of the same class). A local variable x shadows a field x of the surrounding class.
In MiniJava, constructors and destructors are not defined. The new operator calls a default void constructor. In addition, there are no inner classes and there are no static methods or fields. By exception, the pseudo-static method “main” is handled specially in the grammar. A MiniJava program is a file that begins with a special class that contains the main method and specific arguments that are not used. The special class has no fields. After it, other classes are defined that can have fields and methods.
Notably, an A class can contain a field of type B, where B is defined later in the file. But when we have "class B extends A”, A must be defined before B. As you'll notice in the grammar, MiniJava offers very simple ways to construct expressions and only allows < comparisons. There are no lists of operations, e.g., 1 + 2 + 3, but a method call on one object may be used as an argument for another method call. In terms of logical operators, MiniJava allows the logical and ("&&") and the logical not ("!"). For int and boolean arrays, the assignment and [] operators are allowed, as well as the a.length expression, which returns the size of array a. We have “while” and “if” code blocks. The latter are always followed by an “else”. Finally, the assignment "A a = new B();" when B extends A is correct, and the same applies when a method expects a parameter of type A and a B instance is given instead

Two visitors are used for the semantic analysis. The first one is in Main.java and the second one is in SecondVisitor.java

The first visitor is used for the following:
1) Store the classes declarations
2) Store the variables and methods declarations
3) Check for redeclaration errors
4) Typecheck the program

The second visitor is used for the following:
1) Check the statements (typcheck etc)
2) Check the assignments
3) Do additional typecheking
4) Based on the variable method_mode the second visitor adapts the checks according to whether it visits a method or a class
5) Sends to the Symboltable S the data it needs 

The symbolm table (check SymbolTable.java) is used for the following:
1) Store the data in hasmaps
2) Provide public methods for the visitors and the table itself to use for typecheck and data storage
3) For methods management the Method node class is implemented inside the symbol table which stores the data of each method in a method_node object and provides various functions for data selection and checking

Use make all to compile the project
Use make clean to clean the binaries and .class files
Execute as follows (being in minijava_example folder):
java [Main] [file1] [file2] ... [fileN]

Execute examples (being in minijava_example folder):<br /> 
java Main  ./inputs/UseArgs.java <br /> 
java Main ./inputs/TreeVisitor.java Example.java <br /> 

The results are printed in the standard output and there are separators for each file, class, class methods, class variables

This project was Implemented for Compilers class in 2022  Department of Informatics and Telecommunications of the University of Athens . Professor: Yannis Smaragdakis
