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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.SimpleStyle;
import org.immutables.value.Value;

/**
 * A reference to a type. 
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = IdType.class)
@JsonDeserialize(as = IdType.class)
abstract class AIdType extends AType {
    
    @Value.Default
    public String getClassId(){
        return "";
    }
    
    @Override
    public String datatypeId(){
        return getClassId();
    }
}