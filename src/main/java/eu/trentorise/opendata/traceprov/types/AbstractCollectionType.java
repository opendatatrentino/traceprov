package eu.trentorise.opendata.traceprov.types;

import java.util.Collection;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.traceprov.data.TraceData;

/**
 * This is just ot generate {@link #CollectionType} implementation
 *
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = CollectionType.class)
@JsonDeserialize(as = CollectionType.class)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AbstractCollectionType extends ACollectionType {

    /**
     * By default assumes {@link AnyType#of()}
     */
    @Value.Default
    public TraceType getSubtype() {
	return AnyType.of();
    }

    @Override
    public Class getJavaClass() {
	return Collection.class;
    }

    @Value.Default
    @Override
    public boolean isImmutable() {
	return false;
    }

    /**
     * True if the collection is optimized to fully reside in RAM, false
     * otherwise.
     * 
     */
    @Value.Default
    @Override
    public boolean isInMemory() {
	return true;
    }

    /**
     * True if allows fast access (nearly O(1)) of i-th element.
     */
    @Value.Default
    @Override
    public boolean isRandomAccess() {
	return false;
    }

    /**
     * True if collection values are generated on demand.
     */
    @Value.Default
    @Override
    public boolean isLazy() {
	return false;
    }

    /**
     * True if collection has size() method AND calling it is O(1)
     */
    @Value.Default
    @Override
    public boolean isSizeFast() {
	return false;
    }

    @Value.Default
    @Override
    public boolean isAllowingDuplicates() {
	return true;
    }

    @Value.Default
    @Override
    public boolean isNullHostile() {
	return false;
    }

    @Value.Default
    @Override
    public boolean isIterationOrderConsistentWithInsertion() {
	return false;
    }

}
