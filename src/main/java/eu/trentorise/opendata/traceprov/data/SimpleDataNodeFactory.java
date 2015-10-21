package eu.trentorise.opendata.traceprov.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 * TODO Do we really need this class? Let's just use factory methods in {@link DataNodes} for now.
 * 
 *
 */
public class SimpleDataNodeFactory implements DataNodeFactory {

    private static final SimpleDataNodeFactory INSTANCE = new SimpleDataNodeFactory();
    
    private static Logger LOG = Logger.getLogger(SimpleDataNodeFactory.class.getSimpleName());
    
    public SimpleDataNodeFactory(){
    }
    
    public static SimpleDataNodeFactory of(){
	return INSTANCE;
    }
    
    /**
     * 
     *  {@inheritDoc}
     *  
     *  In this implementation DataMap and DataArray nodes will be backed by HashMap and ArrayList.
     * 
     */
    // TODO make it stack based instead of using recursion 
    @Override
    public TraceData makeNode(
	    		Ref ref, 
	    		NodeMetadata metadata, 
	    		@Nullable Object obj) {

	TypeRegistry typeRegistry = TraceDb.getCurrentDb().getTypeRegistry();
	if (obj == null || obj instanceof String || obj instanceof Number) {
	    TraceType canType = typeRegistry.getCanonicalTypeFromInstance(obj);
	    NodeMetadata newMetadata;
	    if (metadata.getType().equals(AnyType.of())){
		newMetadata = metadata.withType(canType);
	    } else {
		newMetadata = metadata;
	    }
	    return DataValue.of(ref, newMetadata, obj);

	} else if (obj instanceof Map) {
	    Map map = (Map) obj;
	    boolean allDataNodes = true;
	    Map<String, TraceData> newMap = new HashMap();
	    
	    for (Object key : map.keySet()) {
		String keyString;
		if (key == null){
		    LOG.fine("Found map with nasty null key while converting java object tree to DataNode tree! Converting to generic DataObject.");
		    return DataObject.of(ref, metadata, obj);
		} else if (key instanceof String){
		    keyString = (String) key;
		} else {
		    LOG.fine("Found map with non-string key while converting java object tree to DataNode tree! Converting to generic DataObject.");
		    return DataObject.of(ref, metadata, obj);
		}
		

		Object subObj = map.get(keyString);
		
		TraceData subNode = makeNode(
			DataNodes.makeSubRef(ref, keyString),
			DataNodes.makeMetadata(metadata, subObj),
			subObj);
		
		newMap.put(keyString, subNode);
	    }	
	    return DataMap.of(ref, metadata, newMap);
	} else if(obj instanceof Iterable)  {
	    Iterable col = (Iterable) obj;
	    List ret = new ArrayList();
	    int index = 0;
	    for (Object item : col){		
		TraceData nodeItem = DataNodes.makeNode(DataNodes.makeSubRef(ref, index),
			DataNodes.makeMetadata(metadata, item),
			item);
		ret.add(nodeItem);
		index += 1;
	    }
	    return DataArray.of(ref, metadata, ret);
	} else {
	    return DataObject.of(ref, metadata, obj);
	}
	    
	
    }

}