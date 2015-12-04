package eu.trentorise.opendata.traceprov.tracel;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.geojson.Feature;

@Value.Immutable
@SimpleStyle
@JsonSerialize(as = Property.class)
@JsonDeserialize(as = Property.class)
public abstract class AProperty extends Token {
        
    /**
     * The property, like "myProp"  or "My Property"
     * By default is the empty string "".
     */
    @Value.Default
    public String getLabel(){
        return "";
    }
       
    @Override
    public String toText(){
        if (Tracel.isValidId(getLabel())){
            return "." + getLabel();
        } else {
            return "[\"" + getLabel() + "\"]";
        }
    }
    
}
