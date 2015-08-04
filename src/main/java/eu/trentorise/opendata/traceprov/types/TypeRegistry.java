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

import static com.google.common.base.Preconditions.checkNotNull;
import eu.trentorise.opendata.commons.NotFoundException;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Todo this is much in WTF status
 * 
 * @author David Leoni 
 */
public class TypeRegistry {

    private Map<String, ClassDef> classes;

    private TypeRegistry() {
        this.classes = new HashMap();
    }

    /**
     * @return The previous value associated with key, or null if there was no
     * mapping for key.
     */
    @Nullable
    public ClassDef put(String id, ClassDef classDef) {
        checkNotEmpty(id, "Invalid classDef id!");
        checkNotNull(classDef);
        ClassDef ret = this.classes.get(id);
        this.classes.put(id, classDef);
        return ret;
    }

    public boolean hasClassDef(String classDefId) {
        checkNotEmpty(classDefId, "Invalid classDef id!");
        return !(classes.get(classDefId) == null);
    }

    /**
     * @throws eu.trentorise.opendata.commons.NotFoundException
     */
    public ClassDef getClassDef(String classDefId) {
        checkNotEmpty(classDefId, "Invalid classDef id!");
        ClassDef classDef = classes.get(classDefId);
        if (classDef == null) {
            throw new NotFoundException("Couldn't find classDefId " + classDefId);
        }
        return classDef;

    }
    
    
    Type guessType(Object obj){
        throw new UnsupportedOperationException("todo implement me");    
    }
    
    public static TypeRegistry of(){
        return new TypeRegistry();
    }
}
