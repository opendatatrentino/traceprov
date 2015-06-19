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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.trentorise.opendata.commons.LocalizedString;
import eu.trentorise.opendata.traceprov.schema.Ref;

/**
 * Represents an error in a {@link eu.trentorise.opendata.traceprov.data.ProvFile}
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
abstract class Error {
    /**
     * Returns a reference to an element with errors in a {@link eu.trentorise.opendata.traceprov.data.ProvFile}
     */
    public abstract Ref getRef();
    /**
     * Human readable error reason
     */
    public abstract LocalizedString getReason();
}
