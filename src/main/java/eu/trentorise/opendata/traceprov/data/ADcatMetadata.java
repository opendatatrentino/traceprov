/* 
 * Copyright 2015 TrentoRISE   (trentorise.eu).
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
package eu.trentorise.opendata.traceprov.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.SimpleStyle;
import eu.trentorise.opendata.traceprov.dcat.DcatCatalog;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;
import eu.trentorise.opendata.traceprov.dcat.DcatDistribution;
import java.io.Serializable;
import org.immutables.value.Value;

/**
 * The context of a distribution coming from a dcat catalog. Superceeds
 * open entity <a href="https://github.com/opendatatrentino/openentity-api/blob/feb2015-demo/src/main/java/eu/trentorise/opendata/semantics/model/knowledge/IResourceContext.java" target="_blank"> IResourceContext</a>
 *
 * @author David Leoni <david.leoni@unitn.it>
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = DcatMetadata.class)
@JsonDeserialize(as = DcatMetadata.class)
abstract class ADcatMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The catalog of provenance. If no catalog was associated,
     * {@link DcatCatalog#of()} is returned.
     */
    @Value.Default
    public DcatCatalog getCatalog() {
        return DcatCatalog.of();
    }

    /**
     * The dataset metadata in dcat format
     */
    @Value.Default
    public DcatDataset getDataset() {
        return DcatDataset.of();
    }

    /**
     * The distribution metadata in dcat format
     */
    @Value.Default
    public DcatDistribution getDistribution() {
        return DcatDistribution.of();
    }
}
