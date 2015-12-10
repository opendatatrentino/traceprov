package eu.trentorise.opendata.traceprov.tracel.java;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.engine.Engine;
import eu.trentorise.opendata.traceprov.tracel.java.Property;

/**
 * The property, like "myProp" or "My Property". Notice that even if the
 * property label can only be a string, if the property is used to access an
 * array and the string actually represents an integer number like "4", then the
 * 4th index of the array will be searched (this follows Javascript semantics).
 *
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = Property.class)
@JsonDeserialize(as = Property.class)
public abstract class AProperty extends Token {

    /**
     * The property, like "myProp" or "My Property" or, if the meaning is an
     * array index, the integer number of the index, like "4".
     * 
     * <p>
     * By default is the empty string "".
     * </p>
     */
    @Value.Default
    public String getLabel() {
        return "";
    }

    @Override
    public String toText() {
        String b = getLabel();
        String all = All.of().toText();
        if (all.equals(b)){
            return "["+all+"]";  // todo make work with all selectors
        } else if (Engine.isValidId(b)) {
            return "." + b;
        } else {
            try {
                Integer.decode(b);                
                return "[" + b + "]";
                
            } catch (NumberFormatException ex){                
                return "[\"" + b + "\"]";
            }
            
            
        }
    }

}
