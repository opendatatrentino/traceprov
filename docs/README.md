### Introduction

TraceProv is a Java library to model provenance of data from DCAT catalogs, which are represented using <a href="http://www.w3.org/TR/vocab-dcat/" target="_blank">DCAT W3C vocabulary</a>. At present also allows to reference data in sources having table-like formats such as CSV. 

### Usage

TraceProv is available on Maven Central. To use it, put this in the dependencies section of your _pom.xml_:

```
<dependency>
    <groupId>eu.trentorise.opendata</groupId>
    <artifactId>traceprov</artifactId>
    <version>#{version}</version>            
</dependency>   
```

In case updates are available, version numbers follow [semantic versioning](http://semver.org/) rules.

#### The API

Most objects in TraceProv are immutable, and make heavy use of <a href="https://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained" target="_blank"> Guava immutable collections </a>. In TraceProv, wherever you see a class called 'AbstractSomething', there will always be an immutable class 'Something' implementing it. 

##### Building objects

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

##### Logging

TraceProv uses native Java logging system (JUL). If you have an application which uses SLF4J logging system and want to see TraceProv logs, you can route logging with <a href="http://mvnrepository.com/artifact/org.slf4j/jul-to-slf4j" target="_blank">JUL to SLF4J bridge</a>, just remember <a href="http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge" target="_blank"> to programmatically install it first. </a>