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
package eu.trentorise.opendata.traceprov.data;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import eu.trentorise.opendata.traceprov.types.AnyType;
import eu.trentorise.opendata.traceprov.types.ClassDef;
import eu.trentorise.opendata.traceprov.types.PropertyMapping;
import eu.trentorise.opendata.traceprov.types.Type;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintViolation;

/**
 * Tree-like generic data model to represent a file and mappings to track data
 * provenance. Also holds constraint violations.
 *
 * @author David Leoni
 */
public class ProvFile implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final ProvFile INSTANCE = new ProvFile();

    private DcatMetadata dcatMetadata;
    private Type type;
    private List<ClassDef> classDefs;
    private ImmutableList<ConstraintViolation> typeErrors;
    private Data data;
    private List<ConstraintViolation> dataErrors;
    private ImmutableList<PropertyMapping> mappings;

    ProvFile() {
        this.dcatMetadata = DcatMetadata.of();
        this.type = AnyType.of();
        this.classDefs = ImmutableList.of();
        this.typeErrors = ImmutableList.of();
        this.data = DataMap.of();
        this.dataErrors = new ArrayList();
        this.mappings = ImmutableList.of();
    }

    ProvFile(
            DcatMetadata dcatMetadata,
            Type type,
            Iterable<ConstraintViolation> schemaErrors,
            Data data,
            List<ConstraintViolation> dataErrors,
            Iterable<PropertyMapping> mappings) {
        checkNotNull(dcatMetadata);
        checkNotNull(type);
        checkNotNull(schemaErrors);
        checkNotNull(data);
        checkNotNull(dataErrors);
        checkNotNull(mappings);

        this.dcatMetadata = dcatMetadata;
        this.type = type;
        this.typeErrors = ImmutableList.copyOf(schemaErrors);
        this.data = data;
        this.dataErrors = dataErrors;
        this.mappings = ImmutableList.copyOf(mappings);
    }

    /**
     * The Dcat metadata associated to the original file. If no metadata was
     * found, {@link DcatMetadata#of()} is returned.
     */
    public DcatMetadata getDcatMetadata() {
        return dcatMetadata;
    }

    /**
     * The type of the tree common tree representation of the original file. If
     * no definition was present or it was invalid,
     * {@link eu.trentorise.opendata.traceprov.types.AnyType#of()} is returned.
     */
    //  todo add validator tag for non-any type
    public Type getType() {
        return type;
    }

    public List<ClassDef> getClassDefs() {
        return classDefs;
    }

    /**
     * Returns the validation errors found in the original file.
     */
    public ImmutableList<ConstraintViolation> getTypeErrors() {
        return typeErrors;
    }

    /**
     * The high-level mappings from source file elements (columns, schema node
     * paths, ...) to the target schema property paths.
     *
     * !! TODO !! if conversion is totally automatic we don't need mappings with
     * scores. We need to decide it.
     */
    public ImmutableList<PropertyMapping> getMappings() {
        return mappings;
    }

    /**
     * Returns the data content of the file as a hierarchical tree. If no data
     * was found, {@link DataMap#of()} is returned.
     */
    public Data getData() {
        return data;
    }

    /**
     * Returns the validation errors found in the original file.
     */
    public List<ConstraintViolation> getDataErrors() {
        return dataErrors;
    }

    /**
     * Default singleton    
     */
    public static ProvFile of() {
        return INSTANCE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ProvFile provFile;
        private boolean doneBuilding;

        public Builder() {
            this.provFile = new ProvFile();
            this.provFile.dataErrors = new ArrayList();
            doneBuilding = false;
        }

        public void setDcatMetadata(DcatMetadata dcatMetadata) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            checkNotNull(dcatMetadata);
            this.provFile.dcatMetadata = dcatMetadata;
        }

        public void setType(Type type) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            checkNotNull(type);
            this.provFile.type = type;
        }

        public void setClassDefs(Iterable<ClassDef> classDefs) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            this.provFile.classDefs = ImmutableList.copyOf(classDefs);
        }

        public void setTypeErrors(Iterable<ConstraintViolation> typeErrors) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            checkNotNull(typeErrors);
            this.provFile.typeErrors = ImmutableList.copyOf(typeErrors);
        }

        public void setData(Data data) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            checkNotNull(data);
            this.provFile.data = data;
        }

        public void setDataErrors(Iterable<ConstraintViolation> dataErrors) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            checkNotNull(dataErrors);
            this.provFile.dataErrors = Lists.newArrayList(dataErrors);
        }

        public void addDataError(ConstraintViolation dataError) {
            checkNotNull(dataError);
            this.provFile.dataErrors.add(dataError);
        }
   
        
        public void setMappings(Iterable<PropertyMapping> mappings) {
            if (doneBuilding) {
                throw new IllegalStateException("The object has already been built!");
            }
            checkNotNull(mappings);
            this.provFile.mappings = ImmutableList.copyOf(mappings);
        }

        public ProvFile build() {
            for (ClassDef classDef : this.provFile.classDefs) {

            }
            doneBuilding = true;
            return provFile;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.provFile);
            hash = 59 * hash + (this.doneBuilding ? 1 : 0);
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
            final Builder other = (Builder) obj;
            if (!Objects.equals(this.provFile, other.provFile)) {
                return false;
            }
            if (this.doneBuilding != other.doneBuilding) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Builder{" + "provFile=" + provFile + ", doneBuilding=" + doneBuilding + '}';
        }

    }
}
