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
package eu.trentorise.opendata.traceprov.validation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.LocalizedString;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.schema.Ref;
import org.immutables.value.Value;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Parameter;

/**
 * Represent a generic error in a {@link eu.trentorise.opendata.traceprov.data.ProvFile}
 * @author David Leoni 
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as=GenericError.class)
@JsonDeserialize(as=GenericError.class)
abstract class AGenericError extends Error {
    
    @Default
    @Parameter
    @Override
    public Ref getRef(){
        return Ref.of();
    };
    
    @Default
    @Parameter
    @Override
    public LocalizedString getReason(){
        return LocalizedString.of();
    };
}
