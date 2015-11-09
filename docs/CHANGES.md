
#### 0.3.0

November 9th, 2015

- improved dcat models following DCAT AP 1.1 (adapted for dirty data)
- added geojson package
- better defined temporal data
- now requiring at least java 7
- upgraded
	* immutables to 2.1.0
	
- extracted packages eu.trentorise.opendata.commons into separate project [Tod Commons](https://github.com/opendatatrentino/tod-commons)
 - now inheriting from tod-super-pom	

- shaded apache http client

BREAKING CHANGES:
- Renamed AbstractSomething classes to ASomething
- renamed dependency Odt commons into Tod commons, so classes and files starting with `Odt*` change to `Tod*`. Also `odt.commons.logging.properties` changed to `tod.commons.logging.properties` 
- removed joda classes from api, now using simple String or todutils APeriodOfTime

#### 0.2.0

19 January 2015

Implemented :
- DCAT classes
- provenance references
- tod commons (logging, config, Dict, ...)
