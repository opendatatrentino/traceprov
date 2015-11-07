
#### 0.3.0

todo in progress

 - added geojson package
 - extracted packages eu.trentorise.opendata.commons into separate project [Tod Commons](https://github.com/opendatatrentino/tod-commons)
 - now inheriting from tod-super-pom
 - upgraded to immutables 2.x

BREAKING CHANGES:
- Renamed AbstractSomething classes to ASomething
- renamed dependency Odt commons into Tod commons, so classes and files starting with `Odt*` change to `Tod*`. Also `odt.commons.logging.properties` changed to `tod.commons.logging.properties` 

#### 0.2.0

19 January 2015

Implemented :
- DCAT classes
- provenance references
- tod commons (logging, config, Dict, ...)
