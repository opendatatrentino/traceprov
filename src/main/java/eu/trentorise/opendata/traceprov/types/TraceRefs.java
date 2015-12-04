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

import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.commons.validation.Ref.Builder;
import eu.trentorise.opendata.traceprov.data.DataNodes;
import eu.trentorise.opendata.traceprov.tracel.TraceQueries;

import java.util.logging.Logger;

/**
 * Methods to create references. todo do we need this?
 *
 * @author David Leoni
 */
public final class TraceRefs {

    private static final Logger LOG = Logger.getLogger(TraceRefs.class.getName());

    /**
     * todo this dublic core stuff is probably not needed.
     * 
     * @deprecated
     */
    public static final String DUBLIC_CORE_TERMS_TITLE = "http://purl.org/dc/terms/title";

    /**
     * todo this dublic core stuff is probably not needed.
     * 
     * @deprecated
     */
    public static final String DUBLIC_CORE_TERMS_SPATIAL = "http://purl.org/dc/terms/spatial";

    /**
     * todo this dublic core stuff is probably not needed.
     * 
     * @deprecated
     */
    public static final String DUBLIC_CORE_TERMS_TEMPORAL = "http://purl.org/dc/terms/temporal";

    /**
     * todo this dublic core stuff is probably not needed.
     * 
     * @deprecated
     */
    public static final String DUBLIC_CORE_TERMS_PUBLISHER = "http://purl.org/dc/terms/publisher";

    /**
     * todo this dublic core stuff is probably not needed.
     * 
     * @deprecated
     */
    public static final String DUBLIC_CORE_TERMS_LICENSE = "http://purl.org/dc/terms/license";

    private TraceRefs(){}
    
    /**
     * 
     * Makes a Ref to several DataNodes. DocumentId is fixed to
     * {@link DataNodes.DATANODES_IRI}/datanodes, TODO TRACEPATH
     * 
     */
    // todo use custom db documentid?
    public static Ref dataNodesRef(Iterable<Long> nodeIds) {
        Builder retb = Ref.builder();

        retb.setDocumentId(DataNodes.DATANODES_IRI);
        //retb.setTracePath(TraceQueries.dataNodesPath(nodeIds));

        LOG.warning("TODO: dataNodesRef NEEDS TO BE MUCH BETTER SPEFICIED! ");
        return retb.build();

    }

   
 
}
