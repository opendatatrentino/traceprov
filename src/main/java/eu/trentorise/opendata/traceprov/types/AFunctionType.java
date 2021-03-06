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
import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.commons.SimpleStyle;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@SimpleStyle
@JsonSerialize(as = FunctionType.class)
@JsonDeserialize(as = FunctionType.class)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AFunctionType extends TraceType {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Todo what about empty list??
     */
    public List<? extends TraceType> getSourceTypes(){
        return ImmutableList.of(AnyType.of());
    }
    
    @Value.Default
    public TraceType getTargetType(){
        return AnyType.of();
    }
    /*
    @Override
    public String getDatatypeStandardId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    @Override
    public Class getJavaClass() {
        return Object.class ; // todo
    }
    
    @Override
    public boolean isImmutable(){
	return true;	
    }
}    
