package eu.trentorise.opendata.traceprov.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.Type;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 * TODO Do we really need this class? Let's just use factory methods in {@link DataNodes} for now.
 * 
 *
 */
public class SimpleDataNodeFactory implements DataNodeFactory {

    private static final SimpleDataNodeFactory INSTANCE = new SimpleDataNodeFactory();
    
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
    public DataNode makeNode(
	    		Ref ref, 
	    		NodeMetadata metadata, 
	    		@Nullable Object obj,
	    		TypeRegistry typeRegistry) {

	if (obj == null || obj instanceof String || obj instanceof Number) {
	    Type canType = typeRegistry.getCanonicalTypeFromInstance(obj);
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
	    Map<String, DataNode> newMap = new HashMap();
	    
	    for (Object key : map.keySet()) {
		String keyString;
		if (key == null){
		    throw new IllegalArgumentException("Found map with nasty null key while converting java object tree to DataNode tree! value toString() = " + map.get(null).toString());
		} else if (key instanceof String){
		    keyString = (String) key;
		} else {
		    throw new IllegalArgumentException("Found map with non-string key while converting java object tree to DataNode tree! Key toString() = " + key.toString());
		}
		
				
		Object subObj = map.get(keyString);
		
		DataNode subNode = makeNode(
			DataNodes.makeSubRef(ref, keyString),
			DataNodes.makeMetadata(metadata, subObj, typeRegistry),
			subObj,
			typeRegistry);
		
		newMap.put(keyString, subNode);
	    }	
	    return DataMap.of(ref, metadata, newMap);
	} else if(obj instanceof Iterable)  {
	    Iterable col = (Iterable) obj;
	    List ret = new ArrayList();
	    int index = 0;
	    for (Object item : col){		
		DataNode nodeItem = DataNodes.makeNode(DataNodes.makeSubRef(ref, index),
			DataNodes.makeMetadata(metadata, item, typeRegistry),
			item,
			typeRegistry);
		ret.add(nodeItem);
		index += 1;
	    }
	    return DataArray.of(ref, metadata, ret);
	} else {
	    return DataObject.of(ref, metadata, obj);
	}
	    
	
    }

}