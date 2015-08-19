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
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.validation.Ref;
import java.io.Serializable;
import java.util.Objects;


/**
 * Definition that binds a name to a type. It may be a class definition, a
 * property definition, etc...
 *
 *
 *  * todo check not null in each builder set.
 *
 * @author David Leoni
 */
public class Def<T extends Type> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Def<AnyType> INSTANCE = new Def(
            AnyType.of().getId(),
            AnyType.of(),
            Concept.of(),
            Dict.of(),
            Dict.of(),
            Ref.of());

    private T type;
    private String id;
    private String canonicalName;
    private Dict name;
    private Dict description;
    private Concept concept;
    private Ref ref;

    private Def() {
        id = "";
        canonicalName = "";
        name = Dict.of();
        description = Dict.of();
        concept = Concept.of();
        ref = Ref.of();
        type = null;
    }

    @JsonCreator
    private Def(@JsonProperty("id") String id,
            @JsonProperty("type") T type,
            @JsonProperty("concept") Concept concept,
            @JsonProperty("name") Dict name,
            @JsonProperty("description") Dict description,
            @JsonProperty("ref") Ref ref) {
        this();

        this.type = type;
        this.id = id;
        this.concept = concept;
        this.canonicalName = canonicalName;
        this.name = name;
        this.description = description;
        this.ref = ref;
        checkState();
    }

    private void checkState() {
        checkNotNull(id);
        checkNotNull(concept);
        checkNotNull(canonicalName);
        checkNotNull(name);
        checkNotNull(description);
        checkNotNull(type);
        checkNotNull(ref);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.type);
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.canonicalName);
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.description);
        hash = 17 * hash + Objects.hashCode(this.concept);
        hash = 17 * hash + Objects.hashCode(this.ref);
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
        if (!Objects.equals(this.canonicalName, other.canonicalName)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.concept, other.concept)) {
            return false;
        }
        if (!Objects.equals(this.ref, other.ref)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Def{" + "type=" + type + ", id=" + id + ", canonicalName=" + canonicalName + ", name=" + name + ", description=" + description + ", concept=" + concept + ", ref=" + ref + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * The fully qualified definition package and name, like i.e.
     * "org.mycompany.Person". The id is intended as canonical name that should
     * be used in scripting, so it should be in English and camelcased, i.e. for
     * a class could be CreativeWork, BroadcastService, and for a property could
     * be officeHours, storageRooms
     *
     */
    public String getId() {
        return id;
    }

    /**
     * The id of the object being defined as it is identified on the system of
     * origin. Since this could be arbitrary it may or not be an IRI and may be
     * non human readble, i.e. http://mycompany.com/types/3867
     *
     */
    public String getOriginId() {
        return id;
    }

    /**
     * The high-level concept describing the object being defined. For example,
     * it could be a Dublin core class or attribute.
     *
     * For example, for a class the id of the concept might be an IRI to a
     * well-known type like i.e. https://schema.org/Person or for a property
     * could be an IRI to a well-known property like i.e.
     * <a href="http://schema.org/name" target="_blank">http://schema.org/name</a>
     *
     * If unknown, {@link Concept #of()} is used.
     *
     */
    public Concept getConcept() {
        return concept;
    }

    /**
     * Human-readable name of the definition. It can contain spaces.
     *
     * @see #getCanonicalName()
     */
    public Dict getName() {
        return name;
    }

    /**
     * Natural language description of the definition
     */
    public Dict getDescription() {
        return description;
    }

    /**
     * The type assigned to the name
     */
    public T getType() {
        return type;
    }

    /**
     * A reference to a document and position where the definition is located
     */
    public Ref getRef() {
        return ref;
    }

    /**
     * Default definition.
     */
    public static Def<AnyType> of() {
        return INSTANCE;
    }

    public static class Builder<W extends Type> {

        private static final String ERR_SET = "Tried to set property of an already built TypeDef!";

        private boolean built;
        private final Def<W> td;

        private Builder() {
            built = false;
            td = new Def();
        }

        public Def<W> build() {
            td.checkState();
            built = true;
            return td;
        }

        public Builder<W> setType(W type) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }
            td.type = type;
            return this;
        }

        public Builder<W> setId(String id) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }

            td.id = id;
            return this;
        }

        public Builder<W> setCanonicalName(String canonicalName) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }

            td.canonicalName = canonicalName;
            return this;
        }

        public Builder<W> setName(Dict name) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }

            td.name = name;
            return this;
        }

        public Builder<W> setDescription(Dict description) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }

            td.description = description;
            return this;
        }

        public Builder<W> setConcept(Concept concept) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }

            td.concept = concept;
            return this;
        }

        public Builder<W> setRef(Ref ref) {
            if (built) {
                throw new IllegalStateException(ERR_SET);
            }
            td.ref = ref;
            return this;
        }

    }

}
