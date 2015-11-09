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
package eu.trentorise.opendata.traceprov;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utilities for TraceProv library
 * @author David Leoni
 * @since 0.3
 */
public final class TraceProvs {
    public static final String TRACEPROV_IRI = "http://opendatatrentino.github.io/traceprov/#";
    public static final String TRACEPROV_NAMESPACE = "eu.trentorise.opendata.traceprov";
    public static final String TRACEPROV_PREFIX = "traceprov:";
    
    private TraceProvs(){}

}
