package eu.trentorise.opendata.traceprov.tracel;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.tracel.PropertyPath.Builder;

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

    
    public static PropertyPath of(List<String> path){
        checkNotEmpty(path, "Invalid fields of property path!");
        return PropertyPath.builder()
                .setRoot(Id.of(path.get(0)))
                .addProperties(Iterables.skip(path, 1)).build();
    }
    
    public static PropertyPath of(String rootElement, String... props ){
        PropertyPath.Builder ret = PropertyPath.builder().setRoot(Id.of(rootElement));
                
        for (String p : props){
            ret.addProperties(Property.of(p));    
        }
                
        return ret.build();
    }
    
    /**
     * Returns a new PropertyPath with provided properties appended to the existing ones.
     */
    public PropertyPath appendProperties(String... props){
        PropertyPath.Builder ret = PropertyPath.builder().from((PropertyPath) this);
                
        for (String p : props){
            ret.addProperties(Property.of(p));    
        }
        
        return ret.build();
    }

    /**
     * Returns a new PropertyPath with provided properties appended to the existing ones.
     */
    public PropertyPath appendProperties(Property... props){
        PropertyPath.Builder ret = PropertyPath.builder().from((PropertyPath) this);
                
        for (Property p : props){
            ret.addProperties(p);    
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
    
    public static abstract class Builder {
                
        public abstract PropertyPath.Builder addProperties(Property element);
        
        public PropertyPath.Builder addProperties(Iterable<String> path){
            for (String label : path){
                addProperties(Property.of(label));
            }
            return addProperties();
        }
        
        /**
         * @see {@link #addProperties(List)}
         *  
         */
        public PropertyPath.Builder addProperties(String... path){
            return addProperties(Arrays.asList(path));
        }
    }
    
}
