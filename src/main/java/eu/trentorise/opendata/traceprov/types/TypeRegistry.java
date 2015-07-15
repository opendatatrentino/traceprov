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

public class TypeRegistry {

    private Map<String, Type> types;

    private TypeRegistry() {
        this.types = new HashMap();
    }

    /**
     * @return The previous value associated with key, or null if there was no
     * mapping for key.
     */
    @Nullable
    public Type putType(String id, Type type) {
        checkNotEmpty(id, "Invalid type id!");
        checkNotNull(type);
        Type ret = this.types.get(id);
        this.types.put(id, type);
        return ret;
    }

    public boolean hasType(String typeId) {
        checkNotEmpty(typeId, "Invalid type id!");
        return !(types.get(typeId) == null);
    }

    /**
     * @throws eu.trentorise.opendata.commons.NotFoundException
     */
    public Type getType(String typeId) {
        checkNotEmpty(typeId, "Invalid type id!");
        Type type = types.get(typeId);
        if (type == null) {
            throw new NotFoundException("Couldn't find typeId " + typeId);
        }
        return type;

    }
}
