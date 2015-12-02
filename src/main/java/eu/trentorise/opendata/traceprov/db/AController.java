package eu.trentorise.opendata.traceprov.db;

import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.immutables.value.Value;

import com.google.common.collect.ImmutableMap;


/**
 * A controller of {@link TraceData} nodes which are in the same {@code sameas} clique.
 * 
 * todo 0.4 what happens when cliques split/merge/etc ??
 */
@Immutable
public abstract class AController {

    
    /**
     * The id of a node in the controlled sameas clique.
     * Defaults to -1.
     */
    @Value.Default
    public long getControlledId(){
        return -1;
    }
    
    /**
     * Returns an immutable map containing field names of the view and eventual
     * corresponding errors. todo using Object instead of ValidationError
     */   
    public abstract Map<String, Object> getErrors();

    /**
     * The status of the controller.
     * By default is {@link ControllerStatus#UNTOUCHED}
     */
    @Value.Default
    public ControllerStatus getStatus(){
        return ControllerStatus.UNTOUCHED;
    }
}




