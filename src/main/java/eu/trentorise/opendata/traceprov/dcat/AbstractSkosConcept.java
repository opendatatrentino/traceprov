/**
 * *****************************************************************************
 * Copyright 2013-2014 Trento Rise (www.trentorise.eu/)
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************
 */
package eu.trentorise.opendata.traceprov.dcat;

import eu.trentorise.opendata.traceprov.Dict;
import eu.trentorise.opendata.traceprov.BuilderStyle;
import org.immutables.value.Value;

/**
 * Models a
 * <a href="http://www.w3.org/2009/08/skos-reference/skos.html#Concept">
 * SkosConcept </a>
 *
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStyle
public abstract class AbstractSkosConcept {
    
    public static final String CLASS_URI="http://www.w3.org/2004/02/skos/core#Concept";
    

    /**
     * skos:inScheme Default value is the empty concept scheme
     * {@link SkosConceptScheme#of()}
     */
    @Value.Default
    @Value.Parameter
    public AbstractSkosConceptScheme getInScheme() {
        return SkosConceptScheme.of();
    }

    /**
     * skos:prefLabel i.e. "Accountability"
     */
    @Value.Parameter
    @Value.Default
    public Dict getPrefLabel(){
        return Dict.of();
    };

    @Value.Default
    @Value.Parameter
    public String getUri() {
        return "";
    }

}
