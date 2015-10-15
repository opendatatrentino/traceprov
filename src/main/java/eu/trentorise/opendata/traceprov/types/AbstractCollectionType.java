package eu.trentorise.opendata.traceprov.types;

import java.util.Collection;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.data.DataNode;

/**
 * This is just ot generate {@link #CollectionType} implementation
 *
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = CollectionType.class)
@JsonDeserialize(as = CollectionType.class)
abstract class AbstractCollectionType extends ACollectionType {

    /**
     * By default assumes {@link AnyType#of()}
     */
    @Value.Default
    public Type getSubtype() {
	return AnyType.of();
    }
    
    @Override
    public Class getJavaClass() {
	return Collection.class;
    }
   
    
    @Override
    public boolean isImmutable() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }   

    /**
     * True if the collection is optimized to fully reside in RAM, false
     * otherwise.
     * 
     */
    public boolean isInMemory() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * True if allows fast access (nearly O(1)) of i-th element.
     */
    public boolean isRandomAccess() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * True if collection values are generated on demand.
     */
    public boolean isLazy() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * True if collection has size() method AND calling it is O(1)
     */
    public boolean isSizeFast() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isAllowingDuplicates(){
	// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isNullHostile(){
	// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public boolean isIterationOrderConsistentWithInsertion(){
	// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
