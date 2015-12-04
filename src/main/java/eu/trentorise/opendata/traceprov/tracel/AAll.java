package eu.trentorise.opendata.traceprov.tracel;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.geojson.Feature;

final class All extends Token {   

    private static final All INSTANCE = new All();

    @Override
    public String toText(){
        return "ALL";
    }
    
    public static All of(){
        return INSTANCE;
    }
}