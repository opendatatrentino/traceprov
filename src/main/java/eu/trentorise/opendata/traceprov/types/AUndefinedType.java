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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.SimpleStyle;
import org.immutables.value.Value;

/**
 * Singleton type for infamous Javascript value {@code undefined}. Users cannot
 * declare a variable to be of this type (this is the same behaviour as in
 * TypeScript)
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = UndefinedType.class)
@JsonDeserialize(as = UndefinedType.class)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AUndefinedType extends TraceType {

    private static final long serialVersionUID = 1L;
    /*
    @Override
    public String getDatatypeStandardId() {	
        return Types.TRACEPROV_TYPES + "undefined";
    }*/

    @Override
    public Class getJavaClass() {
        return Object.class;
    }
    
    @Override
    public boolean isImmutable() {
	return true;
    }
}
