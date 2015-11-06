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
import java.util.List;
import java.util.Set;

import org.immutables.value.Value;

@Value.Immutable
@SimpleStyle
@JsonSerialize(as = SetType.class)
@JsonDeserialize(as = SetType.class)
@JsonIgnoreProperties(ignoreUnknown = true)
// todo review methods. btw, what is exactly a set for us??
abstract class ASetType extends ACollectionType {

    private static final long serialVersionUID = 1L;

    @Override
    public Class getJavaClass() {
	return Set.class;
    }

    @Override
    public boolean isImmutable() {
	return getSubtype().isImmutable();
    }
  
    
    @Override
    public boolean isInMemory() {
	return true;
    }

    
    @Override
    public boolean isRandomAccess() {
	return true;
    }

    
    @Override
    public boolean isLazy() {
	return false;
    }

    @Override
    public boolean isSizeFast() {
	return true;
    }

    @Override
    public boolean isAllowingDuplicates() {
	return false;
    }

    @Override
    public boolean isNullHostile() {
	return true;
    }

    @Override
    public boolean isIterationOrderConsistentWithInsertion() {	
	return true;
    }
    
    
}