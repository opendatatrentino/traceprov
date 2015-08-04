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
package eu.trentorise.opendata.traceprov.dcat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import org.immutables.value.Value;

/**
 * VCard java model, specified by
 * <a href="http://www.w3.org/Submission/vcard-rdf/#VCARD" target="_blank">
 * Representing vCard Objects in RDF </a>
 * and <a href="http://www.w3.org/2006/vcard/ns-2006.html" target="_blank"> An
 * Ontology for vCards </a>
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = VCard.class)
@JsonDeserialize(as = VCard.class)
class AVCard {

    /**
     * The uri identifying of the person.
     */
    @Value.Default
    public String getUri() {
        return "";
    }

    /**
     * The formatted name of a person
     *
     * Specified by
     * <a href="http://www.w3.org/2006/vcard/ns-2006.html#fn" target="_blank">
     * v:fn </a>
     * If unknown the empty string is returned.
     */
    @Value.Default
    public String getFn() {
        return "";
    }

    /**
     * A postal or street address of a person.
     *
     * Specified by
     * <a href="http://www.w3.org/2006/vcard/ns-2006.html#adr" target="_blank">
     * v:adr </a>
     * If unknown the empty string is returned.
     */
    @Value.Default
    public String getAdr() {
        return "";
    }

    /**
     * An email address. Specified by
     * <a href="http://www.w3.org/2006/vcard/ns-2006.html#email" target="_blank">
     * v:email </a>
     * If unknown the empty string is returned.
     */
    @Value.Default
    public String getEmail() {
        return "";
    }

    /**
     * A telephone number of a person.
     *
     * Specified by
     * <a href="http://www.w3.org/2006/vcard/ns-2006.html#tel" target="_blank">
     * v:tel </a>
     * If unknown the empty string is returned.
     */
    @Value.Default
    public String getTel() {
        return "";
    }

    /**
     * An organization associated with a person. Specified by
     * <a href="http://www.w3.org/2006/vcard/ns-2006.html#org" target="_blank">
     * v:org </a>
     * If unknown the empty string is returned.
     */
    @Value.Default
    public String getOrg() {
        return "";
    }

}
