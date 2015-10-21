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
package eu.trentorise.opendata.traceprov.engine;

import eu.trentorise.opendata.commons.NotFoundException;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.util.Locale;

/**
 * Todo this is much in WTF status
 *
 * @author David Leoni
 */
public class ConverterRegistry {

    private TypeRegistry typeRegistry;

    private ConverterRegistry() {
        this.typeRegistry = TypeRegistry.of();
    }

    /**
     *
     * Casts value to target type. Method should never throw, and in case of
     * failures they should be reported in the returned {CastResult} object.
     * <br/>
     *
     * @param source a value of type {@code S}
     * @param sourceLocale the locale of the source value. When {Locale#ROOT}
     * the locale inside the value will be used, if present. See
     * {@link #getLocale(java.lang.Object)}
     * @param targetType the target type of the conversion
     */
    public CastResult cast(
            Object source,
            Locale sourceLocale,
            TraceType targetType) {
        throw new UnsupportedOperationException("TODO implement me!");
    }

    /**
     * Returns true if converter from {@code S} to {@code T} has been
     * registered.
     *
     * @see #getConverter(eu.trentorise.opendata.traceprov.types.TraceType,
     * eu.trentorise.opendata.traceprov.types.TraceType)
     */
    public boolean hasConverter(TraceType sourceType, TraceType targetType) {
        throw new UnsupportedOperationException("TODO implement me!");
    }

    /**
     * The converter from type {@code S} to {@code T}, if available
     *
     * @throws NotFoundException is not available
     * @see #hasInverse(eu.trentorise.opendata.traceprov.types.TraceType,
     * eu.trentorise.opendata.traceprov.types.TraceType)
     */
    public <S extends TraceType, T extends TraceType> Converter<T, S> getConverter(S sourceType, T targetType) {
        throw new UnsupportedOperationException("TODO implement me!");
    }
}
