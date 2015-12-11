
### Type system

TODO total work in progress..

Note: we always use fully qualified names, here for the sake of brevity we use suffix `tt`
tt = eu.trentorise.opendata.traceprov.types

Main elements are `Type`, `ClassType`, `Def` and `RefType`.

#### Type
`Type` is a type expression, which doesn't have an associated name. A type can have parameters to represent generics.



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

The `typeId` field is a string with the java class fully qualified name, WITHOUT eventual parameters as generics:
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

#### Definitions

**TODO OLD & WRONG**

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



#### ClassType
 A `ClassType` is a complex type expression made of properties and methods associated to them.  As class model we get inspiration from _Typescript_. Due to their particular nature, class expressions may also have an identifier. In the example we show the minimal identifiers required to form a class. More metadata such as natural language names, description, etc can be added to class and to each property and method.

```java
        ClassType myPersonType = ClassType
                .builder()
                .setId("org.mycompany.Person")
                .putPropertyDefs(
                        "age", // property name within the class, short, english and camelcased
                        Def
                        .builder()
                        // id for eventual condivision of property def with other types. 
                        .setId("org.mycompany.properties.age")
                        .setType(IntType.of())
                        .build())
                .putPropertyDefs(
                        "name",
                        Def
                        .builder()
                        .setId("org.mycompany.properties.name")
                        .setType(StringType.of())
                        .build())
                .putMethodDefs("walk",
                        Def
                        .builder()
                        .setId("org.mycompany.methods.walk")
                        .setType(FunctionType.of()) // todo add better example                                
                        .build())
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

**TODO** Maybe it can just be a simple

```java
Map<String, Def> defs;
get/has ClassType
get/has FunctionType
```


#### Conversions

##### Date conversions

```
"10/11/2014" -> Java Date (that is number of millisecs from 1970) 38293432489234
accuracy:	0.6
```

| candidates  | score |
| ------------|-------|
| 10 Nov 2014 | 0.4|
| 11 Oct 2014 | 0.6|


```
"10/11/2014" -> Java Date (that is number of millisecs from 1970) 38293432489234
accuracy: 0.6
```

| candidates  | score | accuracy|
| ------------|-------|---------|
| 10 Nov 2014 | 0.4   | 0.6  |
| 11 Oct 2014 | 0.6   | 0.6  |


##### Name enrichment as scored converion

```
"Trento" -> id

accuracy:	0.6
```

| candidates  | score |
| ------------|-------|
| 10 Nov 2014 | 0.4|
| 11 Oct 2014 | 0.6|


"Trento (TN)" -> id
accuracy: 0.3

```
"10/11/2014" -> Java Date (that is number of millisecs from 1970) 38293432489234
accuracy: 0.6
```

| candidates  | score | accuracy|
| ------------|-------|---------|
| 10 Nov 2014 | 0.4   | 0.6  |
| 11 Oct 2014 | 0.6   | 0.6  |


