package eu.trentorise.opendata.traceprov.tracel;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;

/**
 * A TRACEL expression made only of a root identifier and {@link Property properties} following it
 *
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = PropertyPath.class)
@JsonDeserialize(as = PropertyPath.class)
abstract class APropertyPath extends Expr {

    private static final Logger LOG = Logger.getLogger(APropertyPath.class.getSimpleName());

    public abstract List<Property> getProperties();

    /**
     * The root element. By default returns "this"
     */
    @Value.Default
    public Id getRoot() {
        return Id.of();
    }

    @Override
    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRoot().toText());
        for (Property prop : getProperties()) {
            sb.append(prop.toText());
        }
        return sb.toString();
    }

    public PropertyPath next() {
        if (getProperties().size() <= 1) {
            throw new TraceProvException("Can't make subpath from a path of one element!");
        }
        return PropertyPath.builder()
                           .addAllProperties(getProperties().subList(1, getProperties().size()))
                           .build();
    }
    
    public static PropertyPath of(String rootElement, String... props ){
        PropertyPath.Builder ret = PropertyPath.builder().setRoot(Id.of(rootElement));
        
        ImmutableList.Builder<Property> retProps = ImmutableList.builder();
        for (String p : props){
            ret.addProperties(Property.of(p));    
        }
                
        return ret.build();
    }
    
    /**
     * Returns the root identifier followed by all the properties as strings.
     */
    public List<String> labels(){
        ImmutableList.Builder<String> retb = ImmutableList.builder();
        retb.add(getRoot().getLabel());
        for (Property p : getProperties()){
            retb.add(p.getLabel());
        }
        return retb.build();
    }
}
