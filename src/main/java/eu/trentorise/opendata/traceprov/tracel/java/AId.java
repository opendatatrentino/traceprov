package eu.trentorise.opendata.traceprov.tracel.java;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.engine.Engine;
import eu.trentorise.opendata.traceprov.geojson.Feature;
import eu.trentorise.opendata.traceprov.tracel.java.Id;

/**
 * 
 * TODO COMPLETE THIS CLASS
 *
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = Id.class)
@JsonDeserialize(as = Id.class)
abstract class AId extends Token {

    /**
     * The property, like "myProp" or "My Property". By default is the value
     * "this".
     */
    @Value.Default
    public String getLabel() {
        return "this";
    }

    @Value.Check
    protected void check(){        
        if (!Engine.isValidId(getLabel())){
            throw new IllegalStateException("Token Id " + getLabel() + " is not valid!");
        }
    }

    @Override
    public String toText(){
        return getLabel();
    }
}