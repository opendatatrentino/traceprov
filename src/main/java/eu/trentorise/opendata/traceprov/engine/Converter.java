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

import eu.trentorise.opendata.traceprov.engine.CastResult;
import eu.trentorise.opendata.traceprov.types.Type;
import java.util.Locale;

/**
 * Converts values of one type to another. todo what about
 * lists/maps/autoboxing?
 *
 * @author David Leoni
 */
public abstract class Converter<S extends Type, T extends Type> {

    public abstract boolean isLongRunning();

    public abstract S getSourceType();

    public abstract T getTargetType();

    /**
     *
     * Casts value to target type.
     * <br/>
     *
     * @param source a value of type {@code S}
     * @param sourceLocale the locale of the source value. When {Locale#ROOT}
     * the locale inside the value will be used, if present. See
     * {@link #getLocale(java.lang.Object)}
     *
     */
    public abstract CastResult cast(
            Object source,
            Locale sourceLocale);

    /**
     * 
     */
    public abstract boolean isLossy();

    
}
