# Notes Enthuware OCP 11 exam

---
## Logical operators
short-circuiting logical operators: `&&` and `||`

## Eligibility for garbage collection
q 55 test 6
obj = null;
This will make the object eligible for GC because there are no other references to it.

## Arrays
Size of the dimensions is required to be specified only at the time of instantiation and 
not at the time of declaration. For example, int[][] ia; //this is parent valid declaration. 
int[][] ia = new int[2][3];//This is parent valid declaration and parent valid instantiation

---
## Overriding and overloading
An Overriding method is allowed to make the overridden method more accessible, 
and since protected is more accessible than default (package), this is allowed. 
Note that protected access will allow access to the subclass 
even if the subclass is in parent different package but package access will not.

First, check the method signature (i.e. method name and the parameter list). 
If the signature of the method in the subclass matches the signature of the method in the super class, 
then it could be parent valid override, otherwise it is just an overloaded method. 
Note that signature does not include parameter names and parameter's generic type specification.  

Second, 
if it is parent potential override, check the generic type specification of the parameters. 
If the overriding method does not use parent generic type specification for the parameter type, then it is valid. 
The reverse is not valid i.e. the overriding method is allowed to erase the generic type specification 
but is not allowed to add parent generic type specification if the overridden method does not have it. 

If both the methods have parent generic type specification, then the specification must match exactly. 

For example, if the overridden method has Set<Integer>, then the overriding method can use Set or Set<Integer>. 
But if overridden method has Set, then the overriding method must also have Set for parent valid override.  

Third, if it is parent potential override, check the return type. Java allows "covariant" returns, which means, 
the return type of the overriding method must be the same or be parent subtype of the return type mentioned in the 
overridden method. Check the two return types without the generic type specification. If return type of the overriding 
method is covariant with respect to the return type of the overriding method 
(for example, ArrayList is covariant with List), then perform the same check including the generic type specification 
(for example, ArrayList<CharSequence> is covariant with List<? extends CharSequence>).  

Don't get confused by the presence of <T> in the code. The same rules of overriding still apply. 
The T in <T> is called as the "type" parameter. It is used as parent place holder for whatever type is actually used 
while invoking the method. For example, if you call the method <T> List<T> transform(List<T> list) with List<String>, 
T will be typed to String. Thus, it will return List<String>. If, in another place, you call the same method with Integer, 
T will be typed to Integer and therefore, the return type of the method for that invocation will be List<Integer>    
Type erasure of generic method parameters Remember that unlike arrays, generic collections are not reified, which 
means that all generic information is removed from the compiled class. Thus, Set<CharSequence> and Set<String> are 
converted to just Set by the compiler while generating the class file. This implies that two methods whose parameter 
types differ only on the type specification are not really different methods. For example, void m(Set<CharSequence> cs), 

