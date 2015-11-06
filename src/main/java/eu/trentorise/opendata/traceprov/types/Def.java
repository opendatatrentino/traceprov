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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Definition that binds a name to a type. It may be a class definition, a
 * property definition, etc...
 *
 *
 * * todo check not null in each builder set.
 *
 * @author David Leoni
 */
public class Def<T extends TraceType> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Def<AnyType> INSTANCE = new Def();

    private T type;
    private String id;
    private DefMetadata metadata;

    private Def() {
	id = "";
	metadata = DefMetadata.of();
	type = (T) AnyType.of();
    }

    @JsonCreator
    private Def(@JsonProperty("id") @Nullable String id, @JsonProperty("metadata") @Nullable DefMetadata metadata,
	    @JsonProperty("type") @Nullable TraceType type) {
	this();

	this.id = id == null ? "" : id;
	this.metadata = (metadata == null) ? DefMetadata.of() : metadata;
	this.type = (T) (type == null ? AnyType.of() : type);
	checkState();
    }

    private void checkState() {
	checkNotNull(id);
	checkNotNull(metadata);
	checkNotNull(type);
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 73 * hash + Objects.hashCode(this.type);
	hash = 73 * hash + Objects.hashCode(this.id);
	hash = 73 * hash + Objects.hashCode(this.metadata);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Def<?> other = (Def<?>) obj;
	if (!Objects.equals(this.type, other.type)) {
	    return false;
	}
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	if (!Objects.equals(this.metadata, other.metadata)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "Def{" + "type=" + type + ", id=" + id + ", metadata=" + metadata + '}';
    }

    public static Builder builder() {
	return new Builder();
    }

    /**
     * The fully qualified definition package and name, like i.e.
     * "org.mycompany.Person". The id is intended as canonical name that should
     * be used in scripting, so it SHOULD be in English and camelcased, i.e. for
     * a class could be CreativeWork, BroadcastService, and for a property could
     * be officeHours, storageRooms. Still, it MAY contain spaces and other ugly
     * things (i.e. "office hours", "price in €").
     *
     */
    public String getId() {
	return id;
    }

    /**
     * The type assigned to the name
     */
    public T getType() {
	return type;
    }

    /**
     * Metadata about the definition, such as the natural language name,
     * description, etc
     * 
     * @return
     */
    public DefMetadata getMetadata() {
	return metadata;
    }

    /**
     * Default definition.
     */
    public static Def<AnyType> of() {
	return INSTANCE;
    }

    public static class Builder<W extends TraceType> {

	private static final String ERR_SET = "Tried to set property of an already built TypeDef!";

	private boolean built;
	private final Def<W> def;

	private Builder() {
	    built = false;
	    def = new Def();
	}

	public Def<W> build() {
	    def.checkState();
	    built = true;
	    return def;
	}

	/**
	 * @throws IllegalStateException
	 *             if already built
	 */
	private void checkBuilt() {
	    if (built) {
		throw new IllegalStateException(ERR_SET);
	    }
	}

	/**
	 * @see Def#getType()
	 */
	public Builder<W> setType(W type) {
	    checkBuilt();
	    def.type = type;
	    return this;
	}

	/**
	 * @see Def#getId()
	 */
	public Builder<W> setId(String id) {
	    checkBuilt();
	    def.id = id;
	    return this;
	}

	/**
	 * @see Def#getMetadata()
	 */
	public Builder<W> setMetadata(DefMetadata metadata) {
	    checkBuilt();

	    def.metadata = metadata;
	    return this;
	}

    }

}
