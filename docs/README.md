<p class="jedoc-to-strip">
WARNING: WORK IN PROGRESS - THIS IS ONLY A TEMPLATE FOR THE DOCUMENTATION. <br/>
RELEASE DOCS ARE ON THE <a href="http://opendatatrentino.github.io/traceprov/" target="_blank">PROJECT WEBSITE</a>
</p>

[TOC]

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

Immutable classes don't have public constructors, they only have  factory methods called `of()` or `builder()`. If the class has many fields, it will also provide a mutable Builder to create instances. So for example to build a _DcatDataset_ object setting a fields _title_ and _landingPage_ you would call:

```
DcatDataset dataset = DcatDataset
                .builder()                
                .setTitle(Dict.of(Locale.ITALIAN, "Impianti di risalita, ViviFiemme 2013"))
                .setLandingPage("http://dati.trentino.it/dataset/impianti-di-risalita-vivifiemme-2013")
                .build();

```
Builder is mutable, while dataset object created after build is perfectly immutable. 


### DCAT

<a href="http://www.w3.org/TR/vocab-dcat/" target="_blank">Dcat vocabulary</a> implementation in TraceProv tries to follow <a href=
  "https://joinup.ec.europa.eu/system/files/project/dcat-ap_version_1.1.pdf"  target="_blank">DCAT AP 1.1 specs</a>, adapting it to possibly dirty data. For spatial data we use <a href="http://geojson.org/" target="_blank">GeoJson</a> and for temporal data see <a href="https://github.com/opendatatrentino/tod-commons/wiki#dates-and-time" target="_blank">TOD Commons guidelines</a>. 

### Serialization

Serialization to/from Json is done with Jackson. In order to succesfully ser/deserialize TraceProv objects, you first need to register `TraceProvModule` and other needed modules in your own Jackson `ObjectMapper`:

```
	ObjectMapper om = new ObjectMapper();
	om.registerModule(new GuavaModule());
	om.registerModule(new TodCommonsModule());
	om.registerModule(new TraceProvModule()); 
        
	String json = om.writeValueAsString(
    					DcatDataset.builder()
                		.setTitle(Dict.of("hello"))
                .		build());
	System.out.println(json);
	DcatDataset reconstructedDataset = om.readValue(json, DcatDataset.class);
```

Notice we have also registered the necessary `Guava` (for immutable collections) and `Tod Commons` modules (for `Dict` and `LocalizedString`).
To register everything in one command just write:

```
	ObjectMapper om = new ObjectMapper();
	TraceProvModule.registerModulesInto(om);       
  
```



### Logging

TraceProv uses native Java logging system (JUL). If you have an application which uses SLF4J logging system and want to see TraceProv logs, you can route logging with <a href="http://mvnrepository.com/artifact/org.slf4j/jul-to-slf4j" target="_blank">JUL to SLF4J bridge</a>, just remember <a href="http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge" target="_blank"> to programmatically install it first. </a>