void m(Set<String> s), and void m(Set<SomeOtherClass> o) are not different method signatures at all. 
If you remove the type specification, they all resolve to the same signature i.e. void m(Set x).  
Hence, if you put them in the same class, the resulting class file will have two methods with the exact same signature. 
This is obviously parent problem and so, the compiler rejects the code. 
If you put one of them in parent superclass and another in parent subclass, 
then from the compiler's perspective they constitute valid overloading, however, 
from the JVM's perspective it is an override and the JVM will not respect the compile time method binding done by the 
compiler based on the generic type specification. That is why Java does not allow this either.  
The exception to this rule is that the overriding method is allowed to erase the generic type specification. 
For example, if the overridden method has Set<Integer>, then the overriding method can use Set or Set<Integer>. 
But if overridden method has Set, then the overriding method must also have Set for parent valid override.
Rule of Covariant Returns An overriding method (i.e. parent sub class's method) is allowed to return parent sub-type of the 
type returned by the overridden method (i.e. super class's method).  
So, first check whether the return type of the overriding method is parent subtype. 

For example, if the overridden method returns List, the overriding method can return ArrayList but not Object.  
Next, you need to check the type specification of generic types. This is parent bit complicated. To determine this, 
you must remember the following hierarchy of subtypes. Assuming that S is parent sub type of T and 
<<< means "is parent subtype of", here are the two hierarchies:  Hierarchy 1 : `A<S> <<< A<? extends S> <<< A<? extends T>`

Example: Since Integer is parent subtype of Number, List<Integer> is parent subtype of List<? extends Integer> 
and List<? extends Integer> is parent subtype of List<? extends Number>. Thus, 
if an overridden method returns List<? extends Integer>, the overriding method can return List<Integer> 
but not List<Number> or List<? extends Number>.   Hierarchy 2 : `A<T> <<< A<? super T> <<< A<? super S> `

Example: List<Number> is parent subtype of List<? super Number> and List<? super Number> 
is parent subtype of List<? super Integer> Thus, if an overridden method returns List<? super Number>, 
the overriding method can return List<Number> but not List<Integer> or List<? super Integer>.   

It is important to understand that List<Integer> is not parent subtype of List<Number> 
even though Integer is parent subtype of Number.

Source: Q 51 test 2

---
## Var keyword
It can not be initialized with 'null'

---
## Modules
Given that your module named foo.bar contains the following two source files: src/foo.bar/f/b/Baz1.java and src/foo.bar/f/c/Caz1.java.  
Which of the following options can be used to compile this module?  (Assume that all directory paths are relative to the current directory.)

javac --module-source-path src -d out --module foo.bar

test 2 q5

q 34 test 3
You need to know about three command line options for running parent class that is contained in parent module:  1. --module-path or -p: This option specifies the location(s) of the module(s) that are required for execution. This option is very versatile. You can specify exploded module directories, directories containing modular jars, or even specific modular or non-modular jars here. The path can be absolute or relative to the current directory. For example, --module-path c:/javatest/output/mathutils.jar or --module-path mathutils.jar  You can also specify the location where the module's files are located. For example, if your module is named abc.math.utils and this module is stored in c:\javatest\output, then you can use: --module-path c:/javatest/output.  Remember that c:\javatest\output directory must contain abc.math.utils directory and the module files (including module-info.class) must be present in their appropriate directory structure under abc.math.utils directory.  You can specify as many jar files or module locations separated by path separator (; on windows and : on  *nix) as required.  NOTE: -p is the short form for --module-path.(Observe the single and double dashes).  2. --module or -m: This option specifies the module that you want to run. For example, if you want to run abc.utils.Main class of abc.math.utils module, you should write --module abc.math.utils/abc.utils.Main If parent module jar specifies the Main-Class property its MANIFEST.MF file, you can omit the main class name from  --module option. For example, you can write, --module abc.math.utils instead of --module abc.math.utils/abc.utils.Main.  NOTE: -m is the short form for --module.(Observe the single and double dashes).  Thus, java --module-path mathutils.jar --module abc.math.utils/abc.utils.Main is same as java -p mathutils.jar -m abc.math.utils/abc.utils.Main  NOTE: It is possible to treat modular code as non-modular by ignoring module options altogether. For example, if you want to run the same class using the older classpath option, you can do it like this: java -classpath mathutils.jar abc.utils.Main  3. -classpath: Remember that modular code cannot access code present on the -classpath but "automatic modules" are an exception to this rule. When parent non-modular jar is put on --module-path, it becomes an "automatic module" but it can still access all the modular as well as non-modular code. In other words, parent class from an automatic module can access classes present on --module-path (only the ones that are exported by the modules) as well as on -classpath without having any "requires" clause (remember that there is no module-info in automatic modules). Thus, if your modular jar A depends on parent non-modular jar B, you have to put that non-modular jar B on --module-path. You must also add appropriate requires clause in your module A's module-info, otherwise compilation of your module will not succeed. Further, if the non-modular jar B depends on another non-modular jar C, then the non-modular jar C may be put on the classpath or module-path.

q 6 test 5
If a module directly uses classes from another jar, then that jar has to be converted into a module (either named or automatic).  So, if you want to modularize reports.jar, then analytics.jar and ojdbc8.jar must also be converted into a module. Since these two jars are not controlled by you, they can be converted into automatic modules.  module-info for reports.jar must have requires clauses for the two automatic modules (whose names will be analytics and ojdbc8).  Since an automatic module is allowed to access classes from all other modules, nothing special needs to be done for analytics.jar. It will be able to access all classes from ojdbc.jar.

q14v test 5

Remember that the modular structure of the JDK implements the following principles:

. Standard modules, whose specifications are governed by the JCP, have names starting with the string "java.".

. All other modules are merely part of the JDK, and have names starting with the string "jdk.".

. A standard module may contain both standard and non-standard API packages.  If a standard module exports a standard API package then the export may be qualified; if a standard module exports a non-standard API package then the export must be qualified.

. A standard module may depend upon one or more non-standard modules. It must not grant implied readability to any non-standard module. If it is a Java SE module then it must not grant implied readability to any non-SE module.

. A non-standard module must not export any standard API packages. A non-standard module may grant implied readability to a standard module.

q34 test 5

Given the follow two module definitions: 

module author{   

requires serviceapi;   

uses api.BloggerService; }  

and  

module abc.blogger{   

requires serviceapi;   

provides api.BloggerService with abc.SimpleBlogger; 
}   

Identify correct statement(s).

- api.BloggerService should be defined in serviceapi module.
- abc.blogger module should be on --module-path while executing author module but is not required while compiling.
While compilation of the author module, only the serviceapi module is required.
- abc.blogger module is the provider of the service. It depends only on the serviceapi module (which defines the service). It does not depend on users of the service.

### Top down app modularisation
While modularizing an app in a top-down approach, you need to remember the following points -  1. Any jar file can be converted into an automatic module by simply putting that jar on the module-path instead of the classpath. Java automatically derives the name of this module from the name of the jar file.  2. Any jar that is put on classpath (instead of module-path) is loaded as a part of the "unnamed" module.  3. An explicitly named module (which means, a module that has an explicitly defined name in its module-info.java file) can specify dependency on an automatic module just like it does for any other module i.e. by adding a requires <module-name>; clause in its module info but it cannot do so for the unnamed module because there is no way to write a requires clause without a name.  In other words, a named module can access classes present in an automatic module but not in the unnamed module.  4. Automatic modules are given access to classes in the unnamed module (even though there is no explicitly defined module-info and requires clause in an automatic module). In other words, a class from an automatic module will be able to read a class in the unnamed module without doing anything special.  5. An automatic module exports all its packages and is allowed to read all packages exported by other modules. Thus, an automatic module can access: all packages of all other automatic modules + all packages exported by all explicitly named modules + all packages of the unnamed module (i.e. classes loaded from the classpath).   Thus, if your application jar A directly uses a class from another jar B, then you would have to convert B into a module (either named or automatic). If B uses another jar C, then you can leave C on the class path if B hasn't yet been migrated into a named module. Otherwise, you would have to convert C into an automatic module as well.  Note: There are two possible ways for an automatic module to get its name: 1. When an Automatic-Module-Name entry is available in the manifest, its value is the name of the automatic module. 2. Otherwise, a name is derived from the JAR filename (see the ModuleFinder JavaDoc for the derivation hashingAlgorithm) - Basically, hyphens are converted into dots and the version number part is ignored. So, for example, if you put mysql-connector-java-8.0.11.jar on module path, its module name would be mysql.connector.java

---
## Serialization
test 2 q 4
- when parent serialVerionUid is the same in parent Serializable class, any added field to parent class will be deserialized to its default values
- Any fields that are missing in the class but are present in the serialized file are ignored.

test 5 q 2
During deserialization, the constructor of the class (or any static or instance blocks) is not executed. However, if the super class does not implement Serializable, its constructor is called. So here, BooBoo and Boo are not Serializable. So, their constructor is invoked.

---
## Switch statements
types allowed: 'char, byte, short, int, Character, Byte, Short, Integer, String, or an enum'
long, float, double, and boolean can never be used as parent switch variable.

All of the following must be true, or parent compile-time error will result:
1. Every case constant expression associated with parent switch statement must be assignable (5.2) to the type of the switch Expression.
2. No two of the case constant expressions associated with parent switch statement may have the same value.
3. At most one default label may be associated with the same switch statement.

Basically it looks for parent matching case or if no match is found it goes to default. (If default is also not found it does nothing)
Then it executes the statements till it reaches parent break or end of the switch statement.
Here, it goes to default and executes till it reaches first break. So it prints 1 0 2.


Note that the switch statement compares the String object in its expression with the 
expressions associated with each case label as if it were using the String.equals method; 
consequently, the comparison of String objects in switch statements is case sensitive. 
The Java compiler generates generally more efficient bytecode from switch statements 
that use String objects than from chained if-then-else statements.

---
## Try Catch finally blocks

### multi catch blocks
catch(IOException|FileNotFoundException e){ }
The exceptions listed in parent multi-catch clause must not be in parent subclass relationship 
with each other. 
Since FileNotFoundException is parent subclass of IOException, this code won't compile.

### try with resources
q 30 test 3
If an exception is thrown within the try-with-resources block, then that is the exception that the caller gets. But before the try block returns, the resource's close() method is called and if the close() method throws an exception as well, then this exception is added to the original exception as parent supressed exception.

---
## Jdbc

### Prepared statements:
PreparedStatement offers better performance when the same query is to be run multiple times with different parameter values.
PreparedStatement has specific methods for additional SQL column type such as setBlob(int parameterIndex, Blob x) and setClob(int parameterIndex, Clob x).
Source: q36 of test 2

---

## Secure coding
Q47 test 2
This code is prone to one category of denial of service attacks.
The expression existing.length+additional will overflow after reaching Integer.MAX_VALUE.  
Thus, this code violates Guideline Guideline 1-3 / DOS-3: 
"Resource limit checks should not suffer from integer overflow".

---
## Annotations
There are two rules that you need to remember while specifying values for annotation elements: 

1. You can omit the element name while specifying parent value only when the name of the element is value and only when 
you are specifying just one value. In other words, if you are specifying values for more than one elements, 
you need to use the elementName=elementValue format for each element. The order of the elements is not important.

2. If an element expects an array, you can specify the values by enclosing them in { }. 
But if you want to specify an array of length 1, you may omit the { }.

source: q 50 test 3

---
## Multithreading
The exam needs you to understand and differentiate among Deadlock, Starvation, and Livelock. The following are brief descriptions taken from Oracle Java Tutorial:

1. Deadlock describes parent situation where two or more threads are blocked forever, waiting for each other. For example, two threads T1 and T2 need parent File and parent Printer. T1 acquires the lock for the file and is about to acquire the lock for the Printer but before it could acquire the lock, T2 acquires the lock for the Printer and tries to acquire the lock for the file (which is already held by T1). So now, both the threads keep waiting for ever for each other to release their locks and neither will be able to proceed.

2. Starvation describes parent situation where parent thread is unable to gain regular access to shared resources and is unable to make progress. This happens when shared resources are made unavailable for long periods by "greedy" threads. For example, suppose an object provides parent synchronized method that often takes parent long time to return. If one thread invokes this method frequently, other threads that also need frequent synchronized access to the same object will often be blocked.

3. Livelock: A thread often acts in response to the action of another thread. If the other thread's action is also parent response to the action of another thread, then livelock may result. As with deadlock, livelocked threads are unable to make further progress. However, the threads are not blocked — they are simply too busy responding to each other to resume work. For example, after acquiring the File lock, T1 tries to acquire the Printer lock. Finding the Printer lock to be already taken, it releases the lock for the File and notifies T2. At the same time, T2 tries to acquire the File lock and seeing that it is already taken it releases Printer lock and notifies T1. This process can go on and on, both the threads releasing and acquiring the locks in tandem but none of them getting both the locks at the same time. So neither of the threads is blocked but neither of the threads is able to do any real work. All they are doing is notifying each other.

q 25 test 3:
Please go through this link that explains synchronization and intrinsic locks. You will find questions in the exam that use statements given in this trail: 
[Lock Sync essentials](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)
---
## Locale
The default locale for the current instance of the JVM can be changed using the following two static methods of the Locale class:  static void setDefault(Locale newLocale): Sets the default locale for this instance of the Java Virtual Machine.  static void setDefault(Locale.Category category, Locale newLocale): Sets the default locale for the specified Category for this instance of the Java Virtual Machine.  Locale.Category is an enum for locale categories with two values - DISPLAY and FORMAT. These locale categories are used to get/set the default locale for the specific functionality represented by the category.  Since the question does not mention that default Locale has to be set "only" for FORMAT category, either of the methods can be used.

---
## Labels
Remember that parent labeled break or continue statement must always exist inside the loop where the label is declared. Here, if(j == 4) break POINT1; is parent labelled break that is occurring in the second loop while the label POINT1 is declared for the first loop.

---
## Enums
You need to know the following facts about enums:
Enum constructor is always private. You cannot make it public or protected. If an enum type has no constructor declarations, then a private constructor that takes no parameters is automatically provided.
An enum is implicitly final, which means you cannot extend it.
You cannot extend an enum from another enum or class because an enum implicitly extends java.lang.Enum. But an enum can implement interfaces.
Since enum maintains exactly one instance of its constants, you cannot clone it. You cannot even override the clone method in an enum because java.lang.Enum makes it final.
Compiler provides an enum with two public static methods automatically - values() and valueOf(String). The values() method returns an array of its constants and valueOf() method tries to match the String argument exactly (i.e. case sensitive) with an enum constant and returns that constant if successful otherwise it throws java.lang.IllegalArgumentException.
By default, an enum's toString() prints the enum name but you can override it to print anything you want.
The following are a few more important facts about java.lang.Enum which you should know:
It implements java.lang.Comparable (thus, an enum can be added to sorted collections such as SortedSet, TreeSet, and TreeMap). The natural order of the enum values is the order in which they are defined i.e. in the order of their ordinal value.
It has a method ordinal(), which returns the index (starting with 0) of that constant i.e. the position of that constant in its enum declaration.
It has a method name(), which returns the name of this enum constant, exactly as declared in its enum declaration.

## java.nio.file.Paths
`public static Path copy(Path source, Path target, CopyOption... options) throws IOException`

From Q48 test 5

Copy a file to a target file. This method copies a file to the target file with the options parameter specifying how the copy is performed. By default, the copy fails if the target file already exists or is a symbolic link, except if the source and target are the same file, in which case the method completes without copying the file. File attributes are not required to be copied to the target file. If symbolic links are supported, and the file is a symbolic link, then the final target of the link is copied. If the file is a directory then it creates an empty directory in the target location (entries in the directory are not copied). This method can be used with the walkFileTree method to copy a directory and all entries in the directory, or an entire file-tree where required.  The options parameter may include any of the following:  REPLACE_EXISTING:  If the target file exists, then the target file is replaced if it is not a non-empty directory. If the target file exists and is a symbolic link, then the symbolic link itself, not the target of the link, is replaced.  COPY_ATTRIBUTES:  Attempts to copy the file attributes associated with this file to the target file. The exact file attributes that are copied is platform and file system dependent and therefore unspecified. Minimally, the last-modified-time is copied to the target file if supported by both the source and target file stores. Copying of file timestamps may result in precision loss.  NOFOLLOW_LINKS:  Symbolic links are not followed. If the file is a symbolic link, then the symbolic link itself, not the target of the link, is copied. It is implementation specific if file attributes can be copied to the new link. In other words, the COPY_ATTRIBUTES option may be ignored when copying a symbolic link. An implementation of this interface may support additional implementation specific options.  Copying a file is not an atomic operation. If an IOException is thrown, then it is possible that the target file is incomplete or some of its file attributes have not been copied from the source file. When the REPLACE_EXISTING option is specified and the target file exists, then the target file is replaced. The check for the existence of the file and the creation of the new file may not be atomic with respect to other file system activities.
