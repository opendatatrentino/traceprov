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

import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.trentorise.opendata.commons.jackson.TodCommonsModule;
import eu.trentorise.opendata.traceprov.geojson.GeoJson;
import eu.trentorise.opendata.traceprov.types.TraceType;
import java.io.IOException;

/**
 * Module for serializaing/deserializing TraceProv classes to/from json with
 * Jackson.
 *
 * @author David Leoni
 * @since 0.3
 */
public final class TraceProvModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the module and registers all the needed serializers and
     * deserializers for Tod Commons objects
     */
    public TraceProvModule() {
        super("traceprov-jackson", TodCommonsModule.readJacksonVersion(TraceProvModule.class));

        addDeserializer(GeoJson.class, new GeoJsonDeserializer());
        addDeserializer(TraceType.class, new TraceTypeDeserializer());

    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // it's like this in Guava module!
    }

    @Override
    public boolean equals(Object o
    ) {
        return this == o; // it's like this in Guava module!
    }

    /**
     * Registers in the provided object mapper the jackson traceprov module and
     * also the required tod commons and guava module.
     */
    public static void registerModulesInto(ObjectMapper om) {
        TodCommonsModule.registerModulesInto(om);
        om.registerModule(new TraceProvModule());
    }

    public static class GeoJsonDeserializer extends StdDeserializer<GeoJson> {

        public GeoJsonDeserializer() {
            super(GeoJson.class);
        }

        public GeoJsonDeserializer(Class<GeoJson> vc) {
            super(vc);
        }

        @Override
        public GeoJson deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode root = (ObjectNode) mapper.readTree(jp);
            String type = root.get("type").asText();
            String className = GeoJson.class.getPackage().getName() + "." + type;
            Class<? extends GeoJson> clazz;
            try {
                clazz = (Class<? extends GeoJson>) Class.forName(className);
                return mapper.convertValue(root, clazz);
            }
            catch (ClassNotFoundException ex) {
                throw new TraceProvException("Cannot resolve geojson class : " + className, ex);
            }
            catch (Exception ex) {
                throw new TraceProvException("Cannot convert json to resolved geojson class: " + className, ex);
            }

        }

    }

    public static class TraceTypeDeserializer extends StdDeserializer<TraceType> {

        public TraceTypeDeserializer() {
            super(TraceType.class);
        }

        public TraceTypeDeserializer(Class<TraceType> vc) {
            super(vc);
        }

        @Override
        public TraceType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode root = (ObjectNode) mapper.readTree(jp);
            String typeId = root.get("id").asText();  
            TraceType traceType = TraceDb.getDb().getTypeRegistry().get(typeId);
                        
            try {                
                return mapper.convertValue(root, traceType.getClass());
            }            
            catch (Exception ex) {
                throw new TraceProvException("Cannot convert json to resolved traceprov type: " + traceType, ex);
            }

        }

    }

}

class TraceProvModuleException extends JsonProcessingException {

    public TraceProvModuleException(String msg) {
        super(msg);
    }

    public TraceProvModuleException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }

}
