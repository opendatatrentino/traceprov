<p class="jedoc-to-strip">
WARNING: WORK IN PROGRESS - THIS IS ONLY A TEMPLATE FOR THE DOCUMENTATION. <br/>
RELEASE DOCS ARE ON THE <a href="http://opendatatrentino.github.io/traceprov/" target="_blank">PROJECT WEBSITE</a>
</p>

### Maven

TraceProv is available on Maven Central. To use it, put this in the dependencies section of your _pom.xml_:

```
<dependency>
    <groupId>eu.trentorise.opendata</groupId>
    <artifactId>traceprov</artifactId>
    <version>#{version}</version>
</dependency>
```

In case updates are available, version numbers follow <a href="http://semver.org/" target="_blank">semantic versioning</a> rules.

### Building objects

Most objects in TraceProv are immutable, and make heavy use of <a href="https://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained" target="_blank"> Guava immutable collections </a>. In TraceProv, wherever you see a class called `ASomething`, there will always be an immutable class `Something` implementing it. 



Immutable classes don't have public constructors, they only have  factory methods called _of()_. So for example, to create a _CellRef_ referring to a cell at row 1 and column 2 you would call:

```
CellRef cellRef = CellRef.of(1,2);
```

If the class has many fields, it will also provide a mutable Builder to create instances. So for example to build a _DcatDataset_ object setting a fields _title_ and _landingPage_ you would call:

```
DcatDataset dataset = DcatDataset
                .builder()                
                .setTitle(Dict.of(Locale.ITALIAN, "Impianti di risalita, ViviFiemme 2013"))
                .setLandingPage("http://dati.trentino.it/dataset/impianti-di-risalita-vivifiemme-2013")
                .build();

```
Builder is mutable, while dataset object created after build is perfectly immutable. 


### Type system

TODO total work in progress..

Note: we always use fully qualified names, here for the sake of brevity we use suffix `tt`
tt = eu.trentorise.opendata.traceprov.types

Main elements are `Type`, `ClassType`, `Def` and `RefType`.

#### Type
`Type` is a type expression, which doesn't have an associated name. A type can have parameters for generics.



##### Simple type example

Let's consider the integer type `tt.IntType`. First, notice it is a singleton:

```java
IntType intType = IntType.of();
```

The `typeId` field is a string with the traceprov fully qualified name, suitable use in programming languages (which here happens to be the same as the Java class for `IntType`):
```java
intType.getTypeId() == "eu.trentorise.opendata.traceprov.types.IntType"
```

In concept we store the high-level most standard definition that most closely describes the datatype:
```java
intType.getConcept().getId() == "http://www.w3.org/2001/XMLSchema#int"
intType.getConcept().getName() == "int"
intType.getConcept().getDescription() == "int is ·derived· from long by setting the value of ·maxInclusive· to be 2147483647 and ·minInclusive· to be -2147483648.  The ·base type· of int is long."
```


Instances of Java Integer can hold instances of IntType:

```java
abstract class IntType extends Type {
    @Override
    public Class getJavaClass(){
        return Integer.class;
    }
}
```



##### Parametric type example
Let's say we a have a list of integers:

```java
ListType stringList = ListType.of(IntType.of());
```

The typeId field is a string with the java class fully qualified name, WITHOUT eventual parameters as generics:
```java
stringList.getTypeId() == "eu.trentorise.opendata.traceprov.types.ListType"
```


We said Type expressions have no assigned name, but actually they can have a name and description for the datatype they represent:

```java
abstract class ListType extends Type {
    @Override
    public Dict getName(){
        return Dict.of(Locale.ENGLISH, "List of " + getSubtype().getName().str(Locale.ENGLISH);
    }

    @Override
    public Dict getDescription(){
        return Dict.of(Locale.ENGLISH, "Traceprov list type expression");
    }

}
```

#### ClassType
 A `ClassType` is a complex type expression made of properties and methods associated to them.  As class model we get inspiration from Typescript. Notice a class expression doesn't have an id, name or description, as these are assigned within a definition `Def`, see next paragraph.

```java
ClassType myPersonType = ClassType.builder()
                            .addProperty(
                                    Def.builder()
                                            .setName("age")
                                            .setType(IntType.of())
                                        .build())
                            .addProperty(
                                    Def.builder()
                                            .setName("name")
                                            .setType(StringType.of())
                                        .build())
                            )
                            .addMethod(Def.builder()
                                            .setName("live")
                                            .setType(Function..)
                                            .build())
                            )
                            .build()
``` 

The type id is always the ClassType as for simple types:
```java
personTypeExpr.getTypeId() == "eu.trentorise.opendata.traceprov.types.ClassType"
```

#### Definitions

A definition `Def` associates  a name and other semantic info like description to a type expression.

For example, the following definition associates `"com.mycompany.Person"` to class type expression:
`ClassType{age:tt.IntType, name : tt.StringType, live() : void}`

```java
Def<ClassType> myPersonDef = Def.<ClassType>builder()
							// id within traceprov for scripting. MUST be unique within
                            // the space of all def ids AND types id
							 .setId("com.mycompany.Person")
                             .setOriginId("http://mycompany.com/types/5462") // id as provided by my company web server
                             .setName(Dict.of(LOCALE.ENGLISH, "My company person"))
                             .setDescription(Dict.of(LOCALE.ENGLISH, "A brilliant person of my company"))
                             .setConcept(Concept  // hi level, loose but standard representation of our Person type
                             				.setId("https://schema.org/Person")
                                            .setName(Dict.of(Locale.ENGLISH, "Person")
                                            .setDescription("A person (alive, dead, undead, or fictional).")))
                             .setType(myPersonType)
                             .build();

```

#### RefType 
A reference to a type defined in a definition `Def`. For example, to reference the `Person` defined in the previous example, we would create a `RefType`:

```java
RefType myRef = RefType.of("com.mycompany.Person");

myRef.getDefId() == "com.mycompany.Person"
myRef.getTypeId() == "tt.RefType"
```

#### TypeContext

Maybe it can just be a simple

```java
Map<String, Def> defs;
get/has ClassType
get/has FunctionType
```


### Logging

TraceProv uses native Java logging system (JUL). If you have an application which uses SLF4J logging system and want to see TraceProv logs, you can route logging with <a href="http://mvnrepository.com/artifact/org.slf4j/jul-to-slf4j" target="_blank">JUL to SLF4J bridge</a>, just remember <a href="http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge" target="_blank"> to programmatically install it first. </a>