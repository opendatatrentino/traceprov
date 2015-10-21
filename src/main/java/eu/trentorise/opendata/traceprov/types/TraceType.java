/*
 * Copyright 2015 Trento Rise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.trentorise.opendata.traceprov.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.trentorise.opendata.commons.Dict;
import java.io.Serializable;
import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 * A type expression, including basic data types like Int, boolean, etc. Complex
 * ones are defined with {@link ClassType}. TraceProv types are more general
 * than Java ones. todo write more.
 *
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class TraceType<T> implements Serializable {

    private static Logger LOG = Logger.getLogger(TraceType.class.getSimpleName());
    
    protected TraceType() {

    }

    /**
     *
     * A string with the fully qualified name valid as unique identifier for
     * traceprov operations. The namespace must be suitable use in programming
     * languages, for example "eu.trentorise.opendata.traceprov.types.IntType"
     */
    public String getId() {
	return this.getClass().getCanonicalName();
    }

    /**
     * Allows traversing the type tree with a {@link TypeVisitor}.
     *
     * <p>
     * Calls {@code accept} on subnodes and then {@code visitor.visit} on this
     * node. todo this should be abstract!
     * </p>
     */
    public void accept(TypeVisitor v) {
	v.visit(this);
    }

    /**
     * The metadata associated to the type expression when it is defined, i.e.
     * the name, description, provenance...
     */
    public DefMetadata getMetadata() {
	return DefMetadata.builder()
		.setConcept(Concept.of())
		.setOriginId(getId())
		.setName(Dict.of(Locale.ENGLISH, this.getClass().getSimpleName()))
		.build();
    }

    /**
     * The default Java class to represent instances of the type. Other classes
     * may be added with type converters todo define better
     */
    public abstract Class<T> getJavaClass();

    /**
     * Returns whether or not the given {@code object} is an instance of this
     * type.
     */
    public boolean isInstance(Object object) {
	throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * todo review
     *
     * Returns whether or not the given {@code object} is to be considered empty
     * (i.e. empty string, empty list, Person with only string fields which are
     * all empty....)
     *
     * @throws IllegalArgumentException
     *             if {@code object} is not an instance of this type (that is,
     *             {@link #isInstance(java.lang.Object) isInstance(object)}
     *             returns false)
     */
    public boolean isEmpty(Object object) {
	throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * Returns the canonical empty object for this type. The returned object is
     * always the same, even if type is not immutable. For getting always a new
     * instance, see {@link #newEmpty()}
     */
    public Object empty() {
	throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * Returns the canonical empty object for this type. If type is immutable,
     * returns always the same instance, otherwise returns a newly created
     * instance each time.
     * 
     * @see #empty()
     */
    public Object newEmpty() {
	throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * todo review
     *
     * Returns whether or not the given {@code object} is to be considered
     * degenerate (i.e. empty string, string with only spaces, empty list,
     * Person with only string fields which only have spaces......)
     *
     * @throws IllegalArgumentException
     *             if {@code object} is not an instance of this type (that is,
     *             {@link #isInstance(java.lang.Object) isInstance(object)}
     *             returns false)
     */
    public boolean isDegenerate(Object obj) {
	throw new UnsupportedOperationException("todo implement me!");
    }

    /**
     * todo review
     *
     * Returns whether or not the given {@code object} is to be considered dirty
     * (i.e. string containing the word "null", string with different encodings
     * within....)
     *
     * @throws IllegalArgumentException
     *             if {@code object} is not an instance of this type (that is,
     *             {@link #isInstance(java.lang.Object) isInstance(object)}
     *             returns false)
     */
    public boolean isDirty(Object obj) {
	throw new UnsupportedOperationException("todo implement me!");
    }

    /*
     * *
     *
     * @param datatype a dataType as defined in {@link
     * eu.trentorise.opendata.opendatarise.types.OdrDataTypes}.
     * 
     * @param list
     * 
     * @param etypeURL an entity type URL in case the dataType is either a
     * STRUCTURE or an ENTITY. Otherwise, it must be empty.
     * 
     * @param etypeName the name of the etype in case the data either a
     * STRUCTURE or an ENTITY. Otherwise, it must be {@link Dict#of()}.
     * 
     * public OdrType(String datatype, boolean list, String etypeURL, Dict
     * etypeName) {
     * 
     * }
     */

    protected void checkInstance(Object obj){
	if (!isInstance(obj)){
	    throw new IllegalArgumentException("Provided object is not instance of Trace Type " + getId());
	}
    }
    
    /**
     * Returns true if the instances of this type are immutable.
     */
    public abstract boolean isImmutable();
    
    
    public <W> W deepCopy(W obj){
	checkInstance(obj);
	if (isImmutable()){
	    return obj;
	} else {
	    LOG.warning("TODO - CALLED deepCopy ON TYPE " + getId() + " BUT RETURNING SAME INPUT UNCHANGED!!");
	    return obj;
	}
    }
    
    public <W> W shallowCopy(W obj){
	checkInstance(obj);
	if (isImmutable()){
	    return obj;
	} else {
	    LOG.warning("TODO - CALLED deepCopy ON TYPE " + getClass().getSimpleName() + " BUT RETURNING SAME INPUT UNCHANGED!!");
	    return obj;
	}
    }
    
}
