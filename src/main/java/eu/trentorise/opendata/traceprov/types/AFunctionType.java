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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.SimpleStyle;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@SimpleStyle
@JsonSerialize(as = FunctionType.class)
@JsonDeserialize(as = FunctionType.class)
abstract class AFunctionType extends Type {
    
    /**
     * Todo what about empty list??
     */
    public List<? extends Type> getSourceTypes(){
        return ImmutableList.of(AnyType.of());
    }
    
    @Value.Default
    public Type getTargetType(){
        return AnyType.of();
    }
    
    @Override
    public String datatypeId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class getJavaClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}